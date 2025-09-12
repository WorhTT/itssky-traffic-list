package com.itssky.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itssky.system.domain.TbCorpInfo;

import java.util.List;

public interface TbCorpInfoMapper extends BaseMapper<TbCorpInfo> {

    public List<TbCorpInfo> selectTbCorpInfoList(TbCorpInfo tbCorpInfo);

    public List<TbCorpInfo> selectTbCorpInfoListForTree(TbCorpInfo tbCorpInfo);
}