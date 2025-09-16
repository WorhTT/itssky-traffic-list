package com.itssky.system.mapper;


import com.itssky.db.Dbedge;
import com.itssky.db.Dbstats;
import com.itssky.db.ItsData;
import com.itssky.system.domain.vo.*;
import com.itssky.system.domain.dto.FtStationDto;
import com.itssky.system.domain.dto.StationShiftDto;
import com.itssky.system.domain.dto.VehicleClassStatDto;

import java.util.List;

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

    @Dbedge
    public List<F6TollVo> getF6TollEntry(StationShiftDto dto);

    @Dbedge
    public List<F6TollVo> getF6TollExit(StationShiftDto dto);

    @Dbedge
    public List<F6TollVo> getF6TollExtra(StationShiftDto dto);

    @Dbedge
    public List<TbShVo> getTbShGroupByStation(StationShiftDto dto);

    @Dbedge
    public List<ExtraPayVo> getExtraPay(StationShiftDto dto);

    @Dbstats
    public List<Cf1Vo> getCf1Vo(StationShiftDto dto);

    @Dbstats
    public List<MOBTollVo> getMOBToll(FtStationDto dto);
}

