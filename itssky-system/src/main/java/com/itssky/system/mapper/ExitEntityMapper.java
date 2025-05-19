package com.itssky.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itssky.db.ItsData;
import com.itssky.system.domain.ExitEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ExitEntityMapper extends BaseMapper<ExitEntity> {

    @ItsData
    List<ExitEntity> selectGpExitList(ExitEntity form);
}