package com.itssky.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itssky.common.annotation.DynamicTableName;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.utils.DateUtils;
import com.itssky.system.domain.TbStationInfo;
import com.itssky.system.domain.dto.FD06Dto;
import com.itssky.system.domain.dto.FD26Dto;
import com.itssky.system.domain.dto.FD27Dto;
import com.itssky.system.domain.dto.StationAndDateRangeDTO;
import com.itssky.system.domain.vo.*;
import com.itssky.system.mapper.ExamineMapper;
import com.itssky.system.mapper.SpecialMapper;
import com.itssky.system.mapper.TbStationInfoMapper;
import com.itssky.system.service.TbStationInfoService;
import com.itssky.util.TableUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ExamineServiceImpl {

    private final ExamineMapper examineMapper;

    private final SpecialMapper specialMapper;

    private final TbStationInfoService tbStationInfoService;

    @DynamicTableName(dateParam = "#dto.beginTime")
    public List<FD06Vo> getFd06(FD06Dto dto) {
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        return examineMapper.getFd06(dto);
    }

    @DynamicTableName(dateParam = "#dto.beginTime")
    public List<FD07Vo> getFd07(FD06Dto dto) {
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));

        dto.setTableName("tbsh" + DateUtil.format(dto.getBeginTime(), DatePattern.SIMPLE_MONTH_PATTERN));
        //获取实缴卡数量、加收金额、实缴金额
        List<FD07Vo> shList = examineMapper.getTbSh(dto);
        Map<Integer, FD07Vo> shMap = shList.stream().collect(Collectors.toMap(
                FD07Vo::getOperatorId, i -> i, (m, n) -> m));
        List<FD07Vo> fd07List = examineMapper.getFd07(dto);
        if (CollectionUtils.isEmpty(fd07List)) {
            return new ArrayList<>();
        }
        for (FD07Vo fd07Vo : fd07List) {
            Integer operatorId = fd07Vo.getOperatorId();
            if (Objects.nonNull(shMap.get(operatorId))) {
                fd07Vo.setAddedToll(shMap.get(operatorId).getAddedToll());
                fd07Vo.setSjCash(shMap.get(operatorId).getSjCash());
                fd07Vo.setSjCardNum(shMap.get(operatorId).getSjCardNum());
            }
        }
        //计算应缴金额和合计
        fd07List.forEach(item -> {
            BigDecimal yjDecimal = BigDecimal.ZERO;
            if (Objects.nonNull(item.getAddedToll())) {
                yjDecimal = yjDecimal.add(new BigDecimal(item.getAddedToll().toString()));
            }
            if (Objects.nonNull(item.getYjCash())) {
                yjDecimal = yjDecimal.add(new BigDecimal(item.getYjCash().toString()));
            }
            item.setYjCash(yjDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue());
            BigDecimal totalTollDecimal = buildTotalTollDecimal(item);
            item.setTotalToll(totalTollDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue());
        });
        return fd07List;
    }

    private static BigDecimal buildTotalTollDecimal(FD07Vo item) {
        BigDecimal totalTollDecimal = BigDecimal.ZERO;
        if (Objects.nonNull(item.getSjCash())) {
            totalTollDecimal = totalTollDecimal.add(new BigDecimal(item.getSjCash().toString()));
        }
        if (Objects.nonNull(item.getAddedToll())) {
            totalTollDecimal = totalTollDecimal.add(new BigDecimal(item.getAddedToll().toString()));
        }
        if (Objects.nonNull(item.getEPay())) {
            totalTollDecimal = totalTollDecimal.add(new BigDecimal(item.getEPay().toString()));
        }
        if (Objects.nonNull(item.getMPay())) {
            totalTollDecimal = totalTollDecimal.add(new BigDecimal(item.getMPay().toString()));
        }
        return totalTollDecimal;
    }

    public List<FD27Vo> getFd27(FD27Dto dto) {
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setTableName("tbrawexit" + DateUtil.format(dto.getBeginTime(), DatePattern.SIMPLE_MONTH_PATTERN));
        List<FD27Vo> list = examineMapper.getFd27(dto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            list.forEach(i -> {
                if (Objects.nonNull(i.getTradeTime())) {
                    i.setTradeTimeStr(DateUtil.format(i.getTradeTime(), DatePattern.NORM_DATETIME_PATTERN));
                }
            });
            //增加总记录数行
            FD27Vo total = new FD27Vo();
            total.setStatDate("总记录数");
            BigDecimal totalToll = list.stream().map(FD27Vo::getToll)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            total.setToll(totalToll);
            total.setTradeVehicleClass(String.valueOf(list.size()));
            total.setTotalRow(true);
            list.add(total);
        }
        return list;
    }

    public List<FD26Vo> getFd26(FD26Dto dto) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setTableName("tbrawexit" + DateUtil.format(dto.getTime(), DatePattern.SIMPLE_MONTH_PATTERN));
        List<FD26Vo> list = examineMapper.getFd26(dto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            list.forEach(i -> {
                if (Objects.nonNull(i.getTradeTime())) {
                    i.setTradeTimeStr(DateUtil.format(i.getTradeTime(), DatePattern.NORM_DATETIME_PATTERN));
                }
            });
            //增加总记录数行
            FD26Vo total = new FD26Vo();
            total.setStatDate("总记录数");
            BigDecimal totalToll = list.stream().map(FD26Vo::getToll)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            total.setToll(totalToll);
            total.setTradeVehicleClass(String.valueOf(list.size()));
            total.setTotalRow(true);
            list.add(total);
        }
        return list;
    }


    public List<FD29Vo> getFd29(FD06Dto dto) {
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setTableName("tbrawexit" + DateUtil.format(dto.getBeginTime(), DatePattern.SIMPLE_MONTH_PATTERN));
        List<FD29Vo> list = examineMapper.getFd29(dto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        for (FD29Vo item : list) {
            if (item.getSdCarNum() == null || item.getSdCarNum() == 0) {
                item.setSdCarRate(0D);
                continue;
            }
            if (item.getTotalCarNum() == null || item.getTotalCarNum() == 0) {
                item.setSdCarRate(0D);
                continue;
            }
            BigDecimal rate = new BigDecimal(item.getSdCarNum())
                    .divide(new BigDecimal(item.getTotalCarNum()), 10, RoundingMode.HALF_UP) // 使用更高精度中间值
                    .multiply(new BigDecimal(100))
                    .setScale(2, RoundingMode.HALF_UP);
            item.setSdCarRate(rate.doubleValue());
        }
        return list;
    }

    /**
     * 自助卡机求助响应考核表
     */
    public List<CardboxResortVo> cardboxResort(StationAndDateRangeDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        //构建会查询到的表集合
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbstatexit",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        if (CollectionUtils.isEmpty(dto.getTableNameList())) {
            return new ArrayList<>();
        }
        //时间传参格式化
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));

        List<CardboxResortVo> list = examineMapper.cardboxResort(dto);
        if (!CollectionUtils.isEmpty(list)) {
            //这里是获取了所选站点范围时间范围内的所有求助及取消报警的操作日志，
            //需要根据各字段进行分组，然后计算操作闭环的响应耗时
            return calculateOperationDurations(list);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 计算求助操作到取消报警的耗时时长
     */
    public List<CardboxResortVo> calculateOperationDurations(List<CardboxResortVo> list) {
        List<CardboxResortVo> result = new ArrayList<>();
        Map<String, List<CardboxResortVo>> groupedRecordsMap = list.stream()
                .collect(Collectors.groupingBy(this::generateGroupKey));
        for (List<CardboxResortVo> recordsInGroup : groupedRecordsMap.values()) {
            processSingleGroup(recordsInGroup, result);
        }
        result.sort(Comparator.comparing(CardboxResortVo::getResortTime));
        return result;
    }

    /**
     * 生成分组的Key，格式：车牌号-车道号-工号
     * 用于确保同一车辆、同一车道、同一收费员的操作被分在一起。
     */
    private String generateGroupKey(CardboxResortVo record) {
        // 处理可能为空的字段，避免NullPointerException
        String vehicleLicense = record.getVehicleLicense() != null ? record.getVehicleLicense() : "NULL_VEHICLE";
        Integer laneId = record.getLaneId() != null ? record.getLaneId() : -1;
        Integer operatorId = record.getOperatorId() != null ? record.getOperatorId() : -1;
        return String.format("%s-%d-%d", vehicleLicense, laneId, operatorId);
    }

    /**
     * 处理单个分组内的记录
     */
    private void processSingleGroup(List<CardboxResortVo> groupRecords, List<CardboxResortVo> results) {
        // 1. 按操作时间(inTime)升序排序，这是正确配对的关键
        groupRecords.sort(Comparator.comparing(CardboxResortVo::getInTime));

        // 2. 遍历排序后的记录，进行配对
        Iterator<CardboxResortVo> iterator = groupRecords.iterator();
        CardboxResortVo pendingResortRecord = null; // 用于暂存上一条求助记录

        while (iterator.hasNext()) {
            CardboxResortVo currentRecord = iterator.next();

            if (24 == currentRecord.getKeyType()) {
                // 遇到一条新的"求助"记录
                // 如果之前已经有一条未配对的求助，理论上说明它没有被取消，根据业务需求决定是否处理（这里先覆盖）
                pendingResortRecord = currentRecord;
            } else if (8 == currentRecord.getKeyType()) {
                // 遇到一条"取消"记录
                if (pendingResortRecord != null) {
                    // 如果前面有求助记录，则配对成功
                    CardboxResortVo result = createResultFromPair(pendingResortRecord, currentRecord);
                    results.add(result);
                    pendingResortRecord = null; // 配对成功，清空暂存，等待下一条求助
                }
                // 如果pendingResortRecord为null，说明这条取消记录前面没有对应的求助，可能是孤立的取消，忽略它
            }
        }
        // 循环结束后，如果pendingResortRecord不为null，说明有一条求助记录始终没有被取消，无法计算时长，将被忽略。
    }

    /**
     * 根据一对配对的求助和取消记录，创建结果对象
     */
    private CardboxResortVo createResultFromPair(CardboxResortVo resortRecord, CardboxResortVo cancelRecord) {
        CardboxResortVo result = new CardboxResortVo();

        // 设置基本信息
        result.setVehicleLicense(resortRecord.getVehicleLicense());
        result.setLaneId(resortRecord.getLaneId());
        result.setOperatorId(resortRecord.getOperatorId());
        result.setOperatorName(resortRecord.getOperatorName());
        result.setStationName(resortRecord.getStationName());
        result.setStationId(resortRecord.getStationId());

        // 设置时间
        result.setResortTime(resortRecord.getInTime());
        result.setOperateTime(cancelRecord.getInTime());

        // 计算时长（毫秒）
        long durationMillis = cancelRecord.getInTime().getTime() - resortRecord.getInTime().getTime();
        result.setDurationMills(durationMillis);

        // 格式化时长为 "x小时x分钟x秒"
        result.setDuration(formatDuration(durationMillis));

        return result;
    }

    /**
     * 将毫秒数格式化为易读的字符串
     */
    private String formatDuration(long totalMillis) {
        if (totalMillis < 0) {
            return "时间计算有误";
        }

        // 使用Java 8的Time API进行转换，更清晰[2,4](@ref)
        Duration duration = Duration.ofMillis(totalMillis);
        long hours = duration.toHours();
        int minutes = (int) ((duration.toMinutes()) % 60);
        int seconds = (int) ((duration.getSeconds()) % 60);

        if (hours > 0) {
            return String.format("%d小时%d分钟%d秒", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%d分钟%d秒", minutes, seconds);
        } else {
            return String.format("%d秒", seconds);
        }
    }
}
