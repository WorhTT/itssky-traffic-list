package com.itssky.system.mapper;


import com.itssky.db.*;
import com.itssky.system.domain.dto.*;
import com.itssky.system.domain.vo.*;

import java.util.List;

/**
 * @author ITSSKY
 */
@Dbstats
public interface TollMapper {

    public List<StationShiftVo> f1StationShift(StationTimeDto dto);

    public List<StationShiftVo> f2StationShift(StationTimeDto dto);

    public List<StationShiftVo> ftToll(FtStationDto dto);

    @Dbstats2
    public List<StationShiftVo> ftTollForOtherDatabase(FtStationDto dto);

    public List<VehicleClassStatVo> afvGeneral(VehicleClassStatDto dto);

    public List<EPayTollStatVo> eefEPay(VehicleClassStatDto dto);

    @Dbedge
    public List<TbShVo> getTbShData(StationTimeDto dto);

    @Dbedge
    public List<TbShVo> getTbShDataV2(FtStationDto dto);

    @Dbedge2
    public List<TbShVo> getTbShDataV2ForOtherDatabase(FtStationDto dto);

    @Dbedge
    public List<F6TollVo> getF6TollEntry(StationTimeDto dto);

    @Dbedge
    public List<F6TollVo> getF6TollExit(StationTimeDto dto);

    @Dbedge
    public List<F6TollVo> getF6TollExtra(StationTimeDto dto);

    @Dbedge
    public List<TbShVo> getTbShGroupByStation(StationTimeDto dto);

    @Dbedge
    public List<ExtraPayVo> getExtraPay(StationTimeDto dto);

    @Dbedge2
    public List<ExtraPayVo> getExtraPayForOtherDatabase(StationTimeDto dto);

    public List<Cf1Vo> getCf1Vo(StationTimeDto dto);

    public List<MOBTollVo> getMOBToll(FtStationDto dto);

    public List<TollYhVo> yh(TollYhDto dto);

    public List<EuVo> eu(CommonReportDto dto);

    public List<MobVcVo> mobVc(CommonReportDto dto);
}

