package com.itssky.system.service;

import com.itssky.system.domain.vo.EPayTollStatVo;
import com.itssky.system.domain.vo.F1StationShiftTollVo;
import com.itssky.system.domain.vo.VehicleClassStatVo;
import com.itssky.system.domain.dto.FtStationDto;
import com.itssky.system.domain.dto.StationShiftDto;
import com.itssky.system.domain.dto.VehicleClassStatDto;
import com.itssky.system.domain.vo.StationShiftVo;

import java.util.List;

public interface ITollService {

    public List<StationShiftVo> f1StationShift(StationShiftDto dto);

    public List<F1StationShiftTollVo> getF1StationShiftToll(StationShiftDto dto);

    public List<StationShiftVo> f2StationShift(StationShiftDto dto);

    public List<StationShiftVo> ftToll(FtStationDto dto);

    public List<VehicleClassStatVo> afvGeneral(VehicleClassStatDto dto);

    public List<EPayTollStatVo> eefEPay(VehicleClassStatDto dto);
}
