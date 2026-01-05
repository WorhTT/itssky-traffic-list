package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MobVcVo {

    private String staDate;

    private String monthDate;

    private String stationId;

    private String stationName;

    @Excel(name = "统计方式", mergeRow = 2)
    private String statType;

    @Excel(name = "客车", onlyHeader = true, mergeColumn = 5)
    private String kc;

    @Excel(name = "客一", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String k1;

    @Excel(name = "流量", startColumn = 2, headerRow = 4)
    private int k1f = 0;

    @Excel(name = "金额", startColumn = 3, headerRow = 4)
    private BigDecimal k1t = BigDecimal.ZERO;

    @Excel(name = "客二", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String k2;

    @Excel(name = "流量", startColumn = 4, headerRow = 4)
    private int k2f = 0;

    @Excel(name = "金额", startColumn = 5, headerRow = 4)
    private BigDecimal k2t = BigDecimal.ZERO;

    @Excel(name = "客三", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String k3;

    @Excel(name = "流量", startColumn = 6, headerRow = 4)
    private int k3f = 0;

    @Excel(name = "金额", startColumn = 7, headerRow = 4)
    private BigDecimal k3t = BigDecimal.ZERO;

    @Excel(name = "客四", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String k4;

    @Excel(name = "流量", startColumn = 8, headerRow = 4)
    private int k4f = 0;

    @Excel(name = "金额", startColumn = 9, headerRow = 4)
    private BigDecimal k4t = BigDecimal.ZERO;

    @Excel(name = "小计", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String ksum;

    @Excel(name = "流量", startColumn = 10, headerRow = 4)
    private int ksumf = 0;

    @Excel(name = "金额", startColumn = 11, headerRow = 4)
    private BigDecimal ksumt = BigDecimal.ZERO;

    @Excel(name = "货车", onlyHeader = true, mergeColumn = 5)
    private String hc;

    @Excel(name = "货一", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String h1;

    @Excel(name = "流量", startColumn = 12, headerRow = 4)
    private int h1f = 0;

    @Excel(name = "金额", startColumn = 13, headerRow = 4)
    private BigDecimal h1t = BigDecimal.ZERO;

    @Excel(name = "货二", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String h2;

    @Excel(name = "流量", startColumn = 14, headerRow = 4)
    private int h2f = 0;

    @Excel(name = "金额", startColumn = 15, headerRow = 4)
    private BigDecimal h2t = BigDecimal.ZERO;

    @Excel(name = "货三", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String h3;

    @Excel(name = "流量", startColumn = 16, headerRow = 4)
    private int h3f = 0;

    @Excel(name = "金额", startColumn = 17, headerRow = 4)
    private BigDecimal h3t = BigDecimal.ZERO;

    @Excel(name = "货四", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String h4;

    @Excel(name = "流量", startColumn = 18, headerRow = 4)
    private int h4f = 0;

    @Excel(name = "金额", startColumn = 19, headerRow = 4)
    private BigDecimal h4t = BigDecimal.ZERO;

    @Excel(name = "货五", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String h5;

    @Excel(name = "流量", startColumn = 20, headerRow = 4)
    private int h5f = 0;

    @Excel(name = "金额", startColumn = 21, headerRow = 4)
    private BigDecimal h5t = BigDecimal.ZERO;

    @Excel(name = "货六", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String h6;

    @Excel(name = "流量", startColumn = 22, headerRow = 4)
    private int h6f = 0;

    @Excel(name = "金额", startColumn = 23, headerRow = 4)
    private BigDecimal h6t = BigDecimal.ZERO;

    @Excel(name = "小计", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String hsum;

    @Excel(name = "流量", startColumn = 24, headerRow = 4)
    private int hsumf = 0;

    @Excel(name = "金额", startColumn = 25, headerRow = 4)
    private BigDecimal hsumt = BigDecimal.ZERO;

    @Excel(name = "专车", onlyHeader = true, mergeColumn = 5)
    private String zc;

    @Excel(name = "专一", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String z1;

    @Excel(name = "流量", startColumn = 26, headerRow = 4)
    private int z1f = 0;

    @Excel(name = "金额", startColumn = 27, headerRow = 4)
    private BigDecimal z1t = BigDecimal.ZERO;

    @Excel(name = "专二", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String z2;

    @Excel(name = "流量", startColumn = 28, headerRow = 4)
    private int z2f = 0;

    @Excel(name = "金额", startColumn = 29, headerRow = 4)
    private BigDecimal z2t = BigDecimal.ZERO;

    @Excel(name = "专三", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String z3;

    @Excel(name = "流量", startColumn = 30, headerRow = 4)
    private int z3f = 0;

    @Excel(name = "金额", startColumn = 31, headerRow = 4)
    private BigDecimal z3t = BigDecimal.ZERO;

    @Excel(name = "专四", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String z4;

    @Excel(name = "流量", startColumn = 32, headerRow = 4)
    private int z4f = 0;

    @Excel(name = "金额", startColumn = 33, headerRow = 4)
    private BigDecimal z4t = BigDecimal.ZERO;

    @Excel(name = "专五", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String z5;

    @Excel(name = "流量", startColumn = 34, headerRow = 4)
    private int z5f = 0;

    @Excel(name = "金额", startColumn = 35, headerRow = 4)
    private BigDecimal z5t = BigDecimal.ZERO;

    @Excel(name = "专六", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String z6;

    @Excel(name = "流量", startColumn = 36, headerRow = 4)
    private int z6f = 0;

    @Excel(name = "金额", startColumn = 37, headerRow = 4)
    private BigDecimal z6t = BigDecimal.ZERO;

    @Excel(name = "小计", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String zsum;

    @Excel(name = "流量", startColumn = 38, headerRow = 4)
    private int zsumf = 0;

    @Excel(name = "金额", startColumn = 39, headerRow = 4)
    private BigDecimal zsumt = BigDecimal.ZERO;

    @Excel(name = "合计", onlyHeader = true, mergeColumn = 2, headerRow = 3)
    private String sum;

    @Excel(name = "流量", startColumn = 40, headerRow = 3)
    private int sumf = 0;

    @Excel(name = "金额", startColumn = 41, headerRow = 3)
    private BigDecimal sumt = BigDecimal.ZERO;

    public boolean totalRow = false;

}
