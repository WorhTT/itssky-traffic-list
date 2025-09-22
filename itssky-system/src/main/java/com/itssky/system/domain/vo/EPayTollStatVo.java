package com.itssky.system.domain.vo;


import com.itssky.common.annotation.Excel;
import lombok.*;

import java.math.BigDecimal;

/**
 * EEF电子支付通行费(MTC+ETC)统计表
 * @author ITSSKY
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EPayTollStatVo {

    private String stationName;

    private String stationId;

    private String operatorId;

    private String monthDate;

    private String staDate;

    @Excel(name = "统计方式", mergeRow = 3, mergeColumn = 1, headerRow = 2)
    private String statType = "";

    @Excel(name = "客车", onlyHeader = true, mergeRow = 1, mergeColumn = 15, headerRow = 2)
    private String vehicle;

    @Excel(name = "客一", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 2)
    private String cust1;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 2)
    private BigDecimal cust1C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 3)
    private BigDecimal cust1D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 4)
    private BigDecimal cust1Sum = new BigDecimal("0.00");

    @Excel(name = "客二", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 5)
    private String cust2;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 5)
    private BigDecimal cust2C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 6)
    private BigDecimal cust2D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 7)
    private BigDecimal cust2Sum = new BigDecimal("0.00");

    @Excel(name = "客三", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 8)
    private String cust3;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 8)
    private BigDecimal cust3C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 9)
    private BigDecimal cust3D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 10)
    private BigDecimal cust3Sum = new BigDecimal("0.00");

    @Excel(name = "客四", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 11)
    private String cust4;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 11)
    private BigDecimal cust4C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 12)
    private BigDecimal cust4D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 13)
    private BigDecimal cust4Sum = new BigDecimal("0.00");

    @Excel(name = "小计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 14)
    private String custSubTotal;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 14)
    private BigDecimal custCSubTotal = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 15)
    private BigDecimal custDSubTotal = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 16)
    private BigDecimal custSubSum = new BigDecimal("0.00");

    @Excel(name = "货车", onlyHeader = true, mergeRow = 1, mergeColumn = 21, headerRow = 2)
    private String trust;

    @Excel(name = "货一", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 17)
    private String trust1;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 17)
    private BigDecimal trust1C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 18)
    private BigDecimal trust1D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 19)
    private BigDecimal trust1Sum = new BigDecimal("0.00");

    @Excel(name = "货二", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 20)
    private String trust2;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 20)
    private BigDecimal trust2C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 21)
    private BigDecimal trust2D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 22)
    private BigDecimal trust2Sum = new BigDecimal("0.00");

    @Excel(name = "货三", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 23)
    private String trust3;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 23)
    private BigDecimal trust3C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 24)
    private BigDecimal trust3D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 25)
    private BigDecimal trust3Sum = new BigDecimal("0.00");

    @Excel(name = "货四", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 26)
    private String trust4;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 26)
    private BigDecimal trust4C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 27)
    private BigDecimal trust4D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 28)
    private BigDecimal trust4Sum = new BigDecimal("0.00");

    @Excel(name = "货五", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 29)
    private String trust5;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 29)
    private BigDecimal trust5C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 30)
    private BigDecimal trust5D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 31)
    private BigDecimal trust5Sum = new BigDecimal("0.00");

    @Excel(name = "货六", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 32)
    private String trust6;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 32)
    private BigDecimal trust6C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 33)
    private BigDecimal trust6D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 34)
    private BigDecimal trust6Sum = new BigDecimal("0.00");

    @Excel(name = "小计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 35)
    private String trustSubTotal;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 35)
    private BigDecimal trustCSubTotal = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 36)
    private BigDecimal trustDSubTotal = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 37)
    private BigDecimal trustSubSum = new BigDecimal("0.00");

    @Excel(name = "专车", onlyHeader = true, mergeRow = 1, mergeColumn = 21, headerRow = 2)
    private String spec;

    @Excel(name = "专一", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 38)
    private String spec1;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 38)
    private BigDecimal spec1C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 39)
    private BigDecimal spec1D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 40)
    private BigDecimal spec1Sum = new BigDecimal("0.00");

    @Excel(name = "专二", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 41)
    private String spec2;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 41)
    private BigDecimal spec2C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 42)
    private BigDecimal spec2D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 43)
    private BigDecimal spec2Sum = new BigDecimal("0.00");

    @Excel(name = "专三", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 44)
    private String spec3;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 44)
    private BigDecimal spec3C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 45)
    private BigDecimal spec3D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 46)
    private BigDecimal spec3Sum = new BigDecimal("0.00");

    @Excel(name = "专四", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 47)
    private String spec4;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 47)
    private BigDecimal spec4C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 48)
    private BigDecimal spec4D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 49)
    private BigDecimal spec4Sum = new BigDecimal("0.00");

    @Excel(name = "专五", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 50)
    private String spec5;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 50)
    private BigDecimal spec5C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 51)
    private BigDecimal spec5D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 52)
    private BigDecimal spec5Sum = new BigDecimal("0.00");

    @Excel(name = "专六", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 53)
    private String spec6 = "0";

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 53)
    private BigDecimal spec6C = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 54)
    private BigDecimal spec6D = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 55)
    private BigDecimal spec6Sum = new BigDecimal("0.00");

    @Excel(name = "小计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 56)
    private String specSubTotal;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 56)
    private BigDecimal specCSubTotal = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 57)
    private BigDecimal specDSubTotal = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 58)
    private BigDecimal specSubSum = new BigDecimal("0.00");

    @Excel(name = "总计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 2)
    private String total;

    @Excel(name = "C卡", mergeRow = 2, mergeColumn = 1, startColumn = 59, headerRow = 3)
    private BigDecimal cTotal = new BigDecimal("0.00");

    @Excel(name = "D卡", mergeRow = 2, mergeColumn = 1, startColumn = 60, headerRow = 3)
    private BigDecimal dTotal = new BigDecimal("0.00");

    @Excel(name = "合计", mergeRow = 2, mergeColumn = 1, startColumn = 61, headerRow = 3)
    private BigDecimal totalSum = new BigDecimal("0.00");

    private boolean totalRow;
}
