package com.itssky.system.service;

import com.itssky.system.domain.vo.*;
import com.itssky.system.domain.dto.FtStationDto;
import com.itssky.system.domain.dto.StationShiftDto;
import com.itssky.system.domain.dto.VehicleClassStatDto;

import java.util.List;

public interface ITollService {

    public List<StationShiftVo> f1StationShift(StationShiftDto dto);

    public List<F1StationShiftTollVo> getF1StationShiftToll(StationShiftDto dto);

    public List<F2StationShiftTollVo> getF2StationShiftToll(StationShiftDto dto);

    public List<StationShiftVo> f2StationShift(StationShiftDto dto);

    public List<StationShiftVo> ftToll(FtStationDto dto);

    public List<FtTollVo> getFtToll(FtStationDto dto);

    public List<VehicleClassStatVo> afvGeneral(VehicleClassStatDto dto);

    public List<AfvVehicleVo> getAfvGeneral(VehicleClassStatDto dto);

    public List<EPayTollStatVo> eefEPay(VehicleClassStatDto dto);
}
