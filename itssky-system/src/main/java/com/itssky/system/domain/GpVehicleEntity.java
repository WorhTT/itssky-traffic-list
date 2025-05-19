package com.itssky.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class GpVehicleEntity {
    
    private String vehicleLicense;
    
    private Integer stationId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape =  JsonFormat.Shape.STRING)
    private Date tradeTime;

    private Integer tradeDate;

    private Integer shiftId;

    private Integer operatorId;

    private Integer laneId;

    private String hexStationId;
}
