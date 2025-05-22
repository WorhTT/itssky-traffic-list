package com.itssky.system.domain.vo;


import com.itssky.common.annotation.Excel;
import lombok.*;

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
    private String cust1C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 3)
    private String cust1D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 4)
    private String cust1Sum = "0";

    @Excel(name = "客二", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 5)
    private String cust2;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 5)
    private String cust2C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 6)
    private String cust2D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 7)
    private String cust2Sum = "0";

    @Excel(name = "客三", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 8)
    private String cust3;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 8)
    private String cust3C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 9)
    private String cust3D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 10)
    private String cust3Sum = "0";

    @Excel(name = "客四", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 11)
    private String cust4;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 11)
    private String cust4C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 12)
    private String cust4D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 13)
    private String cust4Sum = "0";

    @Excel(name = "小计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 14)
    private String custSubTotal;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 14)
    private String custCSubTotal = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 15)
    private String custDSubTotal = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 16)
    private String custSubSum = "0";

    @Excel(name = "货车", onlyHeader = true, mergeRow = 1, mergeColumn = 21, headerRow = 2)
    private String trust;

    @Excel(name = "货一", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 17)
    private String trust1;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 17)
    private String trust1C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 18)
    private String trust1D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 19)
    private String trust1Sum = "0";

    @Excel(name = "货二", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 20)
    private String trust2;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 20)
    private String trust2C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 21)
    private String trust2D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 22)
    private String trust2Sum = "0";

    @Excel(name = "货三", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 23)
    private String trust3;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 23)
    private String trust3C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 24)
    private String trust3D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 25)
    private String trust3Sum = "0";

    @Excel(name = "货四", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 26)
    private String trust4;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 26)
    private String trust4C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 27)
    private String trust4D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 28)
    private String trust4Sum = "0";

    @Excel(name = "货五", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 29)
    private String trust5;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 29)
    private String trust5C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 30)
    private String trust5D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 31)
    private String trust5Sum = "0";

    @Excel(name = "货六", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 32)
    private String trust6;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 32)
    private String trust6C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 33)
    private String trust6D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 34)
    private String trust6Sum = "0";

    @Excel(name = "小计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 35)
    private String trustSubTotal;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 35)
    private String trustCSubTotal = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 36)
    private String trustDSubTotal = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 37)
    private String trustSubSum = "0";

    @Excel(name = "专车", onlyHeader = true, mergeRow = 1, mergeColumn = 21, headerRow = 2)
    private String spec;

    @Excel(name = "专一", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 38)
    private String spec1;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 38)
    private String spec1C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 39)
    private String spec1D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 40)
    private String spec1Sum = "0";

    @Excel(name = "专二", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 41)
    private String spec2;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 41)
    private String spec2C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 42)
    private String spec2D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 43)
    private String spec2Sum = "0";

    @Excel(name = "专三", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 44)
    private String spec3;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 44)
    private String spec3C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 45)
    private String spec3D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 46)
    private String spec3Sum = "0";

    @Excel(name = "专四", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 47)
    private String spec4;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 47)
    private String spec4C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 48)
    private String spec4D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 49)
    private String spec4Sum = "0";

    @Excel(name = "专五", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 50)
    private String spec5;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 50)
    private String spec5C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 51)
    private String spec5D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 52)
    private String spec5Sum = "0";

    @Excel(name = "专六", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 53)
    private String spec6 = "0";

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 53)
    private String spec6C = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 54)
    private String spec6D = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 55)
    private String spec6Sum = "0";

    @Excel(name = "小计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 56)
    private String specSubTotal;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 56)
    private String specCSubTotal = "0";

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 57)
    private String specDSubTotal = "0";

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 58)
    private String specSubSum = "0";

    @Excel(name = "总计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 2)
    private String total;

    @Excel(name = "C卡", mergeRow = 2, mergeColumn = 1, startColumn = 59, headerRow = 3)
    private String cTotal = "0";

    @Excel(name = "D卡", mergeRow = 2, mergeColumn = 1, startColumn = 60, headerRow = 3)
    private String dTotal = "0";

    @Excel(name = "合计", mergeRow = 2, mergeColumn = 1, startColumn = 61, headerRow = 3)
    private String totalSum = "0";
}
