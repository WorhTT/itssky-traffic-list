package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * EU电子支付(MTC+ETC)按车型统计
 */
@Data
public class EuVo {

    private String staDate;

    private String monthDate;

    private String stationId;

    private String stationName;

    @Excel(name = "车型", mergeRow = 2)
    private String statType;

    @Excel(name = "一车型", onlyHeader = true, mergeColumn = 3)
    private String cx1;

    @Excel(name = "C卡", startColumn = 2, headerRow = 3)
    private BigDecimal vc1 = BigDecimal.ZERO;

    @Excel(name = "D卡", startColumn = 3, headerRow = 3)
    private BigDecimal vd1 = BigDecimal.ZERO;

    @Excel(name = "小计", startColumn = 4, headerRow = 3)
    private BigDecimal v1 = BigDecimal.ZERO;

    @Excel(name = "二车型", onlyHeader = true, mergeColumn = 3)
    private String cx2;

    @Excel(name = "C卡", startColumn = 5, headerRow = 3)
    private BigDecimal vc2 = BigDecimal.ZERO;

    @Excel(name = "D卡", startColumn = 6, headerRow = 3)
    private BigDecimal vd2 = BigDecimal.ZERO;

    @Excel(name = "小计", startColumn = 7, headerRow = 3)
    private BigDecimal v2 = BigDecimal.ZERO;

    @Excel(name = "三车型", onlyHeader = true, mergeColumn = 3)
    private String cx3;

    @Excel(name = "C卡", startColumn = 8, headerRow = 3)
    private BigDecimal vc3 = BigDecimal.ZERO;

    @Excel(name = "D卡", startColumn = 9, headerRow = 3)
    private BigDecimal vd3 = BigDecimal.ZERO;

    @Excel(name = "小计", startColumn = 10, headerRow = 3)
    private BigDecimal v3 = BigDecimal.ZERO;

    @Excel(name = "四车型", onlyHeader = true, mergeColumn = 3)
    private String cx4;

    @Excel(name = "C卡", startColumn = 11, headerRow = 3)
    private BigDecimal vc4 = BigDecimal.ZERO;

    @Excel(name = "D卡", startColumn = 12, headerRow = 3)
    private BigDecimal vd4 = BigDecimal.ZERO;

    @Excel(name = "小计", startColumn = 13, headerRow = 3)
    private BigDecimal v4 = BigDecimal.ZERO;

    @Excel(name = "五车型", onlyHeader = true, mergeColumn = 3)
    private String cx5;

    @Excel(name = "C卡", startColumn = 14, headerRow = 3)
    private BigDecimal vc5 = BigDecimal.ZERO;

    @Excel(name = "D卡", startColumn = 15, headerRow = 3)
    private BigDecimal vd5 = BigDecimal.ZERO;

    @Excel(name = "小计", startColumn = 16, headerRow = 3)
    private BigDecimal v5 = BigDecimal.ZERO;

    @Excel(name = "六车型", onlyHeader = true, mergeColumn = 3)
    private String cx6;

    @Excel(name = "C卡", startColumn = 17, headerRow = 3)
    private BigDecimal vc6 = BigDecimal.ZERO;

    @Excel(name = "D卡", startColumn = 18, headerRow = 3)
    private BigDecimal vd6 = BigDecimal.ZERO;

    @Excel(name = "小计", startColumn = 19, headerRow = 3)
    private BigDecimal v6 = BigDecimal.ZERO;

    @Excel(name = "专项车", onlyHeader = true, mergeColumn = 3)
    private String cxz;

    @Excel(name = "C卡", startColumn = 20, headerRow = 3)
    private BigDecimal vcz = BigDecimal.ZERO;

    @Excel(name = "D卡", startColumn = 21, headerRow = 3)
    private BigDecimal vdz = BigDecimal.ZERO;

    @Excel(name = "小计", startColumn = 22, headerRow = 3)
    private BigDecimal vz = BigDecimal.ZERO;

    @Excel(name = "合计", onlyHeader = true, mergeColumn = 3)
    private String sumAll;

    @Excel(name = "C卡", startColumn = 23, headerRow = 3)
    private BigDecimal sumc = BigDecimal.ZERO;

    @Excel(name = "D卡", startColumn = 24, headerRow = 3)
    private BigDecimal sumd = BigDecimal.ZERO;

    @Excel(name = "小计", startColumn = 25, headerRow = 3)
    private BigDecimal sum = BigDecimal.ZERO;

    public boolean totalRow = false;
}
