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
import com.itssky.system.domain.vo.*;
import com.itssky.system.mapper.ExamineMapper;
import com.itssky.system.mapper.SpecialMapper;
import com.itssky.system.mapper.TbStationInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ExamineServiceImpl {

    private final ExamineMapper examineMapper;

    private final SpecialMapper specialMapper;

    private final TbStationInfoMapper tbStationInfoMapper;

    @DynamicTableName(dateParam = "#dto.beginTime")
    public List<FD06Vo> getFd06(FD06Dto dto) {
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<FD06Vo> list = examineMapper.getFd06(dto);
        if (!CollectionUtils.isEmpty(list)) {
            Set<String> operatorIdSet = list.stream().map(FD06Vo::getOperateId).collect(Collectors.toSet());
            Set<Integer> operatorIds = operatorIdSet.stream().map(Integer::parseInt).collect(Collectors.toSet());
            List<Map> map = specialMapper.buildOperatorName(operatorIds);
            Map<Integer, String> operatorMap = new HashMap<>();
            map.forEach(m -> operatorMap.put(Integer.valueOf(m.get("operatorId").toString()),
                    m.get("operatorName").toString()));
            list.forEach(item -> {
                if (Objects.nonNull(operatorMap.get(Integer.parseInt(item.getOperateId())))) {
                    item.setOperateName(operatorMap.get(Integer.parseInt(item.getOperateId())));
                }
            });
        }
        return list;
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
        dto.setTableName("exit" + DateUtil.format(dto.getBeginTime(), DatePattern.SIMPLE_MONTH_PATTERN));
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
            Double totalToll = list.stream().map(i -> BigDecimal.valueOf(i.getToll()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue();
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
        //判断用户的corpno
        if (dto.getStationId() == -1 && loginUser.getCorpNo().length() == 2) {
            LambdaQueryWrapper<TbStationInfo> tbStationInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tbStationInfoLambdaQueryWrapper.select(TbStationInfo::getStationname, TbStationInfo::getStationhex,
                    TbStationInfo::getStationid).likeRight(TbStationInfo::getCorpno, loginUser.getCorpNo());
            List<TbStationInfo> tbStationInfoList = tbStationInfoMapper.selectList(tbStationInfoLambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(tbStationInfoList)) {
                List<Integer> stationIdList = tbStationInfoList.stream().filter(i -> i.getStationid() != null)
                        .map(TbStationInfo::getStationid).collect(Collectors.toList());
                dto.setStationIdList(stationIdList);
            }
        } else {
            dto.setStationIdList(Collections.singletonList(dto.getStationId()));
        }
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setTableName("exit" + DateUtil.format(dto.getTime(), DatePattern.SIMPLE_MONTH_PATTERN));
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
            Double totalToll = list.stream().map(i -> BigDecimal.valueOf(i.getToll()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue();
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
        dto.setTableName("exit" + DateUtil.format(dto.getBeginTime(), DatePattern.SIMPLE_MONTH_PATTERN));
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
}
