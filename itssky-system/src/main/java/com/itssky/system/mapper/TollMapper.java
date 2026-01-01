package com.itssky.system.mapper;


import com.itssky.db.*;
import com.itssky.system.domain.dto.TollYhDto;
import com.itssky.system.domain.vo.*;
import com.itssky.system.domain.dto.FtStationDto;
import com.itssky.system.domain.dto.StationShiftDto;
import com.itssky.system.domain.dto.VehicleClassStatDto;

import java.util.List;

/**
 * @author ITSSKY
 */
@Dbstats
public interface TollMapper {

    public List<StationShiftVo> f1StationShift(StationShiftDto dto);

    public List<StationShiftVo> f2StationShift(StationShiftDto dto);

    public List<StationShiftVo> ftToll(FtStationDto dto);

    @Dbstats2
    public List<StationShiftVo> ftTollForOtherDatabase(FtStationDto dto);

    public List<VehicleClassStatVo> afvGeneral(VehicleClassStatDto dto);

    public List<EPayTollStatVo> eefEPay(VehicleClassStatDto dto);

    @Dbedge
    public List<TbShVo> getTbShData(StationShiftDto dto);

    @Dbedge
    public List<TbShVo> getTbShDataV2(FtStationDto dto);

    @Dbedge2
    public List<TbShVo> getTbShDataV2ForOtherDatabase(FtStationDto dto);

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

    @Dbedge2
    public List<ExtraPayVo> getExtraPayForOtherDatabase(StationShiftDto dto);

    public List<Cf1Vo> getCf1Vo(StationShiftDto dto);

    public List<MOBTollVo> getMOBToll(FtStationDto dto);

    public List<TollYhVo> yh(TollYhDto dto);
}

