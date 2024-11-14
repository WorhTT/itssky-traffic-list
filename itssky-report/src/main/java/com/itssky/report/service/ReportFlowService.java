package com.itssky.report.service;

import com.itssky.report.domain.ReportFlowInfo;
import com.itssky.report.domain.tbStateEntry;
import com.itssky.report.mapper.ReportFlowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        List<Map> stationCodeByIds = reportFlowMapper.getStationCodeByIds(stationIdSet);
        Map<Long, List<tbStateEntry>> tbStateEntryMapByStationId = tbStateEntryList.stream()
                .collect(Collectors.groupingBy(tbStateEntry::getStationID));
        for (Map.Entry<Long, List<tbStateEntry>> entry: tbStateEntryMapByStationId.entrySet()) {
            ReportFlowInfo reportFlowInfo = new ReportFlowInfo();
            Long stationId = entry.getKey();
            List<tbStateEntry> entryList = entry.getValue();
            if (!CollectionUtils.isEmpty(stationCodeByIds) &&
                    Objects.nonNull(stationCodeByIds.get(stationId.intValue()))) {
            }
        }
        return result;
    }
}
