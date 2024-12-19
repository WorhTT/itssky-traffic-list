package com.itssky.system.mapper;

import com.itssky.db.Dbstats;
import com.itssky.system.domain.*;
import com.itssky.system.domain.dto.FlowStatisticsDto;
import com.itssky.system.domain.dto.StationShiftDto;
import com.itssky.system.domain.vo.StationShiftVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ITSSKY
 */
@Dbstats
public interface ReportFlowMapper {

    public List<TbStateEntry> getTbStateEntryList(int timeType);

    public List<TbStateExit> getTbStateExitList(int timeType);

    public List<StationCode> getStationCodeByIds(Set<Integer> stationIdSet);

    public List<StationCode> getAllStationCode();

    public List<VehicleClass> getVehicleClassByIds(Set<Integer> vehicleClassSet);

    public List<VehicleClass> getAllVehicleClass();

    public int insertTbStateEntry(TbStateEntry tbStateEntry);

    public int insertTbStateExit(TbStateExit tbStateExit);

    public List<ReportFlowInfo> csjFlow(FlowStatisticsDto dto);
}
