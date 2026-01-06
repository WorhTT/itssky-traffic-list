package com.itssky.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itssky.common.annotation.DynamicTableName;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.utils.DateUtils;
import com.itssky.common.utils.MybatisPlusTableNameHelper;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.db.Dbedge;
import com.itssky.db.Dbstats;
import com.itssky.system.domain.*;
import com.itssky.system.domain.dto.CommonReportDto;
import com.itssky.system.domain.dto.FlowStatisticsDto;
import com.itssky.system.domain.dto.StationTimeDto;
import com.itssky.system.domain.vo.*;
import com.itssky.system.mapper.*;
import com.itssky.system.service.CardService;
import com.itssky.system.service.TbStationInfoService;
import com.itssky.util.TableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
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
    private TbStationInfoMapper tbStationInfoMapper;

    @Autowired
    private TbShMapper tbShMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private TbStationInfoService tbStationInfoService;

    public static final int ENTRY = 0;

    public static final int EXIT = 1;




    /**
     * 获取高速出口流量报表
     * flag 1入口 2出口
     */
    public List<ReportFlowInfo> getExitFlow(FlowStatisticsDto dto, int flag) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //判断用户的corpno
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        dto.setFlagStr(String.valueOf(flag));
        //构建会查询到的表集合
        String tablePrefix = null;
        if (flag == 1 || flag == 3) {
            //RSJ OR RSJ机器人
            tablePrefix = "tbstatentry";
        } else if (flag == 2 || flag == 4) {
            //CSJ OR CSJ机器人
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
        if (CollectionUtils.isEmpty(reportFlowInfos)) {
            return new ArrayList<>();
        }
        reportFlowInfos.forEach(r -> {
            r.setAllAmount(r.getKAmount() + r.getHAmount() + r.getZAmount());
        });
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

    public ExportVo getFlowExportVo(FlowStatisticsDto dto, int flag) {
        ExportVo exportVo = new ExportVo();
        List<ReportFlowInfo> list = getExitFlow(dto, flag);
        exportVo.setResult(list);
        exportVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return exportVo;
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

    public List<FlowYhVo> getFlowYh(FlowStatisticsDto dto) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbstatexit",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        if (CollectionUtils.isEmpty(dto.getTableNameList())) {
            return new ArrayList<>();
        }
        //时间传参格式化
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<FlowYhVo> result = reportFlowMapper.getFlowYh(dto);
        //设置统计方式
        result.forEach(r -> {
            if (dto.getStatisticsType().equals("0")) {
                r.setStatType(r.getStaDate().toString());
            } else if (dto.getStatisticsType().equals("1")) {
                r.setStatType(r.getMonthDate());
            } else if (dto.getStatisticsType().equals("2")) {
                r.setStatType(r.getStationName());
            }
        });
        FlowYhVo totalRow = new FlowYhVo();
        totalRow.setJzx(result.stream().map(i -> new BigDecimal(i.getJzx())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setYz(result.stream().map(i -> new BigDecimal(i.getYz())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setLs(result.stream().map(i -> new BigDecimal(i.getLs())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setKz(result.stream().map(i -> new BigDecimal(i.getKz())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setJg(result.stream().map(i -> new BigDecimal(i.getJg())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setYg(result.stream().map(i -> new BigDecimal(i.getYg())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setJc(result.stream().map(i -> new BigDecimal(i.getJc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZy(result.stream().map(i -> new BigDecimal(i.getZy())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setSgj(result.stream().map(i -> new BigDecimal(i.getSgj())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZobl(result.stream().map(i -> new BigDecimal(i.getZobl())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setYj(result.stream().map(i -> new BigDecimal(i.getYj())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setDjys(result.stream().map(i -> new BigDecimal(i.getDjys())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setStatType("合计");
        totalRow.setTotalRow(true);
        result.add(totalRow);
        return result;
    }

    /**
     * RJ、CJ
     * RJ入口(MTC)交通流量统计表 + CJ出口(MTC)交通流量统计表
     * @param dto
     * @return
     */
    public List<CRJFlowVo> getCRJFlow(FlowStatisticsDto dto) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //判断用户的corpno
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        if (dto.getFlag() == ENTRY) {
            dto.setTableNameList(
                    TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbstatentry",
                            DatePattern.SIMPLE_MONTH_PATTERN));
        } else if (dto.getFlag() == EXIT){
            dto.setTableNameList(
                    TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbstatexit",
                            DatePattern.SIMPLE_MONTH_PATTERN));
        }
        dto.setFlagStr(dto.getFlag().toString());

        if (CollectionUtils.isEmpty(dto.getTableNameList())) {
            return new ArrayList<>();
        }
        //时间传参格式化
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<CRJFlowVo> list = reportFlowMapper.getCRJFlow(dto);
        //计算比例
        list.forEach(item -> {
            //计算sumCount
            item.setSumCount(item.getHSum() + item.getKSum() + item.getZSum());
            BigDecimal kcbl = divideWithRounding(BigDecimal.valueOf(item.getKSum()), BigDecimal.valueOf(item.getSumCount()), 4);
            BigDecimal hcbl = divideWithRounding(BigDecimal.valueOf(item.getHSum()), BigDecimal.valueOf(item.getSumCount()), 4);
            BigDecimal zcbl = divideWithRounding(BigDecimal.valueOf(item.getZSum()), BigDecimal.valueOf(item.getSumCount()), 4);
            item.setKcbl(kcbl.doubleValue());
            item.setHcbl(hcbl.doubleValue());
            item.setZcbl(zcbl.doubleValue());
            if (dto.getStatisticsType().equals("0")) {
                item.setStatType(item.getStaDate().toString());
            } else if (dto.getStatisticsType().equals("1")) {
                item.setStatType(item.getMonthDate());
            } else if (dto.getStatisticsType().equals("2")) {
                item.setStatType(item.getStationName());
            }
        });
        //构建合计行
        CRJFlowVo totalRow = new CRJFlowVo();
        totalRow.setStatType("合计");
        totalRow.setTotalRow(true);
        totalRow.setK1(list.stream().map(i -> new BigDecimal(i.getK1())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK2(list.stream().map(i -> new BigDecimal(i.getK2())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK3(list.stream().map(i -> new BigDecimal(i.getK3())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK4(list.stream().map(i -> new BigDecimal(i.getK4())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setKSum(list.stream().map(i -> new BigDecimal(i.getKSum())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH1(list.stream().map(i -> new BigDecimal(i.getH1())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH2(list.stream().map(i -> new BigDecimal(i.getH2())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH3(list.stream().map(i -> new BigDecimal(i.getH3())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH4(list.stream().map(i -> new BigDecimal(i.getH4())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH5(list.stream().map(i -> new BigDecimal(i.getH5())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH6(list.stream().map(i -> new BigDecimal(i.getH6())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setHSum(list.stream().map(i -> new BigDecimal(i.getHSum())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ1(list.stream().map(i -> new BigDecimal(i.getZ1())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ2(list.stream().map(i -> new BigDecimal(i.getZ2())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ3(list.stream().map(i -> new BigDecimal(i.getZ3())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ4(list.stream().map(i -> new BigDecimal(i.getZ4())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ5(list.stream().map(i -> new BigDecimal(i.getZ5())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZ6(list.stream().map(i -> new BigDecimal(i.getZ6())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setZSum(list.stream().map(i -> new BigDecimal(i.getZSum())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setGw(list.stream().map(i -> new BigDecimal(i.getGw())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setJc(list.stream().map(i -> new BigDecimal(i.getJc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setYh(list.stream().map(i -> new BigDecimal(i.getYh())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setMf(list.stream().map(i -> new BigDecimal(i.getMf())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCd(list.stream().map(i -> new BigDecimal(i.getCd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setSumCount(list.stream().map(i -> new BigDecimal(i.getSumCount())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        BigDecimal kcbl = divideWithRounding(BigDecimal.valueOf(totalRow.getKSum()), BigDecimal.valueOf(totalRow.getSumCount()), 4);
        BigDecimal hcbl = divideWithRounding(BigDecimal.valueOf(totalRow.getHSum()), BigDecimal.valueOf(totalRow.getSumCount()), 4);
        BigDecimal zcbl = divideWithRounding(BigDecimal.valueOf(totalRow.getZSum()), BigDecimal.valueOf(totalRow.getSumCount()), 4);
        totalRow.setKcbl(kcbl.doubleValue());
        totalRow.setHcbl(hcbl.doubleValue());
        totalRow.setZcbl(zcbl.doubleValue());
        list.add(totalRow);
        return list;
    }

    private static BigDecimal divideWithRounding(BigDecimal dividend, BigDecimal divisor, int scale) {
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return dividend.divide(divisor, scale, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }

    /**
     * 1 -> MTC
     * 2 -> ETC+MTC
     */
    public List<TkFlowVo> tkFlow(FlowStatisticsDto dto, int flag) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        Set<String> tableNameList = new HashSet<>();

        List<String> entryTableNameList = TableUtil.generateMonthList(dto.getBeginTime(), dto.getEndTime(), DatePattern.SIMPLE_MONTH_PATTERN);
        if (!CollectionUtils.isEmpty(entryTableNameList)) {
            tableNameList.addAll(entryTableNameList);
        }
        List<String> exitTableNameList = TableUtil.generateMonthList(dto.getBeginTime(), dto.getEndTime(), DatePattern.SIMPLE_MONTH_PATTERN);
        if (!CollectionUtils.isEmpty(exitTableNameList)) {
            tableNameList.addAll(exitTableNameList);
        }
        if (CollectionUtils.isEmpty(tableNameList)) {
            return new ArrayList<>();
        }
        dto.setTableNameList(new ArrayList<>(tableNameList));
        //时间传参格式化
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<TkFlowVo> list = new ArrayList<>();
        if (flag == 1) {
            list = reportFlowMapper.getTkFlow(dto);
        } else if (flag == 2) {
            list = reportFlowMapper.getTkFlowAll(dto);
        }
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        list.forEach(item -> {
            //小计
            item.setRsum(item.getRxj() + item.getRdz());
            item.setCsum(item.getCxj() + item.getCdz() + item.getCyd());
            if ("0".equals(dto.getStatisticsType())) {
                item.setStatType(item.getStaDate());
            } else if ("1".equals(dto.getStatisticsType())) {
                item.setStatType(item.getMonthDate());
            } else if ("2".equals(dto.getStatisticsType())) {
                item.setStatType(item.getStationName());
            }
            //总计
            item.setSumCount(item.getRsum() + item.getCsum());
        });
        //合计
        TkFlowVo totalRow = new TkFlowVo();
        totalRow.setTotalRow(true);
        totalRow.setStatType("合计");
        totalRow.setSumCount(list.stream().map(i -> new BigDecimal(i.getSumCount())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setRxj(list.stream().map(i -> new BigDecimal(i.getRxj())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setRdz(list.stream().map(i -> new BigDecimal(i.getRdz())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setRgw(list.stream().map(i -> new BigDecimal(i.getRgw())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setRjc(list.stream().map(i -> new BigDecimal(i.getRjc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setRyh(list.stream().map(i -> new BigDecimal(i.getRyh())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setRcd(list.stream().map(i -> new BigDecimal(i.getRcd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setRsum(list.stream().map(i -> new BigDecimal(i.getRsum())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCxj(list.stream().map(i -> new BigDecimal(i.getCxj())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCdz(list.stream().map(i -> new BigDecimal(i.getCdz())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCyd(list.stream().map(i -> new BigDecimal(i.getCyd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCxj(list.stream().map(i -> new BigDecimal(i.getCxj())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCgw(list.stream().map(i -> new BigDecimal(i.getCgw())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCjc(list.stream().map(i -> new BigDecimal(i.getCjc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCyh(list.stream().map(i -> new BigDecimal(i.getCyh())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCmf(list.stream().map(i -> new BigDecimal(i.getCmf())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCcd(list.stream().map(i -> new BigDecimal(i.getCcd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCsum(list.stream().map(i -> new BigDecimal(i.getCsum())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        list.add(totalRow);
        return list;
    }

    public List<FlowGroupVo> erjs(FlowStatisticsDto dto) {
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), SecurityUtils.getLoginUser());
        //构建查询站点范围参数
        dto.setStationIdList(authRangeStationIdList);
        //构建查询时间范围参数
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        //构建查询表范围参数
        if ("1".equals(dto.getFlagStr()) || "2".equals(dto.getFlagStr())) {
            dto.setTableNameList(TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(),
                    "tbstatentry",DatePattern.SIMPLE_MONTH_PATTERN));
        } else if ("3".equals(dto.getFlagStr()) || "4".equals(dto.getFlagStr())) {
            dto.setTableNameList(TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(),
                    "tbstatexit",DatePattern.SIMPLE_MONTH_PATTERN));
        }
        List<FlowGroupVo> list = reportFlowMapper.getFlowGroup(dto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            list.forEach(item -> {
                if ("0".equals(dto.getStatisticsType())) {
                    item.setStatType(item.getStaDate());
                } else if ("1".equals(dto.getStatisticsType())) {
                    item.setStatType(item.getMonthDate());
                } else if ("2".equals(dto.getStatisticsType())) {
                    item.setStatType(item.getStationName());
                }
            });
            //添加合计行
            FlowGroupVo totalRow = new FlowGroupVo();
            totalRow.setTotalRow(true);
            totalRow.setStatType("合计");
            totalRow.setK1c(list.stream().map(i -> new BigDecimal(i.getK1c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setK1d(list.stream().map(i -> new BigDecimal(i.getK1d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setK2c(list.stream().map(i -> new BigDecimal(i.getK2c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setK2d(list.stream().map(i -> new BigDecimal(i.getK2d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setK3c(list.stream().map(i -> new BigDecimal(i.getK3c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setK3d(list.stream().map(i -> new BigDecimal(i.getK3d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setK4c(list.stream().map(i -> new BigDecimal(i.getK4c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setK4d(list.stream().map(i -> new BigDecimal(i.getK4d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setKAmountc(list.stream().map(i -> new BigDecimal(i.getKAmountc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setKAmountd(list.stream().map(i -> new BigDecimal(i.getKAmountd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH1c(list.stream().map(i -> new BigDecimal(i.getH1c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH1d(list.stream().map(i -> new BigDecimal(i.getH1d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH2c(list.stream().map(i -> new BigDecimal(i.getH2c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH2d(list.stream().map(i -> new BigDecimal(i.getH2d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH3c(list.stream().map(i -> new BigDecimal(i.getH3c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH3d(list.stream().map(i -> new BigDecimal(i.getH3d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH4c(list.stream().map(i -> new BigDecimal(i.getH4c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH4d(list.stream().map(i -> new BigDecimal(i.getH4d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH5c(list.stream().map(i -> new BigDecimal(i.getH5c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH5d(list.stream().map(i -> new BigDecimal(i.getH5d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH6c(list.stream().map(i -> new BigDecimal(i.getH6c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setH6d(list.stream().map(i -> new BigDecimal(i.getH6d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setHAmountc(list.stream().map(i -> new BigDecimal(i.getHAmountc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setHAmountd(list.stream().map(i -> new BigDecimal(i.getHAmountd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ1c(list.stream().map(i -> new BigDecimal(i.getZ1c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ1d(list.stream().map(i -> new BigDecimal(i.getZ1d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ2c(list.stream().map(i -> new BigDecimal(i.getZ2c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ2d(list.stream().map(i -> new BigDecimal(i.getZ2d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ3c(list.stream().map(i -> new BigDecimal(i.getZ3c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ3d(list.stream().map(i -> new BigDecimal(i.getZ3d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ4c(list.stream().map(i -> new BigDecimal(i.getZ4c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ4d(list.stream().map(i -> new BigDecimal(i.getZ4d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ5c(list.stream().map(i -> new BigDecimal(i.getZ5c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ5d(list.stream().map(i -> new BigDecimal(i.getZ5d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ6c(list.stream().map(i -> new BigDecimal(i.getZ6c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZ6d(list.stream().map(i -> new BigDecimal(i.getZ6d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZAmountc(list.stream().map(i -> new BigDecimal(i.getZAmountc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setZAmountd(list.stream().map(i -> new BigDecimal(i.getZAmountd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setAllAmountc(list.stream().map(i -> new BigDecimal(i.getAllAmountc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setAllAmountd(list.stream().map(i -> new BigDecimal(i.getAllAmountd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            totalRow.setTotal(list.stream().map(i -> new BigDecimal(i.getTotal())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
            list.add(totalRow);
        }
        return list;
    }

    public List<FlowVeClassVo> flowYjzz(FlowStatisticsDto dto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser.getCorpNo().length() != 2) {
            log.warn("当前用户权限不够访问中心级别报表");
            return new ArrayList<>();
        }
        dto.setTableNameList(TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbstatexit",
                DatePattern.SIMPLE_MONTH_PATTERN));
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        //苏州分中心及锡常分中心的数据
        List<FlowVeClassVo> list = reportFlowMapper.getFlowVeClass(dto);

        //沪苏浙分中心的数据
        List<FlowVeClassVo> list2 = reportFlowMapper.getFlowVeClassForOtherDatabase(dto);

        if (!CollectionUtils.isEmpty(list)) {
            if (!CollectionUtils.isEmpty(list2)) {
                list.addAll(list2);
            }
        } else {
            return new ArrayList<>();
        }
        //计算合计
        FlowVeClassVo totalRow = new FlowVeClassVo();
        totalRow.setTotalRow(true);
        totalRow.setStatType("合计");
        totalRow.setR1(list.stream().map(i -> new BigDecimal(i.getR1())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setR2(list.stream().map(i -> new BigDecimal(i.getR2())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setR3(list.stream().map(i -> new BigDecimal(i.getR3())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setR4(list.stream().map(i -> new BigDecimal(i.getR4())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setR5(list.stream().map(i -> new BigDecimal(i.getR5())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setR6(list.stream().map(i -> new BigDecimal(i.getR6())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setRzx(list.stream().map(i -> new BigDecimal(i.getRzx())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setRsum(list.stream().map(i -> new BigDecimal(i.getRsum())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setC1(list.stream().map(i -> new BigDecimal(i.getC1())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setC2(list.stream().map(i -> new BigDecimal(i.getC2())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setC3(list.stream().map(i -> new BigDecimal(i.getC3())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setC4(list.stream().map(i -> new BigDecimal(i.getC4())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setC5(list.stream().map(i -> new BigDecimal(i.getC5())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setC6(list.stream().map(i -> new BigDecimal(i.getC6())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCzx(list.stream().map(i -> new BigDecimal(i.getCzx())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setCsum(list.stream().map(i -> new BigDecimal(i.getCsum())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setAllSum(list.stream().map(i -> new BigDecimal(i.getAllSum())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        list.add(totalRow);
        return list;
    }

    public List<Ecs2Vo> ecs2(StationTimeDto dto) {
        dto.setStationId(dto.getStationId());
        if (Objects.isNull(dto.getTime())) {
            return new ArrayList<>();
        }
        int staDate = Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN));
        String tableName = "mtraffic" + String.valueOf(staDate).substring(0, 6);
        dto.setTableName(tableName);
        dto.setTimeFormat(staDate);
        List<Ecs2Vo> list = reportFlowMapper.ecs2(dto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        //增加合计行
        Ecs2Vo totalRow = new Ecs2Vo();
        totalRow.setTotalRow(true);
        totalRow.setTime("合计");
        totalRow.setK1c(list.stream().map(i -> new BigDecimal(i.getK1c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK1d(list.stream().map(i -> new BigDecimal(i.getK1d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK2c(list.stream().map(i -> new BigDecimal(i.getK2c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK2d(list.stream().map(i -> new BigDecimal(i.getK2d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK3c(list.stream().map(i -> new BigDecimal(i.getK3c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK3d(list.stream().map(i -> new BigDecimal(i.getK3d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK4c(list.stream().map(i -> new BigDecimal(i.getK4c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setK4d(list.stream().map(i -> new BigDecimal(i.getK4d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setKsumc(list.stream().map(i -> new BigDecimal(i.getKsumc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setKsumd(list.stream().map(i -> new BigDecimal(i.getKsumd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH1c(list.stream().map(i -> new BigDecimal(i.getH1c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH1d(list.stream().map(i -> new BigDecimal(i.getH1d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH2c(list.stream().map(i -> new BigDecimal(i.getH2c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH2d(list.stream().map(i -> new BigDecimal(i.getH2d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH3c(list.stream().map(i -> new BigDecimal(i.getH3c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH3d(list.stream().map(i -> new BigDecimal(i.getH3d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH4c(list.stream().map(i -> new BigDecimal(i.getH4c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH4d(list.stream().map(i -> new BigDecimal(i.getH4d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH5c(list.stream().map(i -> new BigDecimal(i.getH5c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH5d(list.stream().map(i -> new BigDecimal(i.getH5d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH6c(list.stream().map(i -> new BigDecimal(i.getH6c())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setH6d(list.stream().map(i -> new BigDecimal(i.getH6d())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setHsumc(list.stream().map(i -> new BigDecimal(i.getHsumc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setHsumd(list.stream().map(i -> new BigDecimal(i.getHsumd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setSumc(list.stream().map(i -> new BigDecimal(i.getSumc())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        totalRow.setSumd(list.stream().map(i -> new BigDecimal(i.getSumd())).reduce(BigDecimal.ZERO, BigDecimal::add).intValue());
        list.add(totalRow);
        return list;
    }


    public List<CtVo> ct(CommonReportDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Integer> authRangeStationIdList = tbStationInfoService.getAuthRangeStationIdList(dto.getStationId(), loginUser);
        dto.setStationIdList(authRangeStationIdList);
        int intBeginDate = Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN));
        int intEndDate = Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN));
        List<String> tableNameList = TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(),
                "mtraffic", DatePattern.SIMPLE_MONTH_PATTERN);
        if (CollectionUtils.isEmpty(tableNameList)) {
            return new ArrayList<>();
        }
        dto.setTableNameList(tableNameList);
        dto.setIntBeginTime(intBeginDate);
        dto.setIntEndTime(intEndDate);
        List<CtVo> list = reportFlowMapper.ct(dto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        list.forEach(r -> {
            if ("1".equals(dto.getStatType())) {
                r.setStatType(r.getStaDate());
            } else if ("2".equals(dto.getStatType())) {
                r.setStatType(r.getMonthDate());
            } else if ("3".equals(dto.getStatType())) {
                r.setStatType(r.getStationName());
            }
            //客车比例
            r.setKbl(divideWithRounding(new BigDecimal(r.getKsum()), new BigDecimal(r.getTotal()), 2));
            //货车比例
            r.setHbl(divideWithRounding(new BigDecimal(r.getHsum()), new BigDecimal(r.getTotal()), 2));
            //专车比例
            r.setZbl(divideWithRounding(new BigDecimal(r.getZsum()), new BigDecimal(r.getTotal()), 2));
        });
        //添加合计行
        CtVo totalRow = new CtVo();
        totalRow.setStatType("合计");
        totalRow.setTotalRow(true);
        totalRow.setK1(list.stream().map(i -> i.getK1()).reduce(0, Integer::sum));
        totalRow.setK2(list.stream().map(i -> i.getK2()).reduce(0, Integer::sum));
        totalRow.setK3(list.stream().map(i -> i.getK3()).reduce(0, Integer::sum));
        totalRow.setK4(list.stream().map(i -> i.getK4()).reduce(0, Integer::sum));
        totalRow.setKsum(list.stream().map(i -> i.getKsum()).reduce(0, Integer::sum));
        totalRow.setH1(list.stream().map(i -> i.getH1()).reduce(0, Integer::sum));
        totalRow.setH2(list.stream().map(i -> i.getH2()).reduce(0, Integer::sum));
        totalRow.setH3(list.stream().map(i -> i.getH3()).reduce(0, Integer::sum));
        totalRow.setH4(list.stream().map(i -> i.getH4()).reduce(0, Integer::sum));
        totalRow.setH5(list.stream().map(i -> i.getH5()).reduce(0, Integer::sum));
        totalRow.setH6(list.stream().map(i -> i.getH6()).reduce(0, Integer::sum));
        totalRow.setHsum(list.stream().map(i -> i.getHsum()).reduce(0, Integer::sum));
        totalRow.setZ1(list.stream().map(i -> i.getZ1()).reduce(0, Integer::sum));
        totalRow.setZ2(list.stream().map(i -> i.getZ2()).reduce(0, Integer::sum));
        totalRow.setZ3(list.stream().map(i -> i.getZ3()).reduce(0, Integer::sum));
        totalRow.setZ4(list.stream().map(i -> i.getZ4()).reduce(0, Integer::sum));
        totalRow.setZ5(list.stream().map(i -> i.getZ5()).reduce(0, Integer::sum));
        totalRow.setZ6(list.stream().map(i -> i.getZ6()).reduce(0, Integer::sum));
        totalRow.setZsum(list.stream().map(i -> i.getZsum()).reduce(0, Integer::sum));
        totalRow.setTotal(list.stream().map(i -> i.getTotal()).reduce(0, Integer::sum));
        totalRow.setKbl(divideWithRounding(new BigDecimal(totalRow.getKsum()), new BigDecimal(totalRow.getTotal()), 2));
        totalRow.setHbl(divideWithRounding(new BigDecimal(totalRow.getHsum()), new BigDecimal(totalRow.getTotal()), 2));
        totalRow.setZbl(divideWithRounding(new BigDecimal(totalRow.getZsum()), new BigDecimal(totalRow.getTotal()), 2));
        list.add(totalRow);
        return list;
    }
}
