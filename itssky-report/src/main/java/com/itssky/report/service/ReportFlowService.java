package com.itssky.report.service;

import com.itssky.common.utils.DateUtils;
import com.itssky.report.domain.*;
import com.itssky.report.mapper.ReportFlowMapper;
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

    /**
     * 获取高速入口流量报表
     */
    public List<ReportFlowInfo> getEntryFlow() {
        List<ReportFlowInfo> result = new ArrayList<>();
        List<tbStateEntry> tbStateEntryList = reportFlowMapper.getTbStateEntryList(0);
        if (CollectionUtils.isEmpty(tbStateEntryList)) {
            log.error("入口班次统计列表查询为空 请联系管理员");
            return result;
        }
        Set<Long> stationIdSet = tbStateEntryList.stream()
                .map(tbStateEntry::getStationID).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(stationIdSet)) {
            log.error("站点编码列表查询为空 请联系管理员");
            return result;
        }
        List<StationCode> stationCodeByIds = reportFlowMapper.getStationCodeByIds(stationIdSet);
        Map<Long, String> stationCodeMap = stationCodeByIds.stream()
                .collect(Collectors.toMap(StationCode::getStationid, StationCode::getStationname, (m, n) -> m));
        Set<Integer> vehicleClassSet = tbStateEntryList.stream()
                .map(tbStateEntry::getVehicleClass).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(vehicleClassSet)) {
            log.error("车型列表查询为空 请联系管理员");
            return result;
        }
        Map<Long, List<tbStateEntry>> tbStateEntryMapByStationId = tbStateEntryList.stream()
                .collect(Collectors.groupingBy(tbStateEntry::getStationID));
        for (Map.Entry<Long, List<tbStateEntry>> entry : tbStateEntryMapByStationId.entrySet()) {
            ReportFlowInfo reportFlowInfo = new ReportFlowInfo();
            Long stationId = entry.getKey();
            if (!CollectionUtils.isEmpty(stationCodeMap) && Objects.nonNull(stationCodeMap.get(stationId))) {
                String stationName = stationCodeMap.get(stationId);
                reportFlowInfo.setStationName(stationName);
            } else {
                continue;
            }
            List<tbStateEntry> entryList = entry.getValue();
            fillEntryFlowReport(reportFlowInfo, entryList);
            result.add(reportFlowInfo);
        }
        ReportFlowInfo amountReportFlowInfo = new ReportFlowInfo();
        amountReportFlowInfo.setStationName("合计");
        fillEntryFlowReport(amountReportFlowInfo, tbStateEntryList);
        result.add(amountReportFlowInfo);
        return result;
    }

    /**
     * 获取高速出口流量报表
     */
    public List<ReportFlowInfo> getExitFlow() {
        List<ReportFlowInfo> result = new ArrayList<>();
        List<tbStateEntry> tbStateEntryList = reportFlowMapper.getTbStateExitList(0);
        if (CollectionUtils.isEmpty(tbStateEntryList)) {
            log.error("入口班次统计列表查询为空 请联系管理员");
            return result;
        }
        Set<Long> stationIdSet = tbStateEntryList.stream()
                .map(tbStateEntry::getStationID).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(stationIdSet)) {
            log.error("站点编码列表查询为空 请联系管理员");
            return result;
        }
        List<StationCode> stationCodeByIds = reportFlowMapper.getStationCodeByIds(stationIdSet);
        Map<Long, String> stationCodeMap = stationCodeByIds.stream()
                .collect(Collectors.toMap(StationCode::getStationid, StationCode::getStationname, (m, n) -> m));
        Set<Integer> vehicleClassSet = tbStateEntryList.stream()
                .map(tbStateEntry::getVehicleClass).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(vehicleClassSet)) {
            log.error("车型列表查询为空 请联系管理员");
            return result;
        }
        Map<Long, List<tbStateEntry>> tbStateEntryMapByStationId = tbStateEntryList.stream()
                .collect(Collectors.groupingBy(tbStateEntry::getStationID));
        for (Map.Entry<Long, List<tbStateEntry>> entry : tbStateEntryMapByStationId.entrySet()) {
            ReportFlowInfo reportFlowInfo = new ReportFlowInfo();
            Long stationId = entry.getKey();
            if (!CollectionUtils.isEmpty(stationCodeMap) && Objects.nonNull(stationCodeMap.get(stationId))) {
                String stationName = stationCodeMap.get(stationId);
                reportFlowInfo.setStationName(stationName);
            } else {
                continue;
            }
            List<tbStateEntry> entryList = entry.getValue();
            fillEntryFlowReport(reportFlowInfo, entryList);
            result.add(reportFlowInfo);
        }
        ReportFlowInfo amountReportFlowInfo = new ReportFlowInfo();
        amountReportFlowInfo.setStationName("合计");
        fillEntryFlowReport(amountReportFlowInfo, tbStateEntryList);
        result.add(amountReportFlowInfo);
        return result;
    }

    /**
     * 填充报表中的主要内容
     */
    private void fillEntryFlowReport(ReportFlowInfo reportFlowInfo, List<tbStateEntry> tbStateEntryList) {
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
    }

    public void mockData() {
        List<tbStateEntry> tbStateEntryList = new ArrayList<>();
        //随机生成1000条数据
        Random random = new Random();
        List<StationCode> allStationCode = reportFlowMapper.getAllStationCode();
        List<VehicleClass> allVehicleClass = reportFlowMapper.getAllVehicleClass();
        int size = allStationCode.size();
        int vehicleClassListSize = allVehicleClass.size();
        for (int i = 0; i < 5; i++) {
            StationCode stationCode = allStationCode.get(random.nextInt(size - 1));
            for (int j = 0; j < 300; j++) {
                tbStateEntry tbStateEntry = new tbStateEntry();
                tbStateEntry.setVehicleClass(allVehicleClass.get(random.nextInt(vehicleClassListSize)).getVehicleclass());
                tbStateEntry.setBalanceOP(1);
                tbStateEntry.setBalanceTime(DateUtils.getNowDate());
                tbStateEntry.setCancelFlag(1);
                String binary = generateBinary();
                BigInteger bigInteger = new BigInteger(binary, 2);
                System.out.println(bigInteger);
                tbStateEntry.setDealStatus(bigInteger.intValue());
                tbStateEntry.setCardNum(random.nextInt(500));
                tbStateEntry.setLaneID(1);
                tbStateEntry.setSpare(1);
                tbStateEntry.setOperatorID(1L);
                tbStateEntry.setStaDate(20241114);
                tbStateEntry.setShiftID(1);
                tbStateEntry.setStationID(stationCode.getStationid());
                tbStateEntry.setCarNum(random.nextInt(500));
                tbStateEntry.setLaneID(1);
                tbStateEntry.setTransfermark(1);
                tbStateEntry.setCardType(0);
                tbStateEntry.setTotalArrearage(0D);
                //生成现金收费数据
//                tbStateEntry.setPayType(0);
//                int i1 = random.nextInt(100);
//                tbStateEntry.setTotalToll((double) i1);
//                tbStateEntry.setTotalFee((double) i1);
                //生成非现金收费数据
//                tbStateEntry.setPayType(2);
//                int cardType = random.nextInt(23 - 22 + 1) + 22;
//                tbStateEntry.setCardType(cardType);
//                Double generateDouble = generateDouble();
//                tbStateEntry.setTotalToll(generateDouble);
//                tbStateEntry.setTotalFee(generateDouble);
                //生成移动支付收费数据
                tbStateEntry.setPayType(16);
                int i1 = random.nextInt(100);
                tbStateEntry.setTotalToll((double) i1);
                tbStateEntry.setTotalFee((double) i1);
                tbStateEntryList.add(tbStateEntry);
            }
        }
        tbStateEntryList.forEach(i -> reportFlowMapper.insertTbStateExit(i));
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
        String s = generateBinary();// 生成和打印所有组合
        BigInteger i = new BigInteger(s, 2);
        String binaryFromInt = getBinaryFromInt(i.intValue());
        System.out.println(binaryFromInt);
        //获取123位
        String s2 = Integer.toBinaryString(i.intValue());
        String s1 = i.toString(2);
        String substring = s1.substring(1, 4);
        System.out.println(s);
        System.out.println(i);
        System.out.println(s2);
        System.out.println(s1);
        System.out.println(substring);
    }

    /**
     * 获取高速收费报表
     */
    public List<ReportChargeInfo> getCharge() {
        List<ReportChargeInfo> result = new ArrayList<>();
        List<tbStateEntry> tbStateExitList = reportFlowMapper.getTbStateExitList(0);
        if (CollectionUtils.isEmpty(tbStateExitList)) {
            log.error("入口班次统计列表查询为空 请联系管理员");
            return result;
        }
        Set<Long> stationIdSet = tbStateExitList.stream()
                .map(tbStateEntry::getStationID).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(stationIdSet)) {
            log.error("站点编码列表查询为空 请联系管理员");
            return result;
        }
        List<StationCode> stationCodeByIds = reportFlowMapper.getStationCodeByIds(stationIdSet);
        Map<Long, String> stationCodeMap = stationCodeByIds.stream()
                .collect(Collectors.toMap(StationCode::getStationid, StationCode::getStationname, (m, n) -> m));
        Map<Long, List<tbStateEntry>> tbStateEntryMapByStationId = tbStateExitList.stream()
                .collect(Collectors.groupingBy(tbStateEntry::getStationID));
        for (Map.Entry<Long, List<tbStateEntry>> entry : tbStateEntryMapByStationId.entrySet()) {
            Long stationId = entry.getKey();
            ReportChargeInfo reportChargeInfo = new ReportChargeInfo();
            if (!CollectionUtils.isEmpty(stationCodeMap) && Objects.nonNull(stationCodeMap.get(stationId))) {
                String stationName = stationCodeMap.get(stationId);
                reportChargeInfo.setStationName(stationName);
            } else {
                continue;
            }
            List<tbStateEntry> entryList = entry.getValue();
            fillChargeInfo(entryList, reportChargeInfo);
            result.add(reportChargeInfo);
        }
        return result;
    }

    private void fillChargeInfo(List<tbStateEntry> tbStateEntryList, ReportChargeInfo reportChargeInfo) {
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
        //打印票据现金张数
        reportChargeInfo.setXjpjzs("0");
        //打印票据现金打票金额
        reportChargeInfo.setXjdpje("0");
        //打印票据移动支付张数
        reportChargeInfo.setYdzfzs("0");
        //打印票据移动支付金额
        reportChargeInfo.setYdzfdpje("0");
        //打印票据合计
        reportChargeInfo.setDyphj("0");
        //定额票据张数
        reportChargeInfo.setDepjzs("0");
        //定额票据金额
        reportChargeInfo.setDepjje("0");
        //废票张数
        reportChargeInfo.setFpzs("0");
        //废票金额
        reportChargeInfo.setFpje("0");
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
}
