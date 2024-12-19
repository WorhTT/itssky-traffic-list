package com.itssky.system.service;

import com.itssky.system.domain.dto.CardStatisticsDto;
import com.itssky.system.domain.dto.CardStatisticsDtoV2;
import com.itssky.system.domain.vo.CardStatisticsVo;
import com.itssky.system.domain.vo.CdtStatisticsVo;

import java.util.List;

/**
 * @author ITSSKY
 */
public interface CardService {

    public List<CardStatisticsVo> s1StationShift(CardStatisticsDto dto);

    public List<CardStatisticsVo> s2StationShift(CardStatisticsDto dto);

    public List<CardStatisticsVo> sdtStationShift(CardStatisticsDtoV2 dto);

    public List<CdtStatisticsVo> cdtCardRecycle(CardStatisticsDtoV2 dto);
}
