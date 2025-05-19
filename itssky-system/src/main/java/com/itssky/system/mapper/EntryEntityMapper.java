package com.itssky.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itssky.system.domain.EntryEntity;
import com.itssky.system.domain.ExitEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface EntryEntityMapper extends BaseMapper<EntryEntity> {

    List<EntryEntity> selectGpEntryList(EntryEntity form);

}