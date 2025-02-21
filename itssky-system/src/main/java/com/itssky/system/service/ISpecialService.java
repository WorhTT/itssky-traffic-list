package com.itssky.system.service;

import com.itssky.system.domain.dto.GreenDto;
import com.itssky.system.domain.vo.GreenVo;

import java.util.List;

public interface ISpecialService {

    /**
     * 绿优台账
     */
    public List<GreenVo> greenTable(GreenDto greenDto);
}
