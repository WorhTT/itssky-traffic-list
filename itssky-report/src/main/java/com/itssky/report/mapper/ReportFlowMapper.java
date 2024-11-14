package com.itssky.report.mapper;

import com.itssky.report.domain.tbStateEntry;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ITSSKY
 */
public interface ReportFlowMapper {

    public List<tbStateEntry> getTbStateEntryList(int timeType);

    public List<Map> getStationCodeByIds(Set<Long> stationIdSet);
}
