package com.itssky.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 绿优台账Vo
 * @author ITSSKY
 */
@Data
public class GreenVo {

    @Excel(name = "时间")
    private String staDate = "";

    @Excel(name = "收费站")
    private String stationName = "";

    @Excel(name = "收费员")
    private String operatorName = "";

    @Excel(name = "车道")
    private String laneId = "";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date exitTime;

    @Excel(name = "交易时间")
    private String exitTimeStr = "";

    @Excel(name = "优惠前金额")
    private BigDecimal tollfee = new BigDecimal("0.00");

    @Excel(name = "车牌")
    private String LicensePlate = "";

    private Integer OperatorId;

    private boolean hj;

    private Integer stationId;

}
