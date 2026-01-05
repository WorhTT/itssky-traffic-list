package com.itssky.system.service;

import com.itssky.system.domain.dto.*;
import com.itssky.system.domain.vo.*;

import java.util.List;

public interface ITollService {

    public List<StationShiftVo> f1StationShift(StationTimeDto dto);

    public List<F1StationShiftTollVo> getF1StationShiftToll(StationTimeDto dto);

    public List<F2StationShiftTollVo> getF2StationShiftToll(StationTimeDto dto);

    public List<StationShiftVo> f2StationShift(StationTimeDto dto);

    public List<StationShiftVo> ftToll(FtStationDto dto);

    public List<FtTollVo> getFtToll(FtStationDto dto);

    public List<VehicleClassStatVo> afvGeneral(VehicleClassStatDto dto);

    public List<AfvVehicleVo> getAfvGeneral(VehicleClassStatDto dto);

    public List<EPayTollStatVo> eefEPay(VehicleClassStatDto dto);

    public List<F6TollVo> f6Toll(StationTimeDto dto);

    public List<Cf1Vo> cf1Toll(StationTimeDto dto);

    public List<MOBTollVo> mobToll(FtStationDto dto);

    public List<StationShiftVo> yjzz(FtStationDto dto);

    public List<TollYhVo> yh(TollYhDto dto);

    public List<EuVo> eu(CommonReportDto dto);

    public List<MobVcVo> mobVc(CommonReportDto dto);
}
