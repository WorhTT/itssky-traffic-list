package com.itssky.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FD27Vo {

    @Excel(name = "统计日期")
    private String statDate;

    @Excel(name = "班次")
    private String shiftId = "";

    @Excel(name = "收费员工号")
    private String operatorId = "";

    @Excel(name = "收费员姓名")
    private String operatorName = "";

    @Excel(name = "车道")
    private String laneId = "";

    @Excel(name = "卡号")
    private String cardId = "";

    @Excel(name = "车牌")
    private String licensePlate = "";

    private Date tradeTime;

    @Excel(name = "收费时间", width = 30)
    private String tradeTimeStr = "";

    @Excel(name = "改前车型")
    private String beginVehicleClass = "";

    @Excel(name = "入口车型")
    private String entryVehicleClass = "";

    @Excel(name = "收费车型")
    private String tradeVehicleClass = "";

    @Excel(name = "收费金额")
    private BigDecimal toll = BigDecimal.ZERO;

    private boolean totalRow;
}
