package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class F6TollVo {

    @Excel(name = "班组")
    private String teamId = "";

    @Excel(name = "收费员工号")
    private String operatorId = "";

    @Excel(name = "收费员姓名")
    private String operatorName = "";

    @Excel(name = "应缴金额")
    private BigDecimal toll;

    /**
     * 出口钱
     */
    private BigDecimal exitToll;

    /**
     * 加收款
     */
    private BigDecimal addedToll;

    @Excel(name = "应缴IC卡张数")
    private int yjIcCardNum = 0;

    @Excel(name = "纸券")
    private int paperNum;

    @Excel(name = "应发IC卡张数")
    private int yfIcCardNum = 0;

    private boolean totalRow = false;
}