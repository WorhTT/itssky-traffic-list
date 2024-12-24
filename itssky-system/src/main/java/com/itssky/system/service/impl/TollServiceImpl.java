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
        List<StationShiftVo> stationShiftVos = tollMapper.f1StationShift(dto);
        List<TbShVo> tbShData = tollMapper.getTbShData(dto);
        Map<Integer, TbShVo> tbShMap = tbShData.stream().collect(Collectors.toMap(TbShVo::getOperatorId, i -> i));
        stationShiftVos.forEach(i -> {
            if (Objects.nonNull(tbShMap.get(i.getOperatorId()))) {
                TbShVo tbShVo = tbShMap.get(i.getOperatorId());
                //实缴金额
                int handToll = new BigDecimal(tbShVo.getHandToll().toString()).intValue();
                i.setPaidAmount(handToll);
                //加收款
                int addedToll = new BigDecimal(tbShVo.getAddedToll().toString()).intValue();
                i.setExtraTotal(addedToll);
            }
            //计算统计金额
            i.setStatAmount(i.getDueAmount() + i.getMobilePaymentAmount() + i.getEPaymentAmount());
            //计算金额差异
            i.setAmountDiff(i.getPaidAmount() - i.getDueAmount());
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
                int handToll = new BigDecimal(tbShVo.getHandToll().toString()).intValue();
                i.setPaidAmount(handToll);
                //加收款
                int addedToll = new BigDecimal(tbShVo.getAddedToll().toString()).intValue();
                i.setExtraTotal(addedToll);
            }
            //计算统计金额
            i.setStatAmount(i.getDueAmount() + i.getMobilePaymentAmount() + i.getEPaymentAmount());
            //计算金额差异
            i.setAmountDiff(i.getPaidAmount() - i.getDueAmount());
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
        subTotalRow.setStatAmount(list.stream().map(i -> new BigDecimal(i.getStatAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //应缴金额
        subTotalRow.setDueAmount(list.stream().map(i -> new BigDecimal(i.getDueAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //实缴金额
        subTotalRow.setPaidAmount(list.stream().map(i -> new BigDecimal(i.getPaidAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //金额差异
        subTotalRow.setAmountDiff(list.stream().map(i -> new BigDecimal(i.getAmountDiff()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //欠款车次
        subTotalRow.setArrearsTrips(list.stream().map(i -> new BigDecimal(i.getArrearsTrips()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //欠款金额
        subTotalRow.setArrearsAmount(list.stream().map(i -> new BigDecimal(i.getArrearsAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //加收款
        subTotalRow.setExtraTotal(list.stream().map(i -> new BigDecimal(i.getExtraTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //移动支付
        subTotalRow.setMobilePaymentAmount(list.stream().map(i -> new BigDecimal(i.getExtraMobilePayment()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        //电子支付
        subTotalRow.setEPaymentAmount(list.stream().map(i -> new BigDecimal(i.getEPaymentAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
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
        for (StationShiftVo stationShiftVo: stationShiftVos) {
            for (TbShVo tbSh: tbShVoList) {
                //日
                if (dto.getStatisticsType().equals("0") ) {
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
            //计算统计金额
            i.setStatAmount(i.getDueAmount() + i.getMobilePaymentAmount() + i.getEPaymentAmount());
            //计算金额差异
            i.setAmountDiff(i.getPaidAmount() - i.getDueAmount());
            //给统计方式赋值
            if (dto.getStatisticsType().equals("0")) {
                i.setStatType(i.getStaDate().toString());
            }
            else if (dto.getStatisticsType().equals("1")) {
                i.setStatType(i.getMonthDate());
            }
            else if (dto.getStatisticsType().equals("2")) {
                i.setStatType(i.getStationName());
            }
        });
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
        for (VehicleClassStatVo vehicleClassStatVo: vehicleClassStatVos) {
            for (TbShVo tbSh: tbShVoList) {
                //日
                if (dto.getStatisticsType().equals("0") ) {
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
        vehicleClassStatVos.forEach(i -> i.setTotalAmount(i.getCustSubTotal()
                + i.getTruckSubTotal() + i.getSpecSubTotal() + i.getAddedAmount()));
        //获取统计方式
        vehicleClassStatVos.forEach(v -> {
            if (dto.getStatisticsType().equals("0")) {
                v.setStatType(v.getStaDate().toString());
            }
            else if (dto.getStatisticsType().equals("1")) {
                v.setStatType(v.getMonthDate());
            }
            else if (dto.getStatisticsType().equals("2")) {
                v.setStatType(v.getStationName());
            }
            else if (dto.getStatisticsType().equals("3")) {
                v.setStatType(v.getOperatorId().toString());
            }
        });
        return vehicleClassStatVos;
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
}
