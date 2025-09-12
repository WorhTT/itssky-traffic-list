package com.itssky.system.service;

import com.itssky.system.domain.dto.CxczDto;
import com.itssky.system.domain.dto.GreenDto;
import com.itssky.system.domain.dto.UnUseEtcDto;
import com.itssky.system.domain.vo.CxczVo;
import com.itssky.system.domain.vo.GreenVo;
import com.itssky.system.domain.vo.UnUseEtcVo;

import java.util.List;

public interface ISpecialService {

    /**
     * 绿优台账
     */
    public List<GreenVo> greenTable(GreenDto greenDto);

    /**
     * 入口超限操作明细表
     */
    public List<CxczVo> cxczTable(CxczDto dto);

    /**
     * 天扬高速定制报表
     * 非ETC车辆开票数统计
     */
    public List<UnUseEtcVo> unuseEtcTable(UnUseEtcDto dto);
}
