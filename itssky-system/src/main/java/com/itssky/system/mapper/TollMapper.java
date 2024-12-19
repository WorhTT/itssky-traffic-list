package com.itssky.system.mapper;


import com.itssky.db.Dbedge;
import com.itssky.db.Dbstats;
import com.itssky.system.domain.vo.EPayTollStatVo;
import com.itssky.system.domain.vo.VehicleClassStatVo;
import com.itssky.system.domain.dto.FtStationDto;
import com.itssky.system.domain.dto.StationShiftDto;
import com.itssky.system.domain.dto.VehicleClassStatDto;
import com.itssky.system.domain.vo.StationShiftVo;
import com.itssky.system.domain.vo.TbShVo;

import java.util.List;
import java.util.Map;

/**
 * @author ITSSKY
 */
public interface TollMapper {

    @Dbstats
    public List<StationShiftVo> f1StationShift(StationShiftDto dto);

    @Dbstats
    public List<StationShiftVo> f2StationShift(StationShiftDto dto);

    @Dbstats
    public List<StationShiftVo> ftToll(FtStationDto dto);

    @Dbstats
    public List<VehicleClassStatVo> afvGeneral(VehicleClassStatDto dto);

    @Dbstats
    public List<EPayTollStatVo> eefEPay(VehicleClassStatDto dto);

    @Dbedge
    public List<TbShVo> getTbShData(StationShiftDto dto);

    @Dbedge
    public List<TbShVo> getTbShDataV2(FtStationDto dto);


}
