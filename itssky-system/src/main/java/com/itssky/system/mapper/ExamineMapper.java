package com.itssky.system.mapper;

import com.itssky.db.Dbedge;
import com.itssky.db.Dbstats;
import com.itssky.db.ItsData;
import com.itssky.system.domain.*;
import com.itssky.system.domain.dto.FD06Dto;
import com.itssky.system.domain.dto.FD26Dto;
import com.itssky.system.domain.dto.FD27Dto;
import com.itssky.system.domain.dto.FlowStatisticsDto;
import com.itssky.system.domain.vo.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ITSSKY
 */
@Dbstats
public interface ExamineMapper {

    public List<FD06Vo> getFd06(FD06Dto dto);

    @Dbedge
    public List<FD07Vo> getTbSh(FD06Dto dto);

    public List<FD07Vo> getFd07(FD06Dto dto);

    @Dbedge
    public List<FD27Vo> getFd27(FD27Dto dto);

    @Dbedge
    public List<FD29Vo> getFd29(FD06Dto dto);

    @Dbedge
    public List<FD26Vo> getFd26(FD26Dto dto);
}
