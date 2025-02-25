package com.itssky.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 入口超限操作明细表Vo
 * @author ITSSKY
 */
@Data
public class CxczVo {

    @Excel(name = "入口站")
    private String stationName = "";

    /**
     * 入口车道ID
     */
    private Integer laneId;

    @Excel(name = "入口车道")
    private String laneName = "";

    @Excel(name = "卡号")
    private String cardId = "";

    @Excel(name = "入口工号")
    private String operatorId = "";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date entryTime;

    @Excel(name = "入口时间")
    private String entryTimeStr = "";

    @Excel(name = "人工车牌")
    private String vehicleLicense = "";

    @Excel(name = "识别车牌")
    private String LicensePlate = "";

    @Excel(name = "轴型")
    private String axisType = "";

    private String vehicleClass;

    @Excel(name = "车型")
    private String vehicleClassStr;

    @Excel(name = "车重(KG)")
    private String totalWeight;

    @Excel(name = "限重(KG)")
    private String limitWeight;

    @Excel(name = "超限率")
    private String overLoadRate;

    @Excel(name = "校验方式")
    private String checkType;

    @Excel(name = "超限操作")
    private String cz;

}
