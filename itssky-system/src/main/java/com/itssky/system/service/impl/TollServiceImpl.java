package com.itssky.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.itssky.common.annotation.DynamicTableName;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.system.domain.dto.*;
import com.itssky.system.domain.vo.*;
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
    public List<StationShiftVo> f1StationShift(StationTimeDto dto) {
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
    public List<F1StationShiftTollVo> getF1StationShiftToll(StationTimeDto dto) {
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
    public List<F2StationShiftTollVo> getF2StationShiftToll(StationTimeDto dto) {
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
    public List<StationShiftVo> f2StationShift(StationTimeDto dto) {
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
        StationTimeDto paramDto = new StationTimeDto();
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
        if (CollectionUtils.isEmpty(ePayTollStatVos)) {
            return new ArrayList<>();
        }
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
        //合计行
        EPayTollStatVo totalRow = buildEefTotalRow(ePayTollStatVos);
        ePayTollStatVos.add(totalRow);
        return ePayTollStatVos;
    }

    private EPayTollStatVo buildEefTotalRow(List<EPayTollStatVo> ePayTollStatVos) {
        EPayTollStatVo totalRow = new EPayTollStatVo();
        totalRow.setTotalRow(true);
        totalRow.setStatType("合计");
        totalRow.setCust1C(ePayTollStatVos.stream().map(EPayTollStatVo::getCust1C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust1D(ePayTollStatVos.stream().map(EPayTollStatVo::getCust1D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust1Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getCust1Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust2C(ePayTollStatVos.stream().map(EPayTollStatVo::getCust2C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust2D(ePayTollStatVos.stream().map(EPayTollStatVo::getCust2D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust2Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getCust2Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust3C(ePayTollStatVos.stream().map(EPayTollStatVo::getCust3C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust3D(ePayTollStatVos.stream().map(EPayTollStatVo::getCust3D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust3Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getCust3Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust4C(ePayTollStatVos.stream().map(EPayTollStatVo::getCust4C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust4D(ePayTollStatVos.stream().map(EPayTollStatVo::getCust4D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCust4Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getCust4Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCustCSubTotal(ePayTollStatVos.stream().map(EPayTollStatVo::getCustCSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCustDSubTotal(ePayTollStatVos.stream().map(EPayTollStatVo::getCustDSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCustSubSum(ePayTollStatVos.stream().map(EPayTollStatVo::getCustSubSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust1C(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust1C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust1D(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust1D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust1Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust1Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust2C(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust2C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust2D(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust2D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust2Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust2Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust3C(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust3C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust3D(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust3D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust3Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust3Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust4C(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust4C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust4D(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust4D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust4Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust4Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust5C(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust5C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust5D(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust5D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust5Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust5Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust6C(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust6C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust6D(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust6D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrust6Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getTrust6Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrustCSubTotal(ePayTollStatVos.stream().map(EPayTollStatVo::getTrustCSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrustDSubTotal(ePayTollStatVos.stream().map(EPayTollStatVo::getTrustDSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTrustSubSum(ePayTollStatVos.stream().map(EPayTollStatVo::getTrustSubSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec1C(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec1C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec1D(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec1D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec1Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec1Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec2C(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec2C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec2D(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec2D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec2Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec2Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec3C(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec3C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec3D(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec3D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec3Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec3Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec4C(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec4C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec4D(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec4D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec4Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec4Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec5C(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec5C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec5D(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec5D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec5Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec5Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec6C(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec6C)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec6D(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec6D)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpec6Sum(ePayTollStatVos.stream().map(EPayTollStatVo::getSpec6Sum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpecCSubTotal(ePayTollStatVos.stream().map(EPayTollStatVo::getSpecCSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpecDSubTotal(ePayTollStatVos.stream().map(EPayTollStatVo::getSpecDSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setSpecSubSum(ePayTollStatVos.stream().map(EPayTollStatVo::getSpecSubSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setCTotal(ePayTollStatVos.stream().map(EPayTollStatVo::getCTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setDTotal(ePayTollStatVos.stream().map(EPayTollStatVo::getDTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        totalRow.setTotalSum(ePayTollStatVos.stream().map(EPayTollStatVo::getTotalSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        return totalRow;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public List<F6TollVo> f6Toll(StationTimeDto dto) {
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
    public List<Cf1Vo> cf1Toll(StationTimeDto dto) {
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

    /**
     * 沿江总值
     */
    @Override
    public List<StationShiftVo> yjzz(FtStationDto dto) {
        List<StationShiftVo> localResult = ftToll(dto);
        if (!localResult.isEmpty()) {
            localResult.remove(localResult.size() - 1);
            return ftTollForOtherDatabase(localResult, dto);
        }
        return new ArrayList<>();
    }

    private List<StationShiftVo> ftTollForOtherDatabase(List<StationShiftVo> localResult, FtStationDto dto) {
        dto.setStationIdList(Arrays.asList(400002,400003,400004,400005,400006,400007));
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
        List<StationShiftVo> stationShiftVos = tollMapper.ftTollForOtherDatabase(dto);
        //计算统计金额及实缴金额
        //先查询交款记录表
        //构建会查询到的表集合
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbsh",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        List<TbShVo> tbShVoList = tollMapper.getTbShDataV2ForOtherDatabase(dto);
        //新加收表查询
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "extrapay",
                        DatePattern.NORM_YEAR_PATTERN)
        );
        StationTimeDto paramDto = new StationTimeDto();
        BeanUtils.copyProperties(dto,  paramDto);
        List<ExtraPayVo> extraPayList = tollMapper.getExtraPayForOtherDatabase(paramDto);
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
        if (!CollectionUtils.isEmpty(stationShiftVos)) {
            localResult.addAll(stationShiftVos);
        }
        //合计行
        StationShiftVo hjRow = buildTotalRowVo(localResult);
        hjRow.setTotalRow(true);
        hjRow.setStatType("合计");
        stationShiftVos.add(hjRow);
        return localResult;
    }

    /**
     * 集装箱、绿色通道、抗震救灾、运管苏通卡货车、军车、专用工作卡、收割机、应急、大件运输、合计
     */
    @Override
    public List<TollYhVo> yh(TollYhDto dto) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        int intBeginDate = Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN));
        int intEndDate = Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN));
        List<String> tableNameList = TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(),
                "tbstatexit", DatePattern.SIMPLE_MONTH_PATTERN);
        if (CollectionUtils.isEmpty(tableNameList)) {
            return new ArrayList<>();
        }
        dto.setTableNameList(tableNameList);
        dto.setIntBeginTime(intBeginDate);
        dto.setIntEndTime(intEndDate);
        List<TollYhVo> yhList = tollMapper.yh(dto);
        yhList.forEach(item -> {
            if ("1".equals(dto.getStatType())) {
//                item.setStatType(item.getOperatorName() + "(" + item.getOperatorId().substring(item.getOperatorId().length() - 3) + ")");
                item.setStatType(item.getOperatorId());
            } else if ("2".equals(dto.getStatType())) {
                item.setStatType(item.getStaDate());
            } else if ("3".equals(dto.getStatType())) {
                item.setStatType(item.getMonthDate());
            } else if ("4".equals(dto.getStatType())) {
                item.setStatType(item.getStationName());
            }
            //合计列设置
            item.setSumq(item.getJzxq().add(item.getLstdq()).add(item.getKzjzq()).add(item.getZygzkq()).add(item.getSgjq()).add(item.getYjq()).add(item.getDjysq()));
            item.setSumh(item.getJzxh().add(item.getLstdh()).add(item.getKzjzh()).add(item.getZygzkh()).add(item.getSgjh()).add(item.getYjh()).add(item.getDjysh()));
            item.setSumd(item.getJzxd().add(item.getLstdd()).add(item.getKzjzd()).add(item.getZygzkd()).add(item.getSgjd()).add(item.getYjd()).add(item.getDjysd()));
        });
        //添加合计行
        TollYhVo sumRow = new TollYhVo();
        sumRow.setTotalRow(true);
        sumRow.setStatType("合计");
        sumRow.setJzxq(yhList.stream().map(TollYhVo::getJzxq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setJzxh(yhList.stream().map(TollYhVo::getJzxh).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setJzxd(yhList.stream().map(TollYhVo::getJzxd).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setLstdq(yhList.stream().map(TollYhVo::getLstdq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setLstdh(yhList.stream().map(TollYhVo::getLstdh).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setLstdd(yhList.stream().map(TollYhVo::getLstdd).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setKzjzq(yhList.stream().map(TollYhVo::getKzjzq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setKzjzh(yhList.stream().map(TollYhVo::getKzjzh).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setKzjzd(yhList.stream().map(TollYhVo::getKzjzd).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setYgstkq(yhList.stream().map(TollYhVo::getYgstkq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setYgstkh(yhList.stream().map(TollYhVo::getYgstkh).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setYgstkd(yhList.stream().map(TollYhVo::getYgstkd).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setJcq(yhList.stream().map(TollYhVo::getJcq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setJch(yhList.stream().map(TollYhVo::getJch).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setJcd(yhList.stream().map(TollYhVo::getJcd).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setZygzkq(yhList.stream().map(TollYhVo::getZygzkq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setZygzkh(yhList.stream().map(TollYhVo::getZygzkh).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setZygzkd(yhList.stream().map(TollYhVo::getZygzkd).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setSgjq(yhList.stream().map(TollYhVo::getSgjq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setSgjh(yhList.stream().map(TollYhVo::getSgjh).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setSgjd(yhList.stream().map(TollYhVo::getSgjd).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setYjq(yhList.stream().map(TollYhVo::getYjq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setYjh(yhList.stream().map(TollYhVo::getYjh).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setYjd(yhList.stream().map(TollYhVo::getYjd).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setDjysq(yhList.stream().map(TollYhVo::getDjysq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setDjysh(yhList.stream().map(TollYhVo::getDjysh).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setDjysd(yhList.stream().map(TollYhVo::getDjysd).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setSumq(yhList.stream().map(TollYhVo::getSumq).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setSumh(yhList.stream().map(TollYhVo::getSumh).reduce(BigDecimal.ZERO, BigDecimal::add));
        sumRow.setSumd(yhList.stream().map(TollYhVo::getSumd).reduce(BigDecimal.ZERO, BigDecimal::add));
        yhList.add(sumRow);
        return yhList;
    }


    @Override
    public List<EuVo> eu(CommonReportDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        int intBeginDate = Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN));
        int intEndDate = Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN));
        List<String> tableNameList = TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(),
                "tbstatexit", DatePattern.SIMPLE_MONTH_PATTERN);
        if (CollectionUtils.isEmpty(tableNameList)) {
            return new ArrayList<>();
        }
        dto.setTableNameList(tableNameList);
        dto.setIntBeginTime(intBeginDate);
        dto.setIntEndTime(intEndDate);
        List<EuVo> eu = tollMapper.eu(dto);
        if (CollectionUtils.isEmpty(eu)) {
            return new ArrayList<>();
        }
        eu.forEach(item -> {
            if ("1".equals(dto.getStatType())) {
                item.setStatType(item.getStaDate());
            } else if ("2".equals(dto.getStatType())) {
                item.setStatType(item.getMonthDate());
            } else if ("3".equals(dto.getStatType())) {
                item.setStatType(item.getStationName());
            }
        });
        //添加合计行
        EuVo totalRow = new EuVo();
        totalRow.setTotalRow(true);
        totalRow.setStatType("合计");
        totalRow.setVc1(eu.stream().map(EuVo::getVc1).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVd1(eu.stream().map(EuVo::getVd1).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setV1(eu.stream().map(EuVo::getV1).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVc2(eu.stream().map(EuVo::getVc2).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVd2(eu.stream().map(EuVo::getVd2).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setV2(eu.stream().map(EuVo::getV2).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVc3(eu.stream().map(EuVo::getVc3).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVd3(eu.stream().map(EuVo::getVd3).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setV3(eu.stream().map(EuVo::getV3).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVc4(eu.stream().map(EuVo::getVc4).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVd4(eu.stream().map(EuVo::getVd4).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setV4(eu.stream().map(EuVo::getV4).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVc5(eu.stream().map(EuVo::getVc5).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVd5(eu.stream().map(EuVo::getVd5).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setV5(eu.stream().map(EuVo::getV5).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVc6(eu.stream().map(EuVo::getVc6).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVd6(eu.stream().map(EuVo::getVd6).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setV6(eu.stream().map(EuVo::getV6).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVcz(eu.stream().map(EuVo::getVcz).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVdz(eu.stream().map(EuVo::getVdz).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setVz(eu.stream().map(EuVo::getVz).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setSumc(eu.stream().map(EuVo::getSumc).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setSumd(eu.stream().map(EuVo::getSumd).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setSum(eu.stream().map(EuVo::getSum).reduce(BigDecimal.ZERO, BigDecimal::add));
        eu.add(totalRow);
        return eu;
    }

    @Override
    public List<MobVcVo> mobVc(CommonReportDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        int intBeginDate = Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN));
        int intEndDate = Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN));
        List<String> tableNameList = TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(),
                "tbstatexit", DatePattern.SIMPLE_MONTH_PATTERN);
        if (CollectionUtils.isEmpty(tableNameList)) {
            return new ArrayList<>();
        }
        dto.setTableNameList(tableNameList);
        dto.setIntBeginTime(intBeginDate);
        dto.setIntEndTime(intEndDate);
        List<MobVcVo> mobVcVos = tollMapper.mobVc(dto);
        if (CollectionUtils.isEmpty(mobVcVos)) {
            return new ArrayList<>();
        }
        mobVcVos.forEach(item -> {
            if ("1".equals(dto.getStatType())) {
                item.setStatType(item.getStaDate());
            } else if ("2".equals(dto.getStatType())) {
                item.setStatType(item.getMonthDate());
            } else if ("3".equals(dto.getStatType())) {
                item.setStatType(item.getStationName());
            }
        });
        //添加合计行
        MobVcVo totalRow = new MobVcVo();
        totalRow.setTotalRow(true);
        totalRow.setStatType("合计");
        totalRow.setK1f(mobVcVos.stream().map(i -> new BigDecimal(i.getK1f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK1t(mobVcVos.stream().map(MobVcVo::getK1t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setK2f(mobVcVos.stream().map(i -> new BigDecimal(i.getK2f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK2t(mobVcVos.stream().map(MobVcVo::getK2t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setK3f(mobVcVos.stream().map(i -> new BigDecimal(i.getK3f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK3t(mobVcVos.stream().map(MobVcVo::getK3t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setK4f(mobVcVos.stream().map(i -> new BigDecimal(i.getK4f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK4t(mobVcVos.stream().map(MobVcVo::getK4t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setKsumf(mobVcVos.stream().map(i -> new BigDecimal(i.getKsumf())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setKsumt(mobVcVos.stream().map(MobVcVo::getKsumt).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setH1f(mobVcVos.stream().map(i -> new BigDecimal(i.getH1f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH1t(mobVcVos.stream().map(MobVcVo::getH1t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setH2f(mobVcVos.stream().map(i -> new BigDecimal(i.getH2f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH2t(mobVcVos.stream().map(MobVcVo::getH2t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setH3f(mobVcVos.stream().map(i -> new BigDecimal(i.getH3f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH3t(mobVcVos.stream().map(MobVcVo::getH3t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setH4f(mobVcVos.stream().map(i -> new BigDecimal(i.getH4f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH4t(mobVcVos.stream().map(MobVcVo::getH4t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setHsumf(mobVcVos.stream().map(i -> new BigDecimal(i.getHsumf())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setHsumt(mobVcVos.stream().map(MobVcVo::getHsumt).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setZ1f(mobVcVos.stream().map(i -> new BigDecimal(i.getZ1f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ1t(mobVcVos.stream().map(MobVcVo::getZ1t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setZ2f(mobVcVos.stream().map(i -> new BigDecimal(i.getZ2f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ2t(mobVcVos.stream().map(MobVcVo::getZ2t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setZ3f(mobVcVos.stream().map(i -> new BigDecimal(i.getZ3f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ3t(mobVcVos.stream().map(MobVcVo::getZ3t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setZ4f(mobVcVos.stream().map(i -> new BigDecimal(i.getZ4f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ4t(mobVcVos.stream().map(MobVcVo::getZ4t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setZ5f(mobVcVos.stream().map(i -> new BigDecimal(i.getZ5f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ5t(mobVcVos.stream().map(MobVcVo::getZ5t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setZ6f(mobVcVos.stream().map(i -> new BigDecimal(i.getZ6f())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ6t(mobVcVos.stream().map(MobVcVo::getZ6t).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setZsumf(mobVcVos.stream().map(i -> new BigDecimal(i.getZsumf())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZsumt(mobVcVos.stream().map(MobVcVo::getZsumt).reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRow.setSumf(mobVcVos.stream().map(i -> new BigDecimal(i.getSumf())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setSumt(mobVcVos.stream().map(MobVcVo::getSumt).reduce(BigDecimal.ZERO, BigDecimal::add));
        mobVcVos.add(totalRow);
        return mobVcVos;
    }
}
