package com.itssky.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itssky.common.utils.DateUtils;
import com.itssky.common.utils.MybatisPlusTableNameHelper;
import com.itssky.db.Dbedge;
import com.itssky.db.Dbstats;
import com.itssky.system.domain.*;
import com.itssky.system.domain.dto.FlowStatisticsDto;
import com.itssky.system.mapper.EntryFlowMapper;
import com.itssky.system.mapper.ExitFlowMapper;
import com.itssky.system.mapper.ReportFlowMapper;
import com.itssky.system.mapper.TbShMapper;
import com.itssky.util.TableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ITSSKY
 */
@Slf4j
@Service
public class ReportFlowService {

    @Autowired
    private ReportFlowMapper reportFlowMapper;

    @Autowired
    private EntryFlowMapper entryFlowMapper;

    @Autowired
    private ExitFlowMapper exitFlowMapper;

    @Autowired
    private TbShMapper tbShMapper;


//    /**
//     * 获取高速入口流量报表
//     */
//    public List<ReportFlowInfo> getEntryFlow(ReportFlowInfo param) {
//        List<ReportFlowInfo> result = new ArrayList<>();
//        List<TbStateEntry> tbStateEntryList = new ArrayList<>();
//        if (Objects.nonNull(param) && !CollectionUtils.isEmpty(param.getDateRange())) {
//            QueryWrapper<TbStateEntry> queryWrapper = new QueryWrapper<>();
//            if (param.getDateRange().get(0).after(param.getDateRange().get(1))) {
//                log.error("时间范围传参有误");
//                return new ArrayList<>();
//            }
//            queryWrapper.between("BalanceTime", param.getDateRange().get(0), param.getDateRange().get(1));
//            tbStateEntryList = entryFlowMapper.selectList(queryWrapper);
//        } else {
//            tbStateEntryList = reportFlowMapper.getTbStateEntryList(0);
//        }
//
//        if (CollectionUtils.isEmpty(tbStateEntryList)) {
//            log.error("入口班次统计列表查询为空 请联系管理员");
//            return result;
//        }
//        Set<Integer> stationIdSet = tbStateEntryList.stream()
//                .map(TbStateEntry::getStationID).collect(Collectors.toSet());
//        if (CollectionUtils.isEmpty(stationIdSet)) {
//            log.error("站点编码列表查询为空 请联系管理员");
//            return result;
//        }
//        List<StationCode> stationCodeByIds = reportFlowMapper.getStationCodeByIds(stationIdSet);
//        Map<Integer, String> stationCodeMap = stationCodeByIds.stream()
//                .collect(Collectors.toMap(StationCode::getStationid, StationCode::getStationname, (m, n) -> m));
//        Set<Integer> vehicleClassSet = tbStateEntryList.stream()
//                .map(TbStateEntry::getVehicleClass).collect(Collectors.toSet());
//        if (CollectionUtils.isEmpty(vehicleClassSet)) {
//            log.error("车型列表查询为空 请联系管理员");
//            return result;
//        }
//        Map<Integer, List<TbStateEntry>> tbStateEntryMapByStationId = tbStateEntryList.stream()
//                .collect(Collectors.groupingBy(TbStateEntry::getStationID));
//        for (Map.Entry<Integer, List<TbStateEntry>> entry : tbStateEntryMapByStationId.entrySet()) {
//            ReportFlowInfo reportFlowInfo = new ReportFlowInfo();
//            Integer stationId = entry.getKey();
//            if (!CollectionUtils.isEmpty(stationCodeMap) && Objects.nonNull(stationCodeMap.get(stationId))) {
//                String stationName = stationCodeMap.get(stationId);
//                reportFlowInfo.setStationName(stationName);
//            } else {
//                continue;
//            }
//            List<TbStateEntry> entryList = entry.getValue();
//            fillEntryFlowReport(reportFlowInfo, entryList, new ArrayList<>());
//            result.add(reportFlowInfo);
//        }
//        ReportFlowInfo amountReportFlowInfo = new ReportFlowInfo();
//        amountReportFlowInfo.setStationName("合计");
//        fillEntryFlowReport(amountReportFlowInfo, tbStateEntryList, new ArrayList<>());
//        result.add(amountReportFlowInfo);
//        return result;
//    }

    /**
     * 获取高速出口流量报表
     * flag 1入口 2出口
     */
    public List<ReportFlowInfo> getExitFlow(FlowStatisticsDto dto, int flag) {
        //获取收费站ID列表
        if (!CollectionUtils.isEmpty(dto.getStationIdArray())) {
            List<Integer> stationIdList = new ArrayList<>();
            dto.getStationIdArray().forEach(d -> stationIdList.add(d.get(2)));
            dto.setStationIdList(stationIdList);
        }
        //构建会查询到的表集合
        String tablePrefix = null;
        if (flag == 1) {
            tablePrefix = "tbstatentry";
        } else if (flag == 2) {
            tablePrefix = "tbstatexit";
        }
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), tablePrefix,
                        DatePattern.SIMPLE_MONTH_PATTERN));
        if (CollectionUtils.isEmpty(dto.getTableNameList())) {
            return new ArrayList<>();
        }
        //时间传参格式化
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<ReportFlowInfo> reportFlowInfos = reportFlowMapper.csjFlow(dto);
        //设置统计方式
        reportFlowInfos.forEach(r -> {
            if (dto.getStatisticsType().equals("0")) {
                r.setStatType(r.getStaDate().toString());
            } else if (dto.getStatisticsType().equals("1")) {
                r.setStatType(r.getMonthDate());
            } else if (dto.getStatisticsType().equals("2")) {
                r.setStatType(r.getStationName());
            }
        });
        //构建合计行
        ReportFlowInfo total =  new ReportFlowInfo().builder()
                .statType("合计")
                .k1(reportFlowInfos.stream().map(i -> new BigDecimal(i.getK1())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .k2(reportFlowInfos.stream().map(i -> new BigDecimal(i.getK2())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .k3(reportFlowInfos.stream().map(i -> new BigDecimal(i.getK3())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .k4(reportFlowInfos.stream().map(i -> new BigDecimal(i.getK4())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .kAmount(reportFlowInfos.stream().map(i -> new BigDecimal(i.getKAmount())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .h1(reportFlowInfos.stream().map(i -> new BigDecimal(i.getH1())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .h2(reportFlowInfos.stream().map(i -> new BigDecimal(i.getH2())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .h3(reportFlowInfos.stream().map(i -> new BigDecimal(i.getH3())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .h4(reportFlowInfos.stream().map(i -> new BigDecimal(i.getH4())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .h5(reportFlowInfos.stream().map(i -> new BigDecimal(i.getH5())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .h6(reportFlowInfos.stream().map(i -> new BigDecimal(i.getH6())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .hAmount(reportFlowInfos.stream().map(i -> new BigDecimal(i.getHAmount())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .z1(reportFlowInfos.stream().map(i -> new BigDecimal(i.getZ1())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .z2(reportFlowInfos.stream().map(i -> new BigDecimal(i.getZ2())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .z3(reportFlowInfos.stream().map(i -> new BigDecimal(i.getZ3())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .z4(reportFlowInfos.stream().map(i -> new BigDecimal(i.getZ4())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .z5(reportFlowInfos.stream().map(i -> new BigDecimal(i.getZ5())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .z6(reportFlowInfos.stream().map(i -> new BigDecimal(i.getZ6())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .zAmount(reportFlowInfos.stream().map(i -> new BigDecimal(i.getZAmount())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .official(reportFlowInfos.stream().map(i -> new BigDecimal(i.getOfficial())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .military(reportFlowInfos.stream().map(i -> new BigDecimal(i.getMilitary())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .fleet(reportFlowInfos.stream().map(i -> new BigDecimal(i.getFleet())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .free(reportFlowInfos.stream().map(i -> new BigDecimal(i.getFree())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .allAmount(reportFlowInfos.stream().map(i -> new BigDecimal(i.getAllAmount())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                .build();
        reportFlowInfos.add(total);
        return reportFlowInfos;
    }

    /**
     * 填充报表中的主要内容
     */
    private void fillEntryFlowReport(ReportFlowInfo reportFlowInfo, List<TbStateEntry> tbStateEntryList, List<TbStateExit> tbStateExitList) {

        if (!CollectionUtils.isEmpty(tbStateEntryList)) {
            //客一流量
            BigDecimal k1decimal = tbStateEntryList.stream().filter(i -> 1 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setK1(k1decimal.intValue());
            //客二
            BigDecimal k2decimal = tbStateEntryList.stream().filter(i -> 2 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setK2(k2decimal.intValue());
            //客三
            BigDecimal k3decimal = tbStateEntryList.stream().filter(i -> 3 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setK3(k3decimal.intValue());
            //客四
            BigDecimal k4decimal = tbStateEntryList.stream().filter(i -> 4 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setK4(k4decimal.intValue());
            //客车小计
            BigDecimal kAmountDecimal = tbStateEntryList.stream().filter(i -> i.getVehicleClass() <= 4)
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setKAmount(kAmountDecimal.intValue());
            //货一
            BigDecimal h1decimal = tbStateEntryList.stream().filter(i -> 11 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH1(h1decimal.intValue());
            //货二
            BigDecimal h2decimal = tbStateEntryList.stream().filter(i -> 12 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH2(h2decimal.intValue());
            //货三
            BigDecimal h3decimal = tbStateEntryList.stream().filter(i -> 13 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH3(h3decimal.intValue());
            //货四
            BigDecimal h4decimal = tbStateEntryList.stream().filter(i -> 14 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH4(h4decimal.intValue());
            //货五
            BigDecimal h5decimal = tbStateEntryList.stream().filter(i -> 15 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH5(h5decimal.intValue());
            //货六
            BigDecimal h6decimal = tbStateEntryList.stream().filter(i -> 16 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH6(h6decimal.intValue());
            //货车小计
            BigDecimal hAmountDecimal = tbStateEntryList.stream().filter(i -> i.getVehicleClass() <= 16 && i.getVehicleClass() >= 11)
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setHAmount(hAmountDecimal.intValue());
            //专一
            BigDecimal z1decimal = tbStateEntryList.stream().filter(i -> 21 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ1(z1decimal.intValue());
            //专二
            BigDecimal z2decimal = tbStateEntryList.stream().filter(i -> 22 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ2(z2decimal.intValue());
            //专三
            BigDecimal z3decimal = tbStateEntryList.stream().filter(i -> 23 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ3(z3decimal.intValue());
            //专四
            BigDecimal z4decimal = tbStateEntryList.stream().filter(i -> 24 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ4(z4decimal.intValue());
            //专五
            BigDecimal z5decimal = tbStateEntryList.stream().filter(i -> 25 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ5(z5decimal.intValue());
            //专六
            BigDecimal z6decimal = tbStateEntryList.stream().filter(i -> 26 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ6(z6decimal.intValue());
            //专车小计
            BigDecimal zAmountDecimal = tbStateEntryList.stream().filter(i -> i.getVehicleClass() >= 21 && i.getVehicleClass() <= 26)
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZAmount(zAmountDecimal.intValue());
            //公务
            reportFlowInfo.setOfficial((int)tbStateEntryList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("011")).count());
            //军车
            reportFlowInfo.setMilitary((int)tbStateEntryList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("100")).count());
            //优惠
            reportFlowInfo.setDiscount((int)tbStateEntryList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("101")).count());
            //免费
            reportFlowInfo.setFree((int)tbStateEntryList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("110")).count());
            //车队
            reportFlowInfo.setFleet((int)tbStateEntryList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("111")).count());
            //总计
            BigDecimal allAmount = tbStateEntryList.stream()
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setAllAmount(allAmount.intValue());
        } else if (!CollectionUtils.isEmpty(tbStateExitList)) {
            //客一流量
            BigDecimal k1decimal = tbStateExitList.stream().filter(i -> 1 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setK1(k1decimal.intValue());
            //客二
            BigDecimal k2decimal = tbStateExitList.stream().filter(i -> 2 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setK2(k2decimal.intValue());
            //客三
            BigDecimal k3decimal = tbStateExitList.stream().filter(i -> 3 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setK3(k3decimal.intValue());
            //客四
            BigDecimal k4decimal = tbStateExitList.stream().filter(i -> 4 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setK4(k4decimal.intValue());
            //客车小计
            BigDecimal kAmountDecimal = tbStateExitList.stream().filter(i -> i.getVehicleClass() <= 4)
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setKAmount(kAmountDecimal.intValue());
            //货一
            BigDecimal h1decimal = tbStateExitList.stream().filter(i -> 11 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH1(h1decimal.intValue());
            //货二
            BigDecimal h2decimal = tbStateExitList.stream().filter(i -> 12 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH2(h2decimal.intValue());
            //货三
            BigDecimal h3decimal = tbStateExitList.stream().filter(i -> 13 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH3(h3decimal.intValue());
            //货四
            BigDecimal h4decimal = tbStateExitList.stream().filter(i -> 14 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH4(h4decimal.intValue());
            //货五
            BigDecimal h5decimal = tbStateExitList.stream().filter(i -> 15 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH5(h5decimal.intValue());
            //货六
            BigDecimal h6decimal = tbStateExitList.stream().filter(i -> 16 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setH6(h6decimal.intValue());
            //货车小计
            BigDecimal hAmountDecimal = tbStateExitList.stream().filter(i -> i.getVehicleClass() <= 16 && i.getVehicleClass() >= 11)
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setHAmount(hAmountDecimal.intValue());
            //专一
            BigDecimal z1decimal = tbStateExitList.stream().filter(i -> 21 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ1(z1decimal.intValue());
            //专二
            BigDecimal z2decimal = tbStateExitList.stream().filter(i -> 22 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ2(z2decimal.intValue());
            //专三
            BigDecimal z3decimal = tbStateExitList.stream().filter(i -> 23 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ3(z3decimal.intValue());
            //专四
            BigDecimal z4decimal = tbStateExitList.stream().filter(i -> 24 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ4(z4decimal.intValue());
            //专五
            BigDecimal z5decimal = tbStateExitList.stream().filter(i -> 25 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ5(z5decimal.intValue());
            //专六
            BigDecimal z6decimal = tbStateExitList.stream().filter(i -> 26 == i.getVehicleClass())
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZ6(z6decimal.intValue());
            //专车小计
            BigDecimal zAmountDecimal = tbStateExitList.stream().filter(i -> i.getVehicleClass() >= 21 && i.getVehicleClass() <= 26)
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setZAmount(zAmountDecimal.intValue());
            //公务
            reportFlowInfo.setOfficial((int)tbStateExitList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("011")).count());
            //军车
            reportFlowInfo.setMilitary((int)tbStateExitList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("100")).count());
            //优惠
            reportFlowInfo.setDiscount((int)tbStateExitList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("101")).count());
            //免费
            reportFlowInfo.setFree((int)tbStateExitList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("110")).count());
            //车队
            reportFlowInfo.setFleet((int)tbStateExitList.stream()
                    .filter(i -> getBinaryFromInt(i.getDealStatus()).equals("111")).count());
            //总计
            BigDecimal allAmount = tbStateExitList.stream()
                    .map(i -> new BigDecimal(i.getCarNum())).reduce(BigDecimal.ZERO, BigDecimal::add);
            reportFlowInfo.setAllAmount(allAmount.intValue());
        }

    }


    public void mockTbEntryData() {
        List<TbStateEntry> tbStateEntryList = new ArrayList<>();
        //随机生成1000条数据
        Random random = new Random();
        List<StationCode> allStationCode = reportFlowMapper.getAllStationCode();
        List<VehicleClass> allVehicleClass = reportFlowMapper.getAllVehicleClass();
        int size = allStationCode.size();
        //动态表名
        Map<String, Object> map = new HashMap<>();
        map.put(MybatisPlusTableNameHelper.TABLE_TIME, "202411");
        MybatisPlusTableNameHelper.setRequestData(map);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                TbStateEntry tbStateExit = buildTbStateEntry(random,1, random.nextInt(5) + 1, allVehicleClass);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateEntry tbStateExit = buildTbStateEntry(random,2, random.nextInt(5) + 6, allVehicleClass);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateEntry tbStateExit = buildTbStateEntry(random,3, random.nextInt(5) + 11, allVehicleClass);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateEntry tbStateExit = buildTbStateEntry(random, 1,random.nextInt(5) + 1, allVehicleClass);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateEntry tbStateExit = buildTbStateEntry(random, 2,random.nextInt(5) + 6, allVehicleClass);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateEntry tbStateExit = buildTbStateEntry(random, 3,random.nextInt(5) + 11, allVehicleClass);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateEntry tbStateExit = buildTbStateEntry(random, 1, random.nextInt(5) + 1, allVehicleClass);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateEntry tbStateExit = buildTbStateEntry(random, 2, random.nextInt(5) + 6, allVehicleClass);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateEntry tbStateExit = buildTbStateEntry(random, 3, random.nextInt(5) + 11, allVehicleClass);
                tbStateEntryList.add(tbStateExit);
            }
        }
        try {
            tbStateEntryList.forEach(i -> reportFlowMapper.insertTbStateEntry(i));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MybatisPlusTableNameHelper.clear();
        }
    }

    public void mockData() {
        List<TbStateExit> tbStateEntryList = new ArrayList<>();
        //随机生成1000条数据
        Random random = new Random();
        List<StationCode> allStationCode = reportFlowMapper.getAllStationCode();
        List<VehicleClass> allVehicleClass = reportFlowMapper.getAllVehicleClass();
        int size = allStationCode.size();
        //动态表名
        String formatDateTime = DateUtil.format(new Date(), DatePattern.SIMPLE_MONTH_PATTERN);
        Map<String, Object> map = new HashMap<>();
//        map.put(MybatisPlusTableNameHelper.TABLE_TIME, formatDateTime);
        map.put(MybatisPlusTableNameHelper.TABLE_TIME, "202411");
        MybatisPlusTableNameHelper.setRequestData(map);
        for (int i = 0; i < 3; i++) {
            StationCode stationCode = allStationCode.get(random.nextInt(size - 1));
            for (int j = 0; j < 10; j++) {
                TbStateExit tbStateExit = buildTbStateExit(random,1, random.nextInt(5) + 1, allVehicleClass);
                //生成移动支付收费数据 早班
                tbStateExit.setPayType(16);
                int i1 = random.nextInt(100);
                tbStateExit.setTotalToll(i1);
                tbStateExit.setTotalFee(i1);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateExit tbStateExit = buildTbStateExit(random,2, random.nextInt(5) + 6, allVehicleClass);
                //生成移动支付收费数据 中班
                tbStateExit.setPayType(16);
                int i1 = random.nextInt(100);
                tbStateExit.setTotalToll(i1);
                tbStateExit.setTotalFee(i1);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateExit tbStateExit = buildTbStateExit(random,3, random.nextInt(5) + 11, allVehicleClass);
                //生成移动支付收费数据 晚班
                tbStateExit.setPayType(16);
                int i1 = random.nextInt(100);
                tbStateExit.setTotalToll(i1);
                tbStateExit.setTotalFee(i1);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateExit tbStateExit = buildTbStateExit(random, 1,random.nextInt(5) + 1, allVehicleClass);
                //生成现金收费数据 早班
                tbStateExit.setPayType(0);
                int i1 = random.nextInt(100);
                tbStateExit.setTotalToll(i1);
                tbStateExit.setTotalFee(i1);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateExit tbStateExit = buildTbStateExit(random, 2,random.nextInt(5) + 6, allVehicleClass);
                //生成现金收费数据 中班
                tbStateExit.setPayType(0);
                int i1 = random.nextInt(100);
                tbStateExit.setTotalToll(i1);
                tbStateExit.setTotalFee(i1);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateExit tbStateExit = buildTbStateExit(random, 3,random.nextInt(5) + 11, allVehicleClass);
                //生成现金收费数据 晚班
                tbStateExit.setPayType(0);
                int i1 = random.nextInt(100);
                tbStateExit.setTotalToll(i1);
                tbStateExit.setTotalFee(i1);
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateExit tbStateExit = buildTbStateExit(random, 1, random.nextInt(5) + 1, allVehicleClass);
                //生成非现金收费数据 早班
                tbStateExit.setPayType(2);
//                int cardType = random.nextInt(23 - 22 + 1) + 22;
//                tbStateExit.setCardType(cardType);
//                Double generateDouble = generateDouble();
                tbStateExit.setTotalToll(random.nextInt(100));
                tbStateExit.setTotalFee(random.nextInt(100));
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateExit tbStateExit = buildTbStateExit(random, 2, random.nextInt(5) + 6, allVehicleClass);
                //生成非现金收费数据 中班
                tbStateExit.setPayType(2);
//                int cardType = random.nextInt(23 - 22 + 1) + 22;
//                tbStateExit.setCardType(cardType);
//                Double generateDouble = generateDouble();
                tbStateExit.setTotalToll(random.nextInt(100));
                tbStateExit.setTotalFee(random.nextInt(100));
                tbStateEntryList.add(tbStateExit);
            }
            for (int j = 0; j < 10; j++) {
                TbStateExit tbStateExit = buildTbStateExit(random, 3, random.nextInt(5) + 11, allVehicleClass);
                //生成非现金收费数据 晚班
                tbStateExit.setPayType(2);
//                int cardType = random.nextInt(23 - 22 + 1) + 22;
//                tbStateExit.setCardType(cardType);
//                Double generateDouble = generateDouble();
                tbStateExit.setTotalToll(random.nextInt(100));
                tbStateExit.setTotalFee(random.nextInt(100));
                tbStateEntryList.add(tbStateExit);
            }
        }
        try {
            tbStateEntryList.forEach(i -> reportFlowMapper.insertTbStateExit(i));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MybatisPlusTableNameHelper.clear();
        }
    }

    private TbStateEntry buildTbStateEntry(Random random, Integer shiftId, Integer operatorId, List<VehicleClass> allVehicleClass) {
        TbStateEntry tbStateEntry = new TbStateEntry();
        tbStateEntry.setVehicleClass(allVehicleClass.get(random.nextInt(allVehicleClass.size())).getVehicleclass());
        tbStateEntry.setBalanceOP(1);
        tbStateEntry.setBalanceTime(DateUtils.getNowDate());
        tbStateEntry.setCancelFlag(1);
        String binary = generateBinary();
        BigInteger bigInteger = new BigInteger(binary, 2);
        System.out.println(bigInteger);
        tbStateEntry.setDealStatus(bigInteger.intValue());
        tbStateEntry.setLaneID(1);
        tbStateEntry.setTeamID(1);
        tbStateEntry.setSpare(1);
        tbStateEntry.setOperatorID(operatorId);
        tbStateEntry.setStaDate(20241130);
        tbStateEntry.setShiftID(shiftId);
        tbStateEntry.setStationID(100007);
        tbStateEntry.setCarNum(random.nextInt(100));
        tbStateEntry.setLaneID(1);
        tbStateEntry.setTransfermark(1);
        List<Integer> cardTypeList = new ArrayList<>();
        cardTypeList.add(0);
        cardTypeList.add(22);
        cardTypeList.add(23);
        cardTypeList.add(26);
        cardTypeList.add(254);
        cardTypeList.add(255);
        tbStateEntry.setCardType(cardTypeList.get(random.nextInt(6)));
        return tbStateEntry;
    }

    private TbStateExit buildTbStateExit(Random random, Integer shiftId, Integer operatorId, List<VehicleClass> allVehicleClass ) {
        TbStateExit tbStateEntry = new TbStateExit();
        tbStateEntry.setVehicleClass(allVehicleClass.get(random.nextInt(allVehicleClass.size())).getVehicleclass());
        tbStateEntry.setBalanceOP(1);
        tbStateEntry.setBalanceTime(DateUtils.getNowDate());
        tbStateEntry.setCancelFlag(1);
        String binary = generateBinary();
        BigInteger bigInteger = new BigInteger(binary, 2);
        System.out.println(bigInteger);
        tbStateEntry.setDealStatus(bigInteger.intValue());
        tbStateEntry.setLaneID(1);
        tbStateEntry.setTeamID(1);
        tbStateEntry.setSpare(1);
        tbStateEntry.setOperatorID(operatorId);
//        tbStateEntry.setStaDate(Integer.parseInt(DateUtils.getDate().replaceAll("-", "")));
        tbStateEntry.setStaDate(20241130);
        tbStateEntry.setShiftID(shiftId);
        tbStateEntry.setStationID(32010101);
        tbStateEntry.setCarNum(random.nextInt(100));
        tbStateEntry.setLaneID(1);
        tbStateEntry.setTransfermark(1);
        List<Integer> cardTypeList = new ArrayList<>();
        //默认
        cardTypeList.add(0);
        //ETC储值卡(C卡)
        cardTypeList.add(22);
        //ETC记账卡(D卡)
        cardTypeList.add(23);
        //CPC卡
        cardTypeList.add(26);
        //纸券
        cardTypeList.add(254);
        //不存卡
        cardTypeList.add(255);
        tbStateEntry.setCardType(cardTypeList.get(random.nextInt(6)));
        tbStateEntry.setTotalArrearage(0);
        return tbStateEntry;
    }

    /**
     * 生成16位二进制
     */
    // 创建一个方法来生成二进制组合
    private static String generateBinary() {
        int length = 16;
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(2));
        }
        return stringBuilder.toString();
    }

    /**
     * 十进制转化为2进制 获取1-3位
     */
    private static String getBinaryFromInt(int value) {
        BigInteger bigInteger = new BigInteger(String.valueOf(value));
        StringBuilder binary = new StringBuilder(bigInteger.toString(2));
        if (binary.length() < 16) {
            int cz = 16 - binary.length();
            for (int i = 0; i < cz; i++) {
                binary.insert(0, "0");
            }
        }
        return binary.substring(1, 4);
    }

    /**
     * 随机生成带两位小数的浮点类型数字
     */
    private Double generateDouble() {
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("#.00");
        int randomInt = random.nextInt(100); // 随机生成0到99之间的整数
        String randomDecimal = df.format(randomInt / 100.0);
        return Double.parseDouble(randomDecimal);
    }

    public static void main(String[] args) {
//        String s = generateBinary();// 生成和打印所有组合
//        BigInteger i = new BigInteger(s, 2);
//        String binaryFromInt = getBinaryFromInt(i.intValue());
//        System.out.println(binaryFromInt);
//        //获取123位
//        String s2 = Integer.toBinaryString(i.intValue());
//        String s1 = i.toString(2);
//        String substring = s1.substring(1, 4);
//        System.out.println(s);
//        System.out.println(i);
//        System.out.println(s2);
//        System.out.println(s1);
//        System.out.println(substring);
//        String date = DateUtils.getDate();
//        System.out.println(date);
    }

    /**
     * 获取高速收费报表
     */
    public List<ReportChargeInfo> getCharge() {
        List<ReportChargeInfo> result = new ArrayList<>();
        List<TbStateExit> tbStateExitList = reportFlowMapper.getTbStateExitList(0);
        if (CollectionUtils.isEmpty(tbStateExitList)) {
            log.error("入口班次统计列表查询为空 请联系管理员");
            return result;
        }
        Set<Integer> stationIdSet = tbStateExitList.stream()
                .map(TbStateExit::getStationID).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(stationIdSet)) {
            log.error("站点编码列表查询为空 请联系管理员");
            return result;
        }
        List<StationCode> stationCodeByIds = reportFlowMapper.getStationCodeByIds(stationIdSet);
        Map<Integer, String> stationCodeMap = stationCodeByIds.stream()
                .collect(Collectors.toMap(StationCode::getStationid, StationCode::getStationname, (m, n) -> m));
        Map<Integer, List<TbStateExit>> tbStateEntryMapByStationId = tbStateExitList.stream()
                .collect(Collectors.groupingBy(TbStateExit::getStationID));
        for (Map.Entry<Integer, List<TbStateExit>> entry : tbStateEntryMapByStationId.entrySet()) {
            Integer stationId = entry.getKey();
            ReportChargeInfo reportChargeInfo = new ReportChargeInfo();
            if (!CollectionUtils.isEmpty(stationCodeMap) && Objects.nonNull(stationCodeMap.get(stationId))) {
                String stationName = stationCodeMap.get(stationId);
                reportChargeInfo.setStationName(stationName);
            } else {
                continue;
            }
            List<TbStateExit> entryList = entry.getValue();
            fillChargeInfo(entryList, reportChargeInfo);
            result.add(reportChargeInfo);
        }
        return result;
    }

    private void fillChargeInfo(List<TbStateExit> tbStateEntryList, ReportChargeInfo reportChargeInfo) {
        //应缴金额
        BigDecimal yjje = tbStateEntryList.stream().filter(i -> 0 == i.getPayType())
                .map(i -> BigDecimal.valueOf(i.getTotalFee())).reduce(BigDecimal.ZERO, BigDecimal::add);
        reportChargeInfo.setYjje(yjje.stripTrailingZeros().toPlainString());
        //实缴金额
        BigDecimal sjje = tbStateEntryList.stream().filter(i -> 0 == i.getPayType())
                .map(i -> BigDecimal.valueOf(i.getTotalToll())).reduce(BigDecimal.ZERO, BigDecimal::add);
        reportChargeInfo.setSjje(sjje.stripTrailingZeros().toPlainString());
        //金额差异
        BigDecimal jecy = sjje.subtract(yjje);
        reportChargeInfo.setJecy(jecy.stripTrailingZeros().toPlainString());
        //欠款车次
        long qkcc = tbStateEntryList.stream().filter(i -> getBinaryFromInt(i.getDealStatus()).equals("010")).count();
        reportChargeInfo.setQkcc(String.valueOf(qkcc));
        //欠款金额
        BigDecimal qkje = tbStateEntryList.stream()
                .map(i -> BigDecimal.valueOf(i.getTotalArrearage())).reduce(BigDecimal.ZERO, BigDecimal::add);
        reportChargeInfo.setQkje(qkje.stripTrailingZeros().toPlainString());
        //加收现金
        reportChargeInfo.setJsxj("0");
        //加收金额
        reportChargeInfo.setJsje("0");
        //加收合计
        reportChargeInfo.setJshj("0");
        //加收次数
        reportChargeInfo.setJscs("0");
        //移动支付
        BigDecimal ydzf = tbStateEntryList.stream().filter(i -> i.getPayType() == 16)
                .map(i -> BigDecimal.valueOf(i.getTotalToll())).reduce(BigDecimal.ZERO, BigDecimal::add);
        reportChargeInfo.setYdzf(ydzf.stripTrailingZeros().toPlainString());
        //电子支付消费额储值卡
        BigDecimal dzzfczk = tbStateEntryList.stream().filter(i -> i.getPayType() == 2 && i.getCardType() == 22)
                .map(i -> BigDecimal.valueOf(i.getTotalToll())).reduce(BigDecimal.ZERO, BigDecimal::add);
        reportChargeInfo.setDzczk(dzzfczk.stripTrailingZeros().toPlainString());
        //电子支付消费额记账卡
        BigDecimal dzzfjzk = tbStateEntryList.stream().filter(i -> i.getPayType() == 2 && i.getCardType() == 23)
                .map(i -> BigDecimal.valueOf(i.getTotalToll())).reduce(BigDecimal.ZERO, BigDecimal::add);
        reportChargeInfo.setDzjzk(dzzfjzk.stripTrailingZeros().toPlainString());
        //电子支付合计
        BigDecimal dzzfhj = dzzfczk.add(dzzfjzk);
        reportChargeInfo.setDzhj(dzzfhj.stripTrailingZeros().toPlainString());
//        //打印票据现金张数
//        reportChargeInfo.setXjpjzs("0");
//        //打印票据现金打票金额
//        reportChargeInfo.setXjdpje("0");
//        //打印票据移动支付张数
//        reportChargeInfo.setYdzfzs("0");
//        //打印票据移动支付金额
//        reportChargeInfo.setYdzfdpje("0");
//        //打印票据合计
//        reportChargeInfo.setDyphj("0");
//        //定额票据张数
//        reportChargeInfo.setDepjzs("0");
//        //定额票据金额
//        reportChargeInfo.setDepjje("0");
//        //废票张数
//        reportChargeInfo.setFpzs("0");
//        //废票金额
//        reportChargeInfo.setFpje("0");
        //公务IC卡
        long gwic = tbStateEntryList.stream().filter(i -> getBinaryFromInt(i.getDealStatus()).equals("011")).count();
        reportChargeInfo.setGwic(String.valueOf(gwic));
        //军车IC卡
        long jcic = tbStateEntryList.stream().filter(i -> getBinaryFromInt(i.getDealStatus()).equals("100")).count();
        reportChargeInfo.setJcic(String.valueOf(jcic));
        //免费IC卡
        long mfic = tbStateEntryList.stream().filter(i -> getBinaryFromInt(i.getDealStatus()).equals("110")).count();
        reportChargeInfo.setMfic(String.valueOf(mfic));
        //应缴IC卡
        long yjic = tbStateEntryList.stream().filter(i -> getBinaryFromInt(i.getDealStatus()).equals("000")).count();
        reportChargeInfo.setYjic(String.valueOf(yjic));
        //统计金额 = 应收款 +电子支付+移动支付
        BigDecimal tjje = ydzf.add(dzzfhj).add(yjje);
        reportChargeInfo.setTjje(tjje.stripTrailingZeros().toPlainString());
    }

    /**
     * 交款记录表数据mock
     */
    @Dbedge
    public void mockTbsh() {
        Random random = new Random();
        List<TbSh> tbShList = new ArrayList<>();
        //动态表名
        String formatDateTime = DateUtil.format(new Date(), DatePattern.SIMPLE_MONTH_PATTERN);
        Map<String, Object> map = new HashMap<>();
//        map.put(MybatisPlusTableNameHelper.TABLE_TIME, formatDateTime);
        map.put(MybatisPlusTableNameHelper.TABLE_TIME, "202411");
        MybatisPlusTableNameHelper.setRequestData(map);
        //mock当前日期的几个班次人员的交易记录数据
        //早班的
        for (int i = 0; i < 5; i++) {
            int operatorId = random.nextInt(2);
            if (operatorId == 0) {
                operatorId += 1;
            }
            TbSh tbSh = TbSh.builder()
                    .StationID(32010101)
                    .Network(1)
//                    .StaDate(Integer.parseInt(DateUtils.getDate().replaceAll("-", "")))
                    .StaDate(20241130)
                    .ShiftID(1)
                    .TeamID(1)
                    .OperatorID(operatorId)
                    .HandInCNum(random.nextInt(100))
                    .HandOutCNum(random.nextInt(100))
                    .HandToll(random.nextInt(1000))
                    .TicketNum(0)
                    .TicketFee(0)
                    .FTicketNum(0)
                    .FTicketFee(0)
                    .AddedToll(random.nextInt(50))
                    .CancelFlag(0)
                    .Transfermark(0)
                    .RecordTime(new Date())
                    .RecordOP(operatorId)
                    .Flag(1)
                    .build();
            tbShList.add(tbSh);
        }
        //中班的
        for (int i = 0; i < 5; i++) {
            int operatorId = random.nextInt(2);
            if (operatorId == 0) {
                operatorId += 1;
            }
            TbSh tbSh = TbSh.builder()
                    .StationID(32010101)
                    .Network(1)
//                    .StaDate(Integer.parseInt(DateUtils.getDate().replaceAll("-", "")))
                    .StaDate(20241130)
                    .ShiftID(2)
                    .TeamID(1)
                    .OperatorID(operatorId)
                    .HandInCNum(random.nextInt(100))
                    .HandOutCNum(random.nextInt(100))
                    .HandToll(random.nextInt(1000))
                    .TicketNum(0)
                    .TicketFee(0)
                    .FTicketNum(0)
                    .FTicketFee(0)
                    .AddedToll(random.nextInt(50))
                    .CancelFlag(0)
                    .Transfermark(0)
                    .RecordTime(new Date())
                    .RecordOP(operatorId)
                    .Flag(1)
                    .build();
            tbShList.add(tbSh);
        }
        //晚班
        for (int i = 0; i < 5; i++) {
            int operatorId = random.nextInt(2);
            if (operatorId == 0) {
                operatorId += 1;
            }
            TbSh tbSh = TbSh.builder()
                    .StationID(32010101)
                    .Network(1)
//                    .StaDate(Integer.parseInt(DateUtils.getDate().replaceAll("-", "")))
                    .StaDate(20241130)
                    .ShiftID(3)
                    .TeamID(1)
                    .OperatorID(operatorId)
                    .HandInCNum(random.nextInt(100))
                    .HandOutCNum(random.nextInt(100))
                    .HandToll(random.nextInt(1000))
                    .TicketNum(0)
                    .TicketFee(0)
                    .FTicketNum(0)
                    .FTicketFee(0)
                    .AddedToll(random.nextInt(50))
                    .CancelFlag(0)
                    .Transfermark(0)
                    .RecordTime(new Date())
                    .RecordOP(operatorId)
                    .Flag(1)
                    .build();
            tbShList.add(tbSh);
        }
        try {
            tbShList.forEach(i -> tbShMapper.insert(i));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MybatisPlusTableNameHelper.clear();
        }
    }
}
