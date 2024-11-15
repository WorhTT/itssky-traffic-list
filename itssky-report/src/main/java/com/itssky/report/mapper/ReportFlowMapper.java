package com.itssky.report.mapper;

import com.itssky.report.domain.StationCode;
import com.itssky.report.domain.VehicleClass;
import com.itssky.report.domain.tbStateEntry;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ITSSKY
 */
public interface ReportFlowMapper {

    public List<tbStateEntry> getTbStateEntryList(int timeType);

    public List<tbStateEntry> getTbStateExitList(int timeType);

    public List<StationCode> getStationCodeByIds(Set<Long> stationIdSet);

    public List<StationCode> getAllStationCode();

    public List<VehicleClass> getVehicleClassByIds(Set<Integer> vehicleClassSet);

    public List<VehicleClass> getAllVehicleClass();

    public int insertTbStateEntry(tbStateEntry tbStateEntry);

    public int insertTbStateExit(tbStateEntry tbStateEntry);
}
