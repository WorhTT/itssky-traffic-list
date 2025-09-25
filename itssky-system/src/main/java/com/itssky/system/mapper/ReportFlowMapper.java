package com.itssky.system.mapper;

import com.itssky.db.Dbstats;
import com.itssky.db.Dbstats2;
import com.itssky.system.domain.*;
import com.itssky.system.domain.dto.FlowStatisticsDto;
import com.itssky.system.domain.vo.*;

import java.util.List;
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

    public List<FlowYhVo> getFlowYh(FlowStatisticsDto dto);

    public List<CRJFlowVo> getCRJFlow(FlowStatisticsDto dto);

    public List<TkFlowVo> getTkFlow(FlowStatisticsDto dto);

    public List<TkFlowVo> getTkFlowAll(FlowStatisticsDto dto);

    public List<FlowGroupVo> getFlowGroup(FlowStatisticsDto dto);

    public List<FlowVeClassVo> getFlowVeClass(FlowStatisticsDto dto);

    @Dbstats2
    public List<FlowVeClassVo> getFlowVeClassForOtherDatabase(FlowStatisticsDto dto);



//    public List<FlowGroupVo> getErjs(FlowStatisticsDto dto);
}
