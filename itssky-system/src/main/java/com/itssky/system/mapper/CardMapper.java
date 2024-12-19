package com.itssky.system.mapper;

import com.itssky.db.Dbstats;
import com.itssky.system.domain.dto.CardStatisticsDto;
import com.itssky.system.domain.dto.CardStatisticsDtoV2;
import com.itssky.system.domain.dto.TbStcDto;
import com.itssky.system.domain.dto.TbStcDtoV2;
import com.itssky.system.domain.vo.CardStatisticsVo;
import com.itssky.system.domain.vo.CdtStatisticsVo;
import com.itssky.system.domain.vo.TbStcVo;

import java.util.List;

/**
 * @author ITSSKY
 */
public interface CardMapper {

    @Dbstats
    public List<CardStatisticsVo> s1StationShift(CardStatisticsDto dto);

    @Dbstats
    public List<CardStatisticsVo> c1StationShift(CardStatisticsDto dto);

    @Dbstats
    public List<CardStatisticsVo> sdtStationShift(CardStatisticsDtoV2 dto);

    @Dbstats
    public List<CdtStatisticsVo> cdtStationShift(CardStatisticsDtoV2 dto);

    /**
     * 按条件获取卡库存表
     */
    @Dbstats
    public List<TbStcVo> getTbStcList(TbStcDto dto);

    @Dbstats
    public List<TbStcVo> getTbStcListV2(TbStcDtoV2 dto);
}
