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
import com.itssky.util.TableUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
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
    private TbStationInfoMapper tbStationInfoMapper;

    /**
     * F1收费站通行费收入班统计
     * 统计金额 = 应收款 + 电子支付 + 移动支付
     * 金额差异 = 实缴款 - 应收款
     * 免费IC卡含拥堵免费、生猪免费
     */
    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public List<StationShiftVo> f1StationShift(StationShiftDto dto) {
        dto.setTimeFormat(Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN)));
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
        //获取出口班次统计表数据
        List<StationShiftVo> stationShiftVos = tollMapper.f1StationShift(dto);
        //获取解款数据
        List<TbShVo> tbShData = tollMapper.getTbShData(dto);
        //获取交款记录表数据
        Map<Integer, TbShVo> tbShMap = tbShData.stream().collect(Collectors.toMap(TbShVo::getOperatorId, i -> i));
        stationShiftVos.forEach(i -> {
            if (Objects.nonNull(tbShMap.get(i.getOperatorId()))) {
                TbShVo tbShVo = tbShMap.get(i.getOperatorId());
                //实缴金额
                double handToll = new BigDecimal(tbShVo.getHandToll().toString()).doubleValue();
                i.setPaidAmount(handToll);
                //加收款
                double addedToll = new BigDecimal(tbShVo.getAddedToll().toString()).doubleValue();
                i.setExtraTotal(addedToll);
//                //应缴IC卡
//                int yj = new BigDecimal(tbShVo.getHandOutCNum().toString()).intValue();
//                i.setDueIcCardCount(yj);
            }
//            //计算统计金额
//            BigDecimal bigDecimal = new BigDecimal("0");
//            bigDecimal = bigDecimal.add(BigDecimal.valueOf(Objects.nonNull(i.getDueAmount()) ? i.getDueAmount() : 0));
//            bigDecimal = bigDecimal.add(BigDecimal.valueOf(Objects.nonNull(i.getMobilePaymentAmount()) ? i.getMobilePaymentAmount() : 0));
//            bigDecimal = bigDecimal.add(BigDecimal.valueOf(Objects.nonNull(i.getEPaymentAmount()) ? i.getEPaymentAmount() : 0));
//            i.setStatAmount(bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue());
            //计算金额差异
            BigDecimal decimal = BigDecimal.valueOf(Objects.nonNull(i.getPaidAmount()) ? i.getPaidAmount() : 0);
            double amountDiff = decimal.subtract(BigDecimal.valueOf(Objects.nonNull(i.getDueAmount()) ? i.getDueAmount() : 0))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
            i.setAmountDiff(amountDiff);
        });
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
                f1StationShiftTollVo.setShiftId(i.getShiftId().toString());
                f1StationShiftTollVo.setOperatorId(i.getOperatorId().toString());
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
        dto.setTimeFormat(Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN)));
        List<StationShiftVo> stationShiftVos = tollMapper.f2StationShift(dto);
        List<TbShVo> tbShData = tollMapper.getTbShData(dto);
        Map<Integer, TbShVo> tbShMap = tbShData.stream().collect(Collectors.toMap(TbShVo::getOperatorId, i -> i));
        stationShiftVos.forEach(i -> {
            if (Objects.nonNull(tbShMap.get(i.getOperatorId()))) {
                TbShVo tbShVo = tbShMap.get(i.getOperatorId());
                //实缴金额
                double handToll = new BigDecimal(tbShVo.getHandToll().toString()).doubleValue();
                i.setPaidAmount(handToll);
                //加收款
                double addedToll = new BigDecimal(tbShVo.getAddedToll().toString()).doubleValue();
                i.setExtraTotal(addedToll);
            }
            //计算统计金额
//            BigDecimal zero = new BigDecimal("0");
//            zero = zero.add(BigDecimal.valueOf(Objects.nonNull(i.getDueAmount()) ? i.getDueAmount() : 0));
//            zero = zero.add(BigDecimal.valueOf(Objects.nonNull(i.getMobilePaymentAmount()) ? i.getMobilePaymentAmount() : 0));
//            zero = zero.add(BigDecimal.valueOf(Objects.nonNull(i.getEPaymentAmount()) ? i.getEPaymentAmount() : 0));
//            i.setStatAmount(zero.setScale(2, RoundingMode.HALF_UP).doubleValue());
            //计算金额差异
            BigDecimal bigDecimal = BigDecimal.valueOf(Objects.nonNull(i.getPaidAmount()) ? i.getPaidAmount() : 0);
            double amountDiff = bigDecimal.subtract(BigDecimal.valueOf(Objects.nonNull(i.getDueAmount()) ? i.getDueAmount() : 0))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
            i.setAmountDiff(amountDiff);
        });
        //给列表增加小计行和合计行
        LinkedList<StationShiftVo> linkedList = buildTotalRow(stationShiftVos);
        return linkedList;
    }

    private LinkedList<StationShiftVo> buildTotalRow(List<StationShiftVo> list) {
        Map<Integer, List<StationShiftVo>> listMap = list.stream()
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
        subTotalRow.setStatAmount(list.stream().map(i -> BigDecimal.valueOf(i.getStatAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //应缴金额
        subTotalRow.setDueAmount(list.stream().map(i -> BigDecimal.valueOf(i.getDueAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //实缴金额
        subTotalRow.setPaidAmount(list.stream().map(i -> BigDecimal.valueOf(i.getPaidAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //金额差异
        subTotalRow.setAmountDiff(list.stream().map(i -> BigDecimal.valueOf(i.getAmountDiff()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //欠款车次
        subTotalRow.setArrearsTrips(list.stream().map(i -> new BigDecimal(i.getArrearsTrips()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).intValue());
        //欠款金额
        subTotalRow.setArrearsAmount(list.stream().map(i -> BigDecimal.valueOf(i.getArrearsAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //加收款
        subTotalRow.setExtraTotal(list.stream().map(i -> BigDecimal.valueOf(i.getExtraTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //移动支付
        subTotalRow.setMobilePaymentAmount(list.stream().map(i -> BigDecimal.valueOf(i.getMobilePaymentAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //电子支付
        subTotalRow.setEPaymentAmount(list.stream().map(i -> BigDecimal.valueOf(i.getEPaymentAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
        for (StationShiftVo stationShiftVo : stationShiftVos) {
            for (TbShVo tbSh : tbShVoList) {
                //日
                if (dto.getStatisticsType().equals("0")) {
                    if (stationShiftVo.getStaDate().equals(tbSh.getStaDate())) {
                        //实缴金额
                        stationShiftVo.setPaidAmount(tbSh.getHandToll());
                        //加收款
                        stationShiftVo.setExtraTotal(tbSh.getAddedToll());
                    }
                }
                //月
                else if (dto.getStatisticsType().equals("1")) {
                    if (stationShiftVo.getMonthDate().equals(tbSh.getMonthDate())) {
                        //实缴金额
                        stationShiftVo.setPaidAmount(tbSh.getHandToll());
                        //加收款
                        stationShiftVo.setExtraTotal(tbSh.getAddedToll());
                    }
                }
                //站
                else if (dto.getStatisticsType().equals("2")) {
                    if (stationShiftVo.getStationId().equals(tbSh.getStationId())) {
                        //实缴金额
                        stationShiftVo.setPaidAmount(tbSh.getHandToll());
                        //加收款
                        stationShiftVo.setExtraTotal(tbSh.getAddedToll());
                    }
                }
            }
        }
        //计算金额差异和统计金额
        stationShiftVos.forEach(i -> {
            //计算金额差异
            BigDecimal amountDiff = BigDecimal.valueOf(i.getPaidAmount());
            amountDiff = amountDiff.subtract(BigDecimal.valueOf(i.getDueAmount()));
            i.setAmountDiff(amountDiff.setScale(2, RoundingMode.HALF_UP).doubleValue());
            //给统计方式赋值
            if (dto.getStatisticsType().equals("0")) {
                i.setStatType(i.getStaDate().toString());
            } else if (dto.getStatisticsType().equals("1")) {
                i.setStatType(i.getMonthDate());
            } else if (dto.getStatisticsType().equals("2")) {
                i.setStatType(i.getStationName());
            }
        });
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
        for (VehicleClassStatVo vehicleClassStatVo : vehicleClassStatVos) {
            for (TbShVo tbSh : tbShVoList) {
                //日
                if (dto.getStatisticsType().equals("0")) {
                    if (vehicleClassStatVo.getStaDate().equals(tbSh.getStaDate())) {
                        //加收款
                        vehicleClassStatVo.setAddedAmount(tbSh.getAddedToll());
                    }
                }
                //月
                else if (dto.getStatisticsType().equals("1")) {
                    if (vehicleClassStatVo.getMonthDate().equals(tbSh.getMonthDate())) {
                        //加收款
                        vehicleClassStatVo.setAddedAmount(tbSh.getAddedToll());
                    }
                }
                //站
                else if (dto.getStatisticsType().equals("2")) {
                    if (vehicleClassStatVo.getStationId().equals(tbSh.getStationId())) {
                        //加收款
                        vehicleClassStatVo.setAddedAmount(tbSh.getAddedToll());
                    }
                }
                //人员
                else if (dto.getStatisticsType().equals("3")) {
                    if (vehicleClassStatVo.getOperatorId().equals(tbSh.getOperatorId())) {
                        //加收款
                        vehicleClassStatVo.setAddedAmount(tbSh.getAddedToll());
                    }
                }
            }
        }
        //计算合计
        vehicleClassStatVos.forEach(i -> {
            BigDecimal sum = BigDecimal.valueOf(Objects.nonNull(i.getCustSubTotal()) ? i.getCustSubTotal() : 0);
            sum = sum.add(BigDecimal.valueOf(Objects.nonNull(i.getTruckSubTotal()) ? i.getTruckSubTotal() : 0));
            sum = sum.add(BigDecimal.valueOf(Objects.nonNull(i.getSpecSubTotal()) ? i.getSpecSubTotal() : 0));
            sum = sum.add(BigDecimal.valueOf(Objects.nonNull(i.getAddedAmount()) ? i.getAddedAmount() : 0));
            i.setTotalAmount(sum.setScale(2, RoundingMode.HALF_UP).doubleValue());
        });
        //获取统计方式
        vehicleClassStatVos.forEach(v -> {
            if (dto.getStatisticsType().equals("0")) {
                v.setStatType(v.getStaDate().toString());
            } else if (dto.getStatisticsType().equals("1")) {
                v.setStatType(v.getMonthDate());
            } else if (dto.getStatisticsType().equals("2")) {
                v.setStatType(v.getStationName());
            } else if (dto.getStatisticsType().equals("3")) {
                v.setStatType(v.getOperatorId().toString());
            }
        });
        //合计
        VehicleClassStatVo totalRow = buildAfvTotalRow(vehicleClassStatVos);
        vehicleClassStatVos.add(totalRow);
        return vehicleClassStatVos;
    }

    private VehicleClassStatVo buildAfvTotalRow(List<VehicleClassStatVo> list) {
        VehicleClassStatVo vehicleClassStatVo = new VehicleClassStatVo();
        vehicleClassStatVo.setStatType("合计");
        vehicleClassStatVo.setTotalRow(true);
        vehicleClassStatVo.setCust1(list.stream().map(i -> BigDecimal.valueOf(i.getCust1()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setCust2(list.stream().map(i -> BigDecimal.valueOf(i.getCust2()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setCust3(list.stream().map(i -> BigDecimal.valueOf(i.getCust3()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setCust4(list.stream().map(i -> BigDecimal.valueOf(i.getCust4()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setCustSubTotal(list.stream().map(i -> BigDecimal.valueOf(i.getCustSubTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setTruck1(list.stream().map(i -> BigDecimal.valueOf(i.getTruck1()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setTruck2(list.stream().map(i -> BigDecimal.valueOf(i.getTruck2()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setTruck3(list.stream().map(i -> BigDecimal.valueOf(i.getTruck3()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setTruck4(list.stream().map(i -> BigDecimal.valueOf(i.getTruck4()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setTruck5(list.stream().map(i -> BigDecimal.valueOf(i.getTruck5()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setTruck6(list.stream().map(i -> BigDecimal.valueOf(i.getTruck6()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setTruckSubTotal(list.stream().map(i -> BigDecimal.valueOf(i.getTruckSubTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setSpec1(list.stream().map(i -> BigDecimal.valueOf(i.getSpec1()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setSpec2(list.stream().map(i -> BigDecimal.valueOf(i.getSpec2()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setSpec3(list.stream().map(i -> BigDecimal.valueOf(i.getSpec3()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setSpec4(list.stream().map(i -> BigDecimal.valueOf(i.getSpec4()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setSpec5(list.stream().map(i -> BigDecimal.valueOf(i.getSpec5()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setSpec6(list.stream().map(i -> BigDecimal.valueOf(i.getSpec6()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setSpecSubTotal(list.stream().map(i -> BigDecimal.valueOf(i.getSpecSubTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setAddedAmount(list.stream().map(i -> BigDecimal.valueOf(i.getAddedAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        vehicleClassStatVo.setTotalAmount(list.stream().map(i -> BigDecimal.valueOf(i.getTotalAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
            if (dto.getStatisticsType().equals("0")) {
                i.setStatType(i.getStaDate().toString());
            } else if (dto.getStatisticsType().equals("1")) {
                i.setStatType(i.getMonthDate());
            } else if (dto.getStatisticsType().equals("2")) {
                i.setStatType(i.getStationName());
            } else if (dto.getStatisticsType().equals("3")) {
                i.setStatType(i.getOperatorId().toString());
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
                                    summary.setOperatorId(list.get(0).getOperatorId());
                                    summary.setOperatorName(list.get(0).getOperatorName());
                                    list.forEach(item -> {
                                        summary.setToll(summary.getToll() + item.getToll());
                                        summary.setYjIcCardNum(summary.getYjIcCardNum() + item.getYjIcCardNum());
                                        summary.setPaperNum(summary.getPaperNum() + item.getPaperNum());
                                        summary.setYfIcCardNum(summary.getYfIcCardNum() + item.getYfIcCardNum());
                                    });
                                    return summary;
                                }
                        )
                ));
        List<F6TollVo> realResult = new ArrayList<>();
        map.forEach((s, item) -> realResult.add(item));
        realResult.forEach(item -> item.setToll(new BigDecimal(item.getToll().toString()).setScale(2, RoundingMode.HALF_UP).doubleValue()));

        //增加合计行
        F6TollVo totalRow = new F6TollVo();
        totalRow.setTotalRow(true);
        totalRow.setOperatorId("合计");
        totalRow.setToll(realResult.stream().map(i -> BigDecimal.valueOf(i.getToll()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).doubleValue());
        totalRow.setPaperNum(realResult.stream().map(i -> BigDecimal.valueOf(i.getPaperNum()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setYjIcCardNum(realResult.stream().map(i -> BigDecimal.valueOf(i.getYjIcCardNum()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setYfIcCardNum(realResult.stream().map(i -> BigDecimal.valueOf(i.getYfIcCardNum()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        realResult.add(totalRow);
        return realResult;
    }

    @Override
    public List<Cf1Vo> cf1Toll(StationShiftDto dto) {
        //构建收费站列表参数
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
        dto.setTimeFormat(Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN)));
        //下班解款数据 获取字段addedtoll,handtoll
        Map<Integer, TbShVo> tbShMap = new HashMap<>();
        dto.setTableName("sh" + DateUtil.format(dto.getTime(), DatePattern.SIMPLE_MONTH_PATTERN));
        List<TbShVo> tbShList = tollMapper.getTbShGroupByStation(dto);
        Set<Integer> stationIdSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(tbShList)) {
            tbShMap = tbShList.stream().collect(Collectors.toMap(TbShVo::getStationId, i -> i, (m,n)->m));
            stationIdSet.addAll(tbShMap.keySet());
        }
        //新款加收 获取字段extAddToll
        dto.setTableName("extrapay" + DateUtil.format(dto.getTime(), DatePattern.NORM_YEAR_PATTERN));
        Map<Integer, ExtraPayVo> extraPayMap = new HashMap<>();
        List<ExtraPayVo> extraPayList = tollMapper.getExtraPayByStation(dto);
        if (!CollectionUtils.isEmpty(extraPayList)) {
            extraPayMap = extraPayList.stream().collect(Collectors.toMap(ExtraPayVo::getStationId, i -> i, (m,n)->m));
            stationIdSet.addAll(extraPayMap.keySet());
        }
        //出口表数据
        dto.setTableName("tbstatexit" + DateUtil.format(dto.getTime(), DatePattern.SIMPLE_MONTH_PATTERN));
        List<Cf1Vo> tbstatExitList = tollMapper.getCf1Vo(dto);
        // 加收金额= addedtoll+extAddToll
        // 实收金额 = cash + addedtoll
        // 总金额 = 实收金额 + eAmount + mAmount
        // 金额差异 = handtoll - 实收金额
        if (CollectionUtils.isEmpty(tbstatExitList)) {
            return new ArrayList<>();
        } else {
            stationIdSet.addAll(tbstatExitList.stream().map(Cf1Vo::getStationId).collect(Collectors.toSet()));
            for (Cf1Vo item : tbstatExitList) {
                item.setDueAmount(new BigDecimal(item.getDueAmount()).setScale(2, RoundingMode.HALF_UP).toPlainString());
                item.setArrearsAmount(new BigDecimal(item.getArrearsAmount()).setScale(2, RoundingMode.HALF_UP).toPlainString());
                BigDecimal extraDecimal = new BigDecimal(0);
                BigDecimal paidDecimal = new BigDecimal(0);
                BigDecimal totalTollDecimal = new BigDecimal(0);
                BigDecimal diffDecimal = new BigDecimal(0);
                Integer stationId = item.getStationId();
                if (Objects.nonNull(tbShMap.get(stationId))) {
                    TbShVo tbShVo = tbShMap.get(stationId);
                    extraDecimal = extraDecimal.add(BigDecimal.valueOf(tbShVo.getAddedToll()));
                    paidDecimal = paidDecimal.add(BigDecimal.valueOf(tbShVo.getHandToll()));
                    diffDecimal = BigDecimal.valueOf(tbShVo.getHandToll());
                }
                if (Objects.nonNull(extraPayMap.get(stationId))) {
                    ExtraPayVo extraPayVo = extraPayMap.get(stationId);
                    extraDecimal = extraDecimal.add(BigDecimal.valueOf(extraPayVo.getExtAddToll()));
                }
                //加收金额
                String extraTotal = extraDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString();
                //实收金额
                paidDecimal = paidDecimal.add(extraDecimal);
                String paidAmount = paidDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString();
                //总金额
                totalTollDecimal = totalTollDecimal.add(paidDecimal);
                totalTollDecimal = totalTollDecimal.add(new BigDecimal(item.getEPaymentAmount()));
                totalTollDecimal = totalTollDecimal.add(new BigDecimal(item.getMobilePaymentAmount()));
                String statAmount =  totalTollDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString();
                //金额差异
                diffDecimal = diffDecimal.subtract(paidDecimal);
                String amountDiff = diffDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString();
                item.setExtraTotal(extraTotal);
                item.setPaidAmount(paidAmount);
                item.setStatAmount(statAmount);
                item.setAmountDiff(amountDiff);
            }
        }
        //构建合计行
        Cf1Vo totalRow = new Cf1Vo();
        totalRow.setTotalRow(true);
        totalRow.setStationName("合计");
        //统计金额
        totalRow.setStatAmount(tbstatExitList.stream().map(i -> new BigDecimal(i.getStatAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        //应缴金额
        totalRow.setDueAmount(tbstatExitList.stream().map(i -> new BigDecimal(i.getDueAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        //实缴金额
        totalRow.setPaidAmount(tbstatExitList.stream().map(i -> new BigDecimal(i.getPaidAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        //金额差异
        totalRow.setAmountDiff(tbstatExitList.stream().map(i -> new BigDecimal(i.getAmountDiff()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        //欠款
        totalRow.setArrearsAmount(tbstatExitList.stream().map(i -> new BigDecimal(i.getArrearsAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        //加收款
        totalRow.setExtraTotal(tbstatExitList.stream().map(i -> new BigDecimal(i.getExtraTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        //移动支付
        totalRow.setMobilePaymentAmount(tbstatExitList.stream().map(i -> new BigDecimal(i.getMobilePaymentAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        //电子支付
        totalRow.setEPaymentAmount(tbstatExitList.stream().map(i -> new BigDecimal(i.getEPaymentAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toPlainString());
        //公务IC卡
        totalRow.setOfficialIcCardCount(tbstatExitList.stream().map(i -> BigDecimal.valueOf(i.getOfficialIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).intValue());
        //军车IC卡
        totalRow.setMilitaryIcCardCount(tbstatExitList.stream().map(i -> BigDecimal.valueOf(i.getMilitaryIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).intValue());
        //免费IC卡
        totalRow.setFreeIcCardCount(tbstatExitList.stream().map(i -> BigDecimal.valueOf(i.getFreeIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).intValue());
        //应缴IC卡
        totalRow.setDueIcCardCount(tbstatExitList.stream().map(i -> BigDecimal.valueOf(i.getDueIcCardCount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).intValue());
        tbstatExitList.add(totalRow);
        return tbstatExitList;
    }

    @Override
    public List<MOBTollVo> mobToll(FtStationDto dto) {
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
