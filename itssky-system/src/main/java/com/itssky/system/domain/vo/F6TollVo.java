package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class F6TollVo {

    @Excel(name = "收费员工号")
    private String operatorId = "";

    @Excel(name = "收费员姓名")
    private String operatorName = "";

    @Excel(name = "应缴金额")
    private Double toll = 0D;

    @Excel(name = "应缴IC卡张数")
    private int yjIcCardNum = 0;

    @Excel(name = "纸券")
    private int paperNum;

    @Excel(name = "应发IC卡张数")
    private int yfIcCardNum = 0;

    private boolean totalRow = false;
}