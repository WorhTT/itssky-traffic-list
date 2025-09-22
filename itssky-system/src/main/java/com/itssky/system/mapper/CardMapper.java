package com.itssky.system.mapper;

import com.itssky.db.Dbstats;
import com.itssky.system.domain.dto.*;
import com.itssky.system.domain.vo.*;

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

    @Dbstats
    public List<Ccq2CardVo> ccq2(CardCcqDto dto);

    @Dbstats
    public List<Ccq3CardVo> ccq3(CardCcqDto dto);

    /**
     * 按条件获取卡库存表
     */
    @Dbstats
    public List<TbStcVo> getTbStcList(TbStcDto dto);

    @Dbstats
    public List<TbStcVo> getTbStcListV2(TbStcDtoV2 dto);
}
