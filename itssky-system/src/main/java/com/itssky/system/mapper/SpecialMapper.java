package com.itssky.system.mapper;

import com.itssky.db.Dbstats;
import com.itssky.db.ItsData;
import com.itssky.system.domain.dto.CxczDto;
import com.itssky.system.domain.dto.GreenDto;
import com.itssky.system.domain.vo.CxczVo;
import com.itssky.system.domain.vo.GreenVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SpecialMapper {

    @ItsData
    public List<GreenVo> greenTable(GreenDto greenDto);

    public List<Map> buildOperatorName(@Param(value = "operatorIds") Set<Integer> operatorIds);

    @ItsData
    public List<CxczVo> cxczTable(CxczDto dto);

}
