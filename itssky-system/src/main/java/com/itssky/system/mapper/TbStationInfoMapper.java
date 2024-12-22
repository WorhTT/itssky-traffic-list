package com.itssky.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itssky.system.domain.TbStationInfo;

public interface TbStationInfoMapper extends BaseMapper<TbStationInfo> {
    int deleteByPrimaryKey(Integer id);

    int insert(TbStationInfo record);

    int insertSelective(TbStationInfo record);

    TbStationInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbStationInfo record);

    int updateByPrimaryKey(TbStationInfo record);
}