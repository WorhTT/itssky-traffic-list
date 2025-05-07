package com.itssky.system.mapper;

import com.itssky.db.Dbstats;
import com.itssky.db.ItsData;
import com.itssky.system.domain.*;
import com.itssky.system.domain.dto.FD06Dto;
import com.itssky.system.domain.dto.FlowStatisticsDto;
import com.itssky.system.domain.vo.FD06Vo;

import java.util.List;
import java.util.Set;

/**
 * @author ITSSKY
 */
@Dbstats
public interface ExamineMapper {

    public List<FD06Vo> getFd06(FD06Dto dto);
}
