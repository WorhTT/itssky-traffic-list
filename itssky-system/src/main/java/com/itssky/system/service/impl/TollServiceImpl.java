package com.itssky.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itssky.common.annotation.DynamicTableName;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.system.domain.TbStationInfo;
import com.itssky.system.domain.vo.*;
import com.itssky.system.domain.dto.FtStationDto;
import com.itssky.system.domain.dto.StationShiftDto;
import com.itssky.system.domain.dto.VehicleClassStatDto;
import com.itssky.system.mapper.TbStationInfoMapper;
import com.itssky.system.mapper.TollMapper;
import com.itssky.system.service.ITollService;
import com.itssky.system.service.TbStationInfoService;
import com.itssky.util.TableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ITSSKY
 * 通行类报表实现类
 */
@Slf4j
@Service
public class TollServiceImpl implements ITollService {

    @Autowired
    private TollMapper tollMapper;

    @Autowired
    private TbStationInfoService tbStationInfoService;

    /**
     * F1收费站通行费收入班统计
     * 统计金额 = 应收款 + 电子支付 + 移动支付
     * 金额差异 = 实缴款 - 应收款
     * 免费IC卡含拥堵免费、生猪免费
     */
    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public List<StationShiftVo> f1StationShift(StationShiftDto dto) {
        int statDate = Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN));
        dto.setTimeFormat(statDate);
        dto.setStationIdList(Collections.singletonList(dto.getStationId()));
        //获取出口班次统计表数据
        List<StationShiftVo> stationShiftVos = tollMapper.f1StationShift(dto);
        //获取解款数据
        List<TbShVo> tbShData = tollMapper.getTbShData(dto);
        //获取加收记录
        //extrapay按年进行分表
        dto.setTableNameList(Collections.singletonList("extrapay" + DateUtil.format(dto.getTime(),
                DatePattern.NORM_YEAR_PATTERN)));
        dto.setStatisticsType("3");
        List<ExtraPayVo> extraPayList = tollMapper.getExtraPay(dto);
        //获取交款记录表数据
        Map<String, TbShVo> tbShMap = tbShData.stream().collect(Collectors.toMap(TbShVo::getOperatorId, i -> i));
        Map<String, ExtraPayVo> extraPayVoMap = extraPayList.stream().collect(Collectors.toMap(ExtraPayVo::getOperatorId, i -> i));
        stationShiftVos.forEach(i -> {
            //加收金额
            BigDecimal addedToll = new BigDecimal("0.00");
            //移动支付加收金额
            BigDecimal ydzfAddedToll = new BigDecimal("0.00");
            //实收金额
            BigDecimal handToll = new BigDecimal("0.00");
            //douTotalToll
            BigDecimal douTotalToll = i.getDouTotalToll();
            if (Objects.nonNull(tbShMap.get(i.getOperatorId()))) {
                TbShVo tbShVo = tbShMap.get(i.getOperatorId());
                handToll = tbShVo.getHandToll();
            }
            if (Objects.nonNull(extraPayVoMap.get(i.getOperatorId()))) {
                ExtraPayVo extraPayVo = extraPayVoMap.get(i.getOperatorId());
                ydzfAddedToll = extraPayVo.getYdzfMoney();
                addedToll =  extraPayVo.getExtAddToll();
            }
            //应缴金额计算
            BigDecimal yjje = douTotalToll.add(addedToll).subtract(ydzfAddedToll);
            //金额差异
            BigDecimal jecy = handToll.subtract(yjje);
            //统计金额 应缴金额(现金)+移动支付+电子支付+移动支付加收
            BigDecimal tjje = yjje.add(i.getMobilePaymentAmount()).add(i.getEPaymentAmount()).add(ydzfAddedToll);
            //实缴金额
            i.setPaidAmount(handToll);
            //应缴金额
            i.setDueAmount(yjje);
            //金额差异
            i.setAmountDiff(jecy);
            //统计金额
            i.setStatAmount(tjje);
            //加收款
            i.setExtraTotal(addedToll);
        });
        //添加合计行
        StationShiftVo totalRow = buildTotalRowVo(stationShiftVos);
        totalRow.setTotalRow(true);
        totalRow.setShiftId("合计");
        stationShiftVos.add(totalRow);
        return stationShiftVos;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public List<F1StationShiftTollVo> getF1StationShiftToll(StationShiftDto dto) {
        List<StationShiftVo> stationShiftVos = f1StationShift(dto);
        List<F1StationShiftTollVo> result = new ArrayList<>();
        stationShiftVos.forEach(i -> {
            F1StationShiftTollVo f1StationShiftTollVo = F1StationShiftTollVo.builder()
                    .shiftId(i.getShiftId())
                    .teamId(i.getTeamId())
                    .operatorId(i.getOperatorId())
                    .mobilePaymentAmount(i.getMobilePaymentAmount())
                    .ePaymentAmount(i.getEPaymentAmount())
                    .officialIcCardCount(i.getOfficialIcCardCount())
                    .militaryIcCardCount(i.getMilitaryIcCardCount())
                    .freeIcCardCount(i.getFreeIcCardCount())
                    .dueIcCardCount(i.getDueIcCardCount())
                    .statAmount(i.getStatAmount())
                    .dueAmount(i.getDueAmount())
                    .paidAmount(i.getPaidAmount())
                    .amountDiff(i.getAmountDiff())
                    .arrearsAmount(i.getArrearsAmount())
                    .extraTotal(i.getExtraTotal())
                    .build();
            result.add(f1StationShiftTollVo);
        });
        return result;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public List<F2StationShiftTollVo> getF2StationShiftToll(StationShiftDto dto) {
        List<StationShiftVo> stationShiftVos = f2StationShift(dto);
        List<F2StationShiftTollVo> result = new ArrayList<>();
        stationShiftVos.forEach(i -> {
            F2StationShiftTollVo f1StationShiftTollVo = F2StationShiftTollVo.builder()
                    .mobilePaymentAmount(i.getMobilePaymentAmount())
                    .ePaymentAmount(i.getEPaymentAmount())
                    .officialIcCardCount(i.getOfficialIcCardCount())
                    .militaryIcCardCount(i.getMilitaryIcCardCount())
                    .freeIcCardCount(i.getFreeIcCardCount())
                    .dueIcCardCount(i.getDueIcCardCount())
                    .statAmount(i.getStatAmount())
                    .dueAmount(i.getDueAmount())
                    .paidAmount(i.getPaidAmount())
                    .amountDiff(i.getAmountDiff())
                    .arrearsAmount(i.getArrearsAmount())
                    .extraTotal(i.getExtraTotal())
                    .build();
            if (i.isSubTotalRow()) {
                f1StationShiftTollVo.setShiftId("小计");
                f1StationShiftTollVo.setOperatorId("");
            } else if (i.isTotalRow()) {
                f1StationShiftTollVo.setShiftId("合计");
                f1StationShiftTollVo.setOperatorId("");
            } else {
                f1StationShiftTollVo.setShiftId(i.getShiftId());
                f1StationShiftTollVo.setOperatorId(i.getOperatorId());
            }
            result.add(f1StationShiftTollVo);
        });
        return result;
    }

    /**
     * F2收费站通行费收入日统计
     * 统计金额 = 应收款 + 电子支付 + 移动支付
     * 金额差异 = 实缴款 - 应收款
     * 免费IC卡含拥堵免费、生猪免费
     */
    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public List<StationShiftVo> f2StationShift(StationShiftDto dto) {
        //判断用户的corpno
        dto.setStationIdList(Collections.singletonList(dto.getStationId()));
        int dateFormat = Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN));
        dto.setTimeFormat(dateFormat);
        List<StationShiftVo> stationShiftVos = tollMapper.f2StationShift(dto);
        List<TbShVo> tbShData = tollMapper.getTbShData(dto);
        //获取加收记录
        dto.setTableNameList(Collections.singletonList("extrapay" + DateUtil.format(dto.getTime(),
                DatePattern.NORM_YEAR_PATTERN)));
        dto.setStatisticsType("3");
        List<ExtraPayVo> extraPayList = tollMapper.getExtraPay(dto);
        Map<String, TbShVo> tbShMap = tbShData.stream().collect(Collectors.toMap(TbShVo::getOperatorId, i -> i));
        Map<String, ExtraPayVo> extraPayVoMap = extraPayList.stream().collect(Collectors.toMap(ExtraPayVo::getOperatorId, i -> i));
        stationShiftVos.forEach(i -> {
            //加收金额
            BigDecimal addedToll = new BigDecimal("0.00");
            //移动支付加收金额
            BigDecimal ydzfAddedToll = new BigDecimal("0.00");
            //实收金额
            BigDecimal handToll = new BigDecimal("0.00");
            //douTotalToll
            BigDecimal douTotalToll = i.getDouTotalToll();
            if (Objects.nonNull(tbShMap.get(i.getOperatorId()))) {
                TbShVo tbShVo = tbShMap.get(i.getOperatorId());
                handToll = tbShVo.getHandToll();
            }
            if (Objects.nonNull(extraPayVoMap.get(i.getOperatorId()))) {
                ExtraPayVo extraPayVo = extraPayVoMap.get(i.getOperatorId());
                ydzfAddedToll = extraPayVo.getYdzfMoney();
                addedToll =  extraPayVo.getExtAddToll();
            }
            //应缴金额计算
            BigDecimal yjje = douTotalToll.add(addedToll).subtract(ydzfAddedToll);
            //金额差异
            BigDecimal jecy = handToll.subtract(yjje);
            //统计金额 应缴金额(现金)+移动支付+电子支付+移动支付加收
            BigDecimal tjje = yjje.add(i.getMobilePaymentAmount()).add(i.getEPaymentAmount()).add(ydzfAddedToll);
            //实缴金额
            i.setPaidAmount(handToll);
            //应缴金额
            i.setDueAmount(yjje);
            //金额差异
            i.setAmountDiff(jecy);
            //统计金额
            i.setStatAmount(tjje);
            //加收款
            i.setExtraTotal(addedToll);
        });
        //给列表增加小计行和合计行
        return buildTotalRow(stationShiftVos);
    }

    private LinkedList<StationShiftVo> buildTotalRow(List<StationShiftVo> list) {
        Map<String, List<StationShiftVo>> listMap = list.stream()
                .sorted(Comparator.comparing(StationShiftVo::getShiftId))
                .collect(Collectors.groupingBy(StationShiftVo::getShiftId));
        LinkedList<StationShiftVo> result = new LinkedList<>();
        //小计
        listMap.forEach((k, v) -> {
            StationShiftVo stationShiftVo = buildTotalRowVo(v);
            stationShiftVo.setSubTotalRow(true);
            result.addAll(v);
            result.add(stationShiftVo);
        });
        //合计
        List<StationShiftVo> subTotalRows = result.stream()
                .filter(StationShiftVo::isSubTotalRow).collect(Collectors.toList());
        StationShiftVo totalRow = buildTotalRowVo(subTotalRows);
        totalRow.setTotalRow(true);
        result.add(totalRow);
        return result;
    }

    private StationShiftVo buildTotalRowVo(List<StationShiftVo> list) {
        StationShiftVo subTotalRow = new StationShiftVo();
        //统计金额
        subTotalRow.setStatAmount(list.stream().map(StationShiftVo::getStatAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //应缴金额
        subTotalRow.setDueAmount(list.stream().map(StationShiftVo::getDueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //实缴金额
        subTotalRow.setPaidAmount(list.stream().map(StationShiftVo::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //金额差异
        subTotalRow.setAmountDiff(list.stream().map(StationShiftVo::getAmountDiff)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //欠款车次
        subTotalRow.setArrearsTrips(list.stream().map(i -> new BigDecimal(i.getArrearsTrips()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //欠款金额
        subTotalRow.setArrearsAmount(list.stream().map(StationShiftVo::getArrearsAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //加收款
        subTotalRow.setExtraTotal(list.stream().map(StationShiftVo::getExtraTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //移动支付
        subTotalRow.setMobilePaymentAmount(list.stream().map(StationShiftVo::getMobilePaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //电子支付
        subTotalRow.setEPaymentAmount(list.stream().map(StationShiftVo::getEPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //公务IC
        subTotalRow.setOfficialIcCardCount(list.stream().map(i -> new BigDecimal(i.getOfficialIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //军车IC
        subTotalRow.setMilitaryIcCardCount(list.stream().map(i -> new BigDecimal(i.getMilitaryIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //免费IC
        subTotalRow.setFreeIcCardCount(list.stream().map(i -> new BigDecimal(i.getFreeIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //应缴IC
        subTotalRow.setDueIcCardCount(list.stream().map(i -> new BigDecimal(i.getDueIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        return subTotalRow;
    }

    /**
     * FT通行费收入统计表
     */
    @Override
    public List<StationShiftVo> ftToll(FtStationDto dto) {
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
        List<StationShiftVo> stationShiftVos = tollMapper.ftToll(dto);
        //计算统计金额及实缴金额
        //先查询交款记录表
        //构建会查询到的表集合
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbsh",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        List<TbShVo> tbShVoList = tollMapper.getTbShDataV2(dto);
        //新加收表查询
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "extrapay",
                        DatePattern.NORM_YEAR_PATTERN)
        );
        StationShiftDto  paramDto = new StationShiftDto();
        BeanUtils.copyProperties(dto,  paramDto);
        List<ExtraPayVo> extraPayList = tollMapper.getExtraPay(paramDto);
        //日
        Map<String, TbShVo> tbshMap = new HashMap<>();
        Map<String, ExtraPayVo> extrapayMap = new HashMap<>();
        if ("0".equals(dto.getStatisticsType())) {
            tbshMap = tbShVoList.stream()
                    .collect(Collectors.toMap(TbShVo::getStaDate, i -> i));
            extrapayMap = extraPayList.stream()
                    .collect(Collectors.toMap(ExtraPayVo::getStaDate, i -> i));
        }
        //月
        else if ("1".equals(dto.getStatisticsType())) {
            tbshMap = tbShVoList.stream()
                    .collect(Collectors.toMap(TbShVo::getMonthDate, i -> i));
            extrapayMap = extraPayList.stream()
                    .collect(Collectors.toMap(ExtraPayVo::getMonthDate, i -> i));
        }
        //站
        else if ("2".equals(dto.getStatisticsType())) {
            tbshMap = tbShVoList.stream()
                    .collect(Collectors.toMap(TbShVo::getStationId, i -> i));
            extrapayMap = extraPayList.stream()
                    .collect(Collectors.toMap(ExtraPayVo::getStationId, i -> i));
        }
        //计算金额差异和统计金额和应缴金额
        for (StationShiftVo item : stationShiftVos) {
            //加收金额
            BigDecimal addedToll = new BigDecimal("0.00");
            //移动支付加收金额
            BigDecimal ydzfAddedToll = new BigDecimal("0.00");
            //实收金额
            BigDecimal handToll = new BigDecimal("0.00");
            //douTotalToll
            BigDecimal douTotalToll = item.getDouTotalToll();
            switch (dto.getStatisticsType()) {
                case "0":
                    item.setStatType(item.getStaDate());
                    if (Objects.nonNull(tbshMap.get(item.getStaDate()))) {
                        TbShVo tbShVo = tbshMap.get(item.getStaDate());
                        handToll = tbShVo.getHandToll();
                    }
                    if (Objects.nonNull(extrapayMap.get(item.getStaDate()))) {
                        ExtraPayVo extraPayVo = extrapayMap.get(item.getStaDate());
                        ydzfAddedToll = extraPayVo.getYdzfMoney();
                        addedToll =  extraPayVo.getExtAddToll();
                    }
                    break;
                case "1":
                    item.setStatType(item.getMonthDate());
                    if (Objects.nonNull(tbshMap.get(item.getMonthDate()))) {
                        TbShVo tbShVo = tbshMap.get(item.getMonthDate());
                        handToll = tbShVo.getHandToll();
                    }
                    if (Objects.nonNull(extrapayMap.get(item.getMonthDate()))) {
                        ExtraPayVo extraPayVo = extrapayMap.get(item.getMonthDate());
                        ydzfAddedToll = extraPayVo.getYdzfMoney();
                        addedToll =  extraPayVo.getExtAddToll();
                    }
                    break;
                case "2":
                    item.setStatType(item.getStationName());
                    if (Objects.nonNull(tbshMap.get(item.getStationId()))) {
                        TbShVo tbShVo = tbshMap.get(item.getStationId());
                        handToll = tbShVo.getHandToll();
                    }
                    if (Objects.nonNull(extrapayMap.get(item.getStationId()))) {
                        ExtraPayVo extraPayVo = extrapayMap.get(item.getStationId());
                        ydzfAddedToll = extraPayVo.getYdzfMoney();
                        addedToll =  extraPayVo.getExtAddToll();
                    }
                    break;
            }
            //应缴金额计算
            BigDecimal yjje = douTotalToll.add(addedToll).subtract(ydzfAddedToll);
            //金额差异
            BigDecimal jecy = handToll.subtract(yjje);
            //统计金额 应缴金额(现金)+移动支付+电子支付+移动支付加收
            BigDecimal tjje = yjje.add(item.getMobilePaymentAmount()).add(item.getEPaymentAmount()).add(ydzfAddedToll);
            //实缴金额
            item.setPaidAmount(handToll);
            //应缴金额
            item.setDueAmount(yjje);
            //金额差异
            item.setAmountDiff(jecy);
            //统计金额
            item.setStatAmount(tjje);
            //加收款
            item.setExtraTotal(addedToll);
        }
        //合计行
        StationShiftVo hjRow = buildTotalRowVo(stationShiftVos);
        hjRow.setTotalRow(true);
        hjRow.setStatType("合计");
        stationShiftVos.add(hjRow);
        return stationShiftVos;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.beginTime")
    public List<FtTollVo> getFtToll(FtStationDto dto) {
        List<StationShiftVo> stationShiftVos = ftToll(dto);
        List<FtTollVo> result = new ArrayList<>();
        stationShiftVos.forEach(i -> {
            FtTollVo f1StationShiftTollVo = FtTollVo.builder()
                    .statType(i.getStatType())
                    .mobilePaymentAmount(i.getMobilePaymentAmount())
                    .ePaymentAmount(i.getEPaymentAmount())
                    .officialIcCardCount(i.getOfficialIcCardCount())
                    .militaryIcCardCount(i.getMilitaryIcCardCount())
                    .freeIcCardCount(i.getFreeIcCardCount())
                    .dueIcCardCount(i.getDueIcCardCount())
                    .statAmount(i.getStatAmount())
                    .dueAmount(i.getDueAmount())
                    .paidAmount(i.getPaidAmount())
                    .amountDiff(i.getAmountDiff())
                    .arrearsAmount(i.getArrearsAmount())
                    .extraTotal(i.getExtraTotal())
                    .build();
            result.add(f1StationShiftTollVo);
        });
        return result;
    }

    /**
     * AFV综合按车型统计表
     */
    public List<VehicleClassStatVo> afvGeneral(VehicleClassStatDto dto) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //判断用户的corpno
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
        //获取afv综合车型统计表数据
        List<VehicleClassStatVo> vehicleClassStatVos = tollMapper.afvGeneral(dto);
        //计算加收款
        //先查询交款记录表
        //构建会查询到的表集合
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbsh",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        FtStationDto paramDto = new FtStationDto();
        BeanUtils.copyProperties(dto, paramDto);
        List<TbShVo> tbShVoList = tollMapper.getTbShDataV2(paramDto);

        Map<String, TbShVo>  tbshMap = new HashMap<>();
        //日
        if ("0".equals(dto.getStatisticsType())) {
            tbshMap = tbShVoList.stream().collect(Collectors.toMap(TbShVo::getStaDate, i -> i));
        }
        //月
        else if ("1".equals(dto.getStatisticsType())) {
            tbshMap = tbShVoList.stream().collect(Collectors.toMap(TbShVo::getMonthDate, i -> i));
        }
        //站
        else if ("2".equals(dto.getStatisticsType())) {
            tbshMap = tbShVoList.stream().collect(Collectors.toMap(TbShVo::getStationId, i -> i));
        }
        //人员
        else if ("3".equals(dto.getStatisticsType())) {
            tbshMap = tbShVoList.stream().collect(Collectors.toMap(TbShVo::getOperatorId, i -> i));
        }

        for (VehicleClassStatVo item : vehicleClassStatVos) {
            //加收金额
            BigDecimal addedToll = BigDecimal.ZERO;
            switch (dto.getStatisticsType()) {
                //日
                case "0":
                    item.setStatType(item.getStaDate());
                    if (Objects.nonNull(tbshMap.get(item.getStaDate()))) {
                        addedToll = tbshMap.get(item.getStaDate()).getAddedToll();
                    }
                    break;
                //月
                case "1":
                    item.setStatType(item.getMonthDate());
                    if (Objects.nonNull(tbshMap.get(item.getMonthDate()))) {
                        addedToll = tbshMap.get(item.getMonthDate()).getAddedToll();
                    }
                    break;
                //站
                case "2":
                    item.setStatType(item.getStationName());
                    if (Objects.nonNull(tbshMap.get(item.getStationId()))) {
                        addedToll = tbshMap.get(item.getStationId()).getAddedToll();
                    }
                    break;
                //人
                case "3":
                    item.setStatType(item.getOperatorId());
                    if (Objects.nonNull(tbshMap.get(item.getOperatorId()))) {
                        addedToll = tbshMap.get(item.getOperatorId()).getAddedToll();
                    }
                    break;
            }
            //设置加收金额
            item.setAddedAmount(addedToll);
            //计算合计
            BigDecimal totalToll = BigDecimal.ZERO;
            totalToll = totalToll.add(item.getCustSubTotal())
                    .add(item.getTruckSubTotal())
                    .add(item.getSpecSubTotal())
                    .add(item.getAddedAmount());
            item.setTotalAmount(totalToll);
        }
        //合计
        VehicleClassStatVo totalRow = buildAfvTotalRow(vehicleClassStatVos);
        vehicleClassStatVos.add(totalRow);
        return vehicleClassStatVos;
    }

    private VehicleClassStatVo buildAfvTotalRow(List<VehicleClassStatVo> list) {
        VehicleClassStatVo vehicleClassStatVo = new VehicleClassStatVo();
        vehicleClassStatVo.setStatType("合计");
        vehicleClassStatVo.setTotalRow(true);
        vehicleClassStatVo.setCust1(list.stream().map(VehicleClassStatVo::getCust1)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setCust2(list.stream().map(VehicleClassStatVo::getCust2)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setCust3(list.stream().map(VehicleClassStatVo::getCust3)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setCust4(list.stream().map(VehicleClassStatVo::getCust4)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setCustSubTotal(list.stream().map(VehicleClassStatVo::getCustSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setTruck1(list.stream().map(VehicleClassStatVo::getTruck1)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setTruck2(list.stream().map(VehicleClassStatVo::getTruck2)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setTruck3(list.stream().map(VehicleClassStatVo::getTruck3)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setTruck4(list.stream().map(VehicleClassStatVo::getTruck4)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setTruck5(list.stream().map(VehicleClassStatVo::getTruck5)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setTruck6(list.stream().map(VehicleClassStatVo::getTruck6)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setTruckSubTotal(list.stream().map(VehicleClassStatVo::getTruckSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setSpec1(list.stream().map(VehicleClassStatVo::getSpec1)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setSpec2(list.stream().map(VehicleClassStatVo::getSpec2)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setSpec3(list.stream().map(VehicleClassStatVo::getSpec3)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setSpec4(list.stream().map(VehicleClassStatVo::getSpec4)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setSpec5(list.stream().map(VehicleClassStatVo::getSpec5)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setSpec6(list.stream().map(VehicleClassStatVo::getSpec6)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setSpecSubTotal(list.stream().map(VehicleClassStatVo::getSpecSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setAddedAmount(list.stream().map(VehicleClassStatVo::getAddedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        vehicleClassStatVo.setTotalAmount(list.stream().map(VehicleClassStatVo::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return vehicleClassStatVo;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.beginTime")
    public List<AfvVehicleVo> getAfvGeneral(VehicleClassStatDto dto) {
        List<VehicleClassStatVo> vehicleClassStatVos = afvGeneral(dto);
        if (CollectionUtils.isEmpty(vehicleClassStatVos)) {
            return new ArrayList<>();
        }
        List<AfvVehicleVo> result = new ArrayList<>();
        vehicleClassStatVos.forEach(i -> {
            AfvVehicleVo afvVehicleVo = new AfvVehicleVo();
            BeanUtils.copyProperties(i, afvVehicleVo);
            result.add(afvVehicleVo);
        });
        return result;
    }

    /**
     * EEF电子支付通行费(MTC+ETC)统计表
     *
     * @param dto
     * @return
     */
    @Override
    public List<EPayTollStatVo> eefEPay(VehicleClassStatDto dto) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //判断用户的corpno
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
        //获取内容
        List<EPayTollStatVo> ePayTollStatVos = tollMapper.eefEPay(dto);
        ePayTollStatVos.forEach(i -> {
            if ("0".equals(dto.getStatisticsType())) {
                i.setStatType(i.getStaDate());
            } else if ("1".equals(dto.getStatisticsType())) {
                i.setStatType(i.getMonthDate());
            } else if ("2".equals(dto.getStatisticsType())) {
                i.setStatType(i.getStationName());
            } else if ("3".equals(dto.getStatisticsType())) {
                i.setStatType(i.getOperatorId());
            }
        });
        return ePayTollStatVos;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public List<F6TollVo> f6Toll(StationShiftDto dto) {
        dto.setTimeFormat(Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN)));
        List<F6TollVo> result = new ArrayList<>();
        List<F6TollVo> entryList = tollMapper.getF6TollEntry(dto);
        if (!CollectionUtils.isEmpty(entryList)) {
            result.addAll(entryList);
        }
        List<F6TollVo> exitList = tollMapper.getF6TollExit(dto);
        if (!CollectionUtils.isEmpty(exitList)) {
            result.addAll(exitList);
        }
        dto.setTableName("extrapay" + DateUtil.format(dto.getTime(), DatePattern.NORM_YEAR_PATTERN));
        List<F6TollVo> extraList = tollMapper.getF6TollExtra(dto);
        if (!CollectionUtils.isEmpty(extraList)) {
            result.addAll(extraList);
        }

        Map<String, F6TollVo> map = result.stream()
                .collect(Collectors.groupingBy(F6TollVo::getOperatorId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    F6TollVo summary = new F6TollVo();
                                    // 设置操作员信息
                                    summary.setTeamId(list.get(0).getTeamId());
                                    summary.setOperatorId(list.get(0).getOperatorId());
                                    summary.setOperatorName(list.get(0).getOperatorName());
                                    // 初始化金额字段为 BigDecimal.ZERO
                                    BigDecimal tollSum = BigDecimal.ZERO;
                                    // 初始化数量字段为 0
                                    int yjIcCardSum = 0;
                                    int paperNumSum = 0;
                                    int yfIcCardSum = 0;
                                    // 遍历列表进行累加
                                    for (F6TollVo item : list) {
                                        // 计算当前项目的 toll（exitToll + addedToll）
                                        BigDecimal exitToll = item.getExitToll() != null ? item.getExitToll() : BigDecimal.ZERO;
                                        BigDecimal addedToll = item.getAddedToll() != null ? item.getAddedToll() : BigDecimal.ZERO;
                                        BigDecimal itemToll = exitToll.add(addedToll);
                                        tollSum = tollSum.add(itemToll);

                                        // 累加其他 int 类型的字段
                                        yjIcCardSum += item.getYjIcCardNum();
                                        paperNumSum += item.getPaperNum();
                                        yfIcCardSum += item.getYfIcCardNum();
                                    }
                                    // 设置累加结果到 summary 对象
                                    summary.setToll(tollSum);
                                    summary.setYjIcCardNum(yjIcCardSum);
                                    summary.setPaperNum(paperNumSum);
                                    summary.setYfIcCardNum(yfIcCardSum);
                                    return summary;
                                }
                        )
                ));
        List<F6TollVo> realResult = new ArrayList<>();
        map.forEach((s, item) -> realResult.add(item));
        realResult.forEach(item -> item.setToll(item.getToll()));

        //增加合计行
        F6TollVo totalRow = new F6TollVo();
        totalRow.setTotalRow(true);
        totalRow.setTeamId("合计");
        totalRow.setToll(realResult.stream().map(F6TollVo::getToll)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setPaperNum(realResult.stream().map(i -> BigDecimal.valueOf(i.getPaperNum()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setYjIcCardNum(realResult.stream().map(i -> BigDecimal.valueOf(i.getYjIcCardNum()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setYfIcCardNum(realResult.stream().map(i -> BigDecimal.valueOf(i.getYfIcCardNum()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        realResult.add(totalRow);
        return realResult.stream()
                .sorted(Comparator.comparing(
                        F6TollVo::getOperatorId,
                        Comparator.nullsLast(
                                Comparator.comparing(
                                        str -> str == null || str.isEmpty() ? "\uffff" : str
                                )
                        )
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cf1Vo> cf1Toll(StationShiftDto dto) {
        //构建收费站列表参数
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        dto.setTimeFormat(Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN)));
        //下班解款数据 获取字段addedtoll,handtoll
        Map<String, TbShVo> tbShMap = new HashMap<>();
        dto.setTableName("tbsh" + DateUtil.format(dto.getTime(), DatePattern.SIMPLE_MONTH_PATTERN));
        List<TbShVo> tbShList = tollMapper.getTbShGroupByStation(dto);
        if (!CollectionUtils.isEmpty(tbShList)) {
            tbShMap = tbShList.stream().collect(Collectors.toMap(TbShVo::getStationId, i -> i, (m,n)->m));
        }
        //新款加收 获取字段extAddToll
        dto.setTableNameList(Collections.singletonList("extrapay" + DateUtil.format(dto.getTime(), DatePattern.NORM_YEAR_PATTERN)));
        dto.setStatisticsType("2");
        Map<String, ExtraPayVo> extraPayMap = new HashMap<>();
        List<ExtraPayVo> extraPayList = tollMapper.getExtraPay(dto);
        if (!CollectionUtils.isEmpty(extraPayList)) {
            extraPayMap = extraPayList.stream().collect(Collectors.toMap(ExtraPayVo::getStationId, i -> i, (m,n)->m));
        }
        //出口表数据
        dto.setTableName("tbstatexit" + DateUtil.format(dto.getTime(), DatePattern.SIMPLE_MONTH_PATTERN));
        List<Cf1Vo> tbstatExitList = tollMapper.getCf1Vo(dto);
        if (CollectionUtils.isEmpty(tbstatExitList)) {
            return new ArrayList<>();
        } else {
            for (Cf1Vo item : tbstatExitList) {
//                SET @doutotaltolls = @doutotaltoll + @doutotaltoll1 + @addedtoll;
//                SET @alltotalfee = @doutotaltolls + @eamoney+@ebmoney+@eamoney1+@ebmoney1+@mobmoney;-- 总金额
//                SET @difffee = @handtoll-@doutotaltolls;-- 金额差异
                //实收金额
                BigDecimal handToll = BigDecimal.ZERO;
                //移动支付加收
                BigDecimal ydzfMoney = BigDecimal.ZERO;
                //加收金额
                BigDecimal addedToll = BigDecimal.ZERO;
                //应缴金额
                BigDecimal yjje = BigDecimal.ZERO;
                //统计金额(总金额)
                BigDecimal tjje = BigDecimal.ZERO;
                //金额差异
                BigDecimal jecy = BigDecimal.ZERO;
                if (Objects.nonNull(tbShMap.get(item.getStationId()))) {
                    TbShVo tbShVo = tbShMap.get(item.getStationId());
                    handToll = tbShVo.getHandToll();
                }
                if (Objects.nonNull(extraPayMap.get(item.getStationId()))) {
                    ExtraPayVo extraPayVo = extraPayMap.get(item.getStationId());
                    ydzfMoney = extraPayVo.getYdzfMoney();
                    addedToll = extraPayVo.getExtAddToll();
                }
                //应缴金额
                yjje = yjje.add(item.getDouTotalToll()).add(addedToll).subtract(ydzfMoney);
                //统计金额
                tjje = tjje.add(yjje).add(item.getEPaymentAmount()).add(item.getMobilePaymentAmount()).add(ydzfMoney);
                //金额差异
                jecy = jecy.add(handToll).subtract(jecy);

                //统计金额
                item.setStatAmount(tjje);
                //应缴金额
                item.setDueAmount(yjje);
                //实缴金额
                item.setPaidAmount(handToll);
                //金额差异
                item.setAmountDiff(jecy);
                //加收款
                item.setExtraTotal(addedToll);
            }
        }
        //构建合计行
        Cf1Vo totalRow = new Cf1Vo();
        totalRow.setTotalRow(true);
        totalRow.setStationName("合计");
        //统计金额
        totalRow.setStatAmount(tbstatExitList.stream().map(Cf1Vo::getStatAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //应缴金额
        totalRow.setDueAmount(tbstatExitList.stream().map(Cf1Vo::getDueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //实缴金额
        totalRow.setPaidAmount(tbstatExitList.stream().map(Cf1Vo::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //金额差异
        totalRow.setAmountDiff(tbstatExitList.stream().map(Cf1Vo::getAmountDiff)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //欠款
        totalRow.setArrearsAmount(tbstatExitList.stream().map(Cf1Vo::getArrearsAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //加收款
        totalRow.setExtraTotal(tbstatExitList.stream().map(Cf1Vo::getExtraTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //移动支付
        totalRow.setMobilePaymentAmount(tbstatExitList.stream().map(Cf1Vo::getMobilePaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //电子支付
        totalRow.setEPaymentAmount(tbstatExitList.stream().map(Cf1Vo::getEPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        //公务IC卡
        totalRow.setOfficialIcCardCount(tbstatExitList.stream().map(i -> BigDecimal.valueOf(i.getOfficialIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //军车IC卡
        totalRow.setMilitaryIcCardCount(tbstatExitList.stream().map(i -> BigDecimal.valueOf(i.getMilitaryIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //免费IC卡
        totalRow.setFreeIcCardCount(tbstatExitList.stream().map(i -> BigDecimal.valueOf(i.getFreeIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //应缴IC卡
        totalRow.setDueIcCardCount(tbstatExitList.stream().map(i -> BigDecimal.valueOf(i.getDueIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        tbstatExitList.add(totalRow);
        return tbstatExitList;
    }

    @Override
    public List<MOBTollVo> mobToll(FtStationDto dto) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        //构建会查询到的表集合
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbstatexit",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        if (CollectionUtils.isEmpty(dto.getTableNameList())) {
            return new ArrayList<>();
        }
        List<MOBTollVo> list = tollMapper.getMOBToll(dto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        list.forEach(i -> {
            if ("0".equals(dto.getStatisticsType())) {
                i.setStatType(i.getStaDate());
            } else if ("1".equals(dto.getStatisticsType())) {
                i.setStatType(i.getMonthDate());
            } else if ("2".equals(dto.getStatisticsType())) {
                i.setStatType(i.getStationName());
            } else if ("3".equals(dto.getStatisticsType())) {
                i.setStatType(i.getOperatorId());
            }
        });
        //构建合计行
        MOBTollVo totalRow = new MOBTollVo();
        totalRow.setTotalRow(true);
        totalRow.setStatType("合计");
        totalRow.setYlCount(list.stream()
                .map(i -> new BigDecimal(i.getYlCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        totalRow.setYlToll(list.stream()
                .map(i -> new BigDecimal(i.getYlToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        totalRow.setWxCount(list.stream()
                .map(i -> new BigDecimal(i.getWxCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        totalRow.setWxToll(list.stream()
                .map(i -> new BigDecimal(i.getWxToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        totalRow.setZfbCount(list.stream()
                .map(i -> new BigDecimal(i.getZfbCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        totalRow.setZfbToll(list.stream()
                .map(i -> new BigDecimal(i.getZfbToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        totalRow.setBdCount(list.stream()
                .map(i -> new BigDecimal(i.getBdCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        totalRow.setBdToll(list.stream()
                .map(i -> new BigDecimal(i.getBdToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        totalRow.setJdCount(list.stream()
                .map(i -> new BigDecimal(i.getJdCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        totalRow.setJdToll(list.stream()
                .map(i -> new BigDecimal(i.getJdToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        totalRow.setTxbCount(list.stream()
                .map(i -> new BigDecimal(i.getTxbCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        totalRow.setTxbToll(list.stream()
                .map(i -> new BigDecimal(i.getTxbToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        totalRow.setSzrmbCount(list.stream()
                .map(i -> new BigDecimal(i.getSzrmbCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        totalRow.setSzrmbToll(list.stream()
                .map(i -> new BigDecimal(i.getSzrmbToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        totalRow.setQtCount(list.stream()
                .map(i -> new BigDecimal(i.getQtCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        totalRow.setQtToll(list.stream()
                .map(i -> new BigDecimal(i.getQtToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        totalRow.setHjCount(list.stream()
                .map(i -> new BigDecimal(i.getHjCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        totalRow.setHjToll(list.stream()
                .map(i -> new BigDecimal(i.getHjToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        list.add(totalRow);
        return list;
    }
}
