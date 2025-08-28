package com.itssky.system.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itssky.system.domain.EntryEntity;
import com.itssky.system.domain.ExitEntity;
import com.itssky.system.domain.GpResultEntity;
import com.itssky.system.domain.GpVehicleEntity;
import com.itssky.system.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务Service
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TaskServiceImpl {

//    @Value("${fft.url}")
//    private String fftUrl;
//
//    @Value("${fft.binfile-auth}")
//    private String binfileAuth;
//
//    @Value(("${fft.corpno}"))
//    private String corpno;

//    private final TbStationInfoMapper stationInfoMapper;
//
//    private final ExitEntityMapper exitEntityMapper;
//
//    private final EntryEntityMapper entryEntityMapper;
//
//
//    @Scheduled(cron = "0 0 1 * * ?")
//    public void handleGpVehicle() {
//        //TODO: 获取前一天的所有过车数据 入出口 过滤重复车牌 根据DealStatus判断是否高频车
//        long yesterdayTimeMills = new Date().getTime() - 8640000;
//        Date yesterdayDate = new Date(yesterdayTimeMills);
//        String dateFormat = DateUtil.format(yesterdayDate, "yyyyMMdd");
//        ExitEntity exitForm = new ExitEntity();
//        exitForm.setExitDate(Integer.parseInt(dateFormat));
//        //昨日出口高频
//        List<ExitEntity> gpExitList = exitEntityMapper.selectGpExitList(exitForm);
//        if (CollectionUtils.isEmpty(gpExitList)) {
//            log.warn("当日出口高频数据为空, Date:{}", dateFormat);
//            return;
//        }
//        Set<String> stationHexSet = gpExitList.stream().map(ExitEntity::getHexStationID).collect(Collectors.toSet());
//        //昨日入口高频
//        EntryEntity entryForm = new EntryEntity();
//        entryForm.setEntryDate(Integer.parseInt(dateFormat));
//        List<EntryEntity> gpEntryList = entryEntityMapper.selectGpEntryList(entryForm);
//        if (CollectionUtils.isEmpty(gpEntryList)) {
//            log.warn("当日入口高频数据为空, Date:{}", dateFormat);
//        }
//        //合并高频数据
//        List<GpVehicleEntity> gpVehicleEntityList = new ArrayList<>();
//        gpExitList.forEach(item -> {
//            GpVehicleEntity gpVehicleEntity = new GpVehicleEntity();
//            gpVehicleEntity.setVehicleLicense(item.getVehicleLicense());
//            gpVehicleEntity.setLaneId(Integer.parseInt(item.getLaneID().toString()));
//            gpVehicleEntity.setShiftId(Integer.parseInt(item.getShiftID().toString()));
//            gpVehicleEntity.setStationId(item.getStationID());
//            gpVehicleEntity.setTradeDate(item.getExitDate());
//            gpVehicleEntity.setTradeTime(item.getExitTime());
//            gpVehicleEntity.setOperatorId(item.getOperatorID());
//            gpVehicleEntity.setHexStationId(item.getHexStationID());
//            gpVehicleEntityList.add(gpVehicleEntity);
//        });
//        gpEntryList.forEach(item -> {
//            GpVehicleEntity gpVehicleEntity = new GpVehicleEntity();
//            gpVehicleEntity.setVehicleLicense(item.getVehicleLicense());
//            gpVehicleEntity.setLaneId(Integer.parseInt(item.getLaneID().toString()));
//            gpVehicleEntity.setShiftId(Integer.parseInt(item.getShiftID().toString()));
//            gpVehicleEntity.setStationId(item.getStationID());
//            gpVehicleEntity.setTradeDate(item.getEntryDate());
//            gpVehicleEntity.setTradeTime(item.getEntryTime());
//            gpVehicleEntity.setOperatorId(item.getOperatorID());
//            gpVehicleEntity.setHexStationId(item.getHexStationID());
//            gpVehicleEntityList.add(gpVehicleEntity);
//        });
//        Map<String, GpVehicleEntity> mergedMap = new HashMap<>();
//        gpVehicleEntityList.forEach(item -> mergedMap.put(item.getVehicleLicense(), item));
//        gpVehicleEntityList.forEach(entry ->
//                mergedMap.putIfAbsent(entry.getVehicleLicense(), entry)
//        );
//        // 过滤后高频车列表
//        List<GpVehicleEntity> fileterGpVehicleList = new ArrayList<>(mergedMap.values());
//
//        //TODO: 调用FFT获取高频车办理明细
//        for (String stationHex : stationHexSet) {
//            JSONObject jsonObject = new JSONObject();
//            String result = HttpUtil.createPost(fftUrl + "/fft/gateway/queryHighRateCarInputInfo").contentType("application/json")
//                    .header("binfile-auth", binfileAuth).body(jsonObject.toJSONString())
//                    .addRequestInterceptor((obj) -> log.info(obj.toString())).execute().body();
//            log.info("/fft/gateway/queryEntryInfo调用结果:{}", result);
//            if (StrUtil.isBlank(result)) {
//                log.error("请求高频车明细数据失败: params:{}", jsonObject.toJSONString());
//            }
//            JSONObject resultJson = JSON.parseObject(result);
//            if (resultJson.containsKey("result") && resultJson.getInteger("result") == 200) {
//                // 直接解析 JSON 字符串为对象列表
//                try {
//                    List<GpResultEntity> gpResultList = JSONArray.parseArray(
//                            JSONObject.toJSONString(resultJson.getJSONArray("data")), GpResultEntity.class);
//                } catch (Exception e) {
//                    log.error("JSON解析错误");
//                }
//            } else {
//                log.error("请求高频车明细数据失败: message:{}", resultJson.getString("messageinfo"));
//            }
//        }
//        //TODO: 整理入库
//    }
}
