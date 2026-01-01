package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * YH优惠金额综合报表
 */
@Data
public class TollYhVo {

    @Excel(name = "统计方式", mergeRow = 2)
    private String statType;

    private String staDate;

    private String operatorId;

    private String operatorName;

    private String monthDate;

    private String stationId;

    private String stationName;

    /**
     * 集装箱、绿色通道、抗震救灾、运管苏通卡货车、军车、专用工作卡、收割机、应急、大件运输、合计
     */
    @Excel(name = "集装箱优惠", onlyHeader = true, mergeColumn = 3)
    private String jzx;

    @Excel(name = "优惠前", startColumn = 2, headerRow = 3)
    private BigDecimal jzxq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 3, headerRow = 3)
    private BigDecimal jzxh = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 4, headerRow = 3)
    private BigDecimal jzxd = BigDecimal.ZERO;

    @Excel(name = "绿色通道优惠", onlyHeader = true, mergeColumn = 3)
    private String lstd;

    @Excel(name = "优惠前", startColumn = 5, headerRow = 3)
    private BigDecimal lstdq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 6, headerRow = 3)
    private BigDecimal lstdh = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 7, headerRow = 3)
    private BigDecimal lstdd = BigDecimal.ZERO;

    @Excel(name = "抗震救灾优惠", onlyHeader = true, mergeColumn = 3)
    private String kzjz;

    @Excel(name = "优惠前", startColumn = 8, headerRow = 3)
    private BigDecimal kzjzq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 9, headerRow = 3)
    private BigDecimal kzjzh = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 10, headerRow = 3)
    private BigDecimal kzjzd = BigDecimal.ZERO;

    @Excel(name = "运管苏通卡货车优惠", onlyHeader = true, mergeColumn = 3)
    private String ygstk;

    @Excel(name = "优惠前", startColumn = 11, headerRow = 3)
    private BigDecimal ygstkq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 12, headerRow = 3)
    private BigDecimal ygstkh = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 13, headerRow = 3)
    private BigDecimal ygstkd = BigDecimal.ZERO;

    @Excel(name = "军车优惠", onlyHeader = true, mergeColumn = 3)
    private String jc;

    @Excel(name = "优惠前", startColumn = 14, headerRow = 3)
    private BigDecimal jcq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 15, headerRow = 3)
    private BigDecimal jch = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 16, headerRow = 3)
    private BigDecimal jcd = BigDecimal.ZERO;

    @Excel(name = "专用工作卡货车优惠", onlyHeader = true, mergeColumn = 3)
    private String zygzk;

    @Excel(name = "优惠前", startColumn = 17, headerRow = 3)
    private BigDecimal zygzkq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 18, headerRow = 3)
    private BigDecimal zygzkh = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 19, headerRow = 3)
    private BigDecimal zygzkd = BigDecimal.ZERO;

    @Excel(name = "收割机优惠", onlyHeader = true, mergeColumn = 3)
    private String sgj;

    @Excel(name = "优惠前", startColumn = 20, headerRow = 3)
    private BigDecimal sgjq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 21, headerRow = 3)
    private BigDecimal sgjh = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 22, headerRow = 3)
    private BigDecimal sgjd = BigDecimal.ZERO;

    @Excel(name = "应急优惠", onlyHeader = true, mergeColumn = 3)
    private String yj;

    @Excel(name = "优惠前", startColumn = 23, headerRow = 3)
    private BigDecimal yjq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 24, headerRow = 3)
    private BigDecimal yjh = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 25, headerRow = 3)
    private BigDecimal yjd = BigDecimal.ZERO;

    @Excel(name = "大件运输优惠", onlyHeader = true, mergeColumn = 3)
    private String djys;

    @Excel(name = "优惠前", startColumn = 26, headerRow = 3)
    private BigDecimal djysq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 27, headerRow = 3)
    private BigDecimal djysh = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 28, headerRow = 3)
    private BigDecimal djysd = BigDecimal.ZERO;

    @Excel(name = "合计优惠", onlyHeader = true, mergeColumn = 3)
    private String sum;

    @Excel(name = "优惠前", startColumn = 29, headerRow = 3)
    private BigDecimal sumq = BigDecimal.ZERO;

    @Excel(name = "优惠后", startColumn = 30, headerRow = 3)
    private BigDecimal sumh = BigDecimal.ZERO;

    @Excel(name = "优惠掉", startColumn = 31, headerRow = 3)
    private BigDecimal sumd = BigDecimal.ZERO;

    private boolean totalRow = false;
}
