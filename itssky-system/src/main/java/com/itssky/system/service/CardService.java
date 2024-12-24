package com.itssky.system.service;

import com.itssky.system.domain.dto.CardStatisticsDto;
import com.itssky.system.domain.dto.CardStatisticsDtoV2;
import com.itssky.system.domain.vo.*;

import java.util.Date;
import java.util.List;

/**
 * @author ITSSKY
 */
public interface CardService {

    public List<CardStatisticsVo> s1StationShift(CardStatisticsDto dto);

    public ExportVo getSCardStationShift(CardStatisticsDto dto);
    public ExportVo getCCardStationShift(CardStatisticsDto dto);

    public ExportVo getSCardStationDay(CardStatisticsDto dto);
    public ExportVo getCCardStationDay(CardStatisticsDto dto);

    public List<CardStatisticsVo> s2StationShift(CardStatisticsDto dto);

    public List<CardStatisticsVo> sdtStationShift(CardStatisticsDtoV2 dto);

    public ExportVo getSdtStationShift(CardStatisticsDtoV2 dto);

    public List<CdtStatisticsVo> cdtCardRecycle(CardStatisticsDtoV2 dto);

    public ExportVo getCdtStationShift(CardStatisticsDtoV2 dto);

    public List<String> buildConditionList(Integer stationId, Date time, Integer shiftId);

    public List<String> buildConditionList(Integer stationId, Date time);

    public List<String> buildConditionList(Integer stationId, Date beginTime, Date endTime);

}
