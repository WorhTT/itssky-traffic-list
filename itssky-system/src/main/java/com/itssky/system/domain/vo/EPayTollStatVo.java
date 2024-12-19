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

    private Integer stationId;

    private Integer operatorId;

    private String monthDate;

    private Integer staDate;

    @Excel(name = "统计方式", mergeRow = 3, mergeColumn = 1, headerRow = 2)
    private String statType;

    @Excel(name = "客车", onlyHeader = true, mergeRow = 1, mergeColumn = 15, headerRow = 2)
    private String vehicle;

    @Excel(name = "客一", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 2)
    private String cust1;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 2)
    private Integer cust1C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 3)
    private Integer cust1D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 4)
    private Integer cust1Sum;

    @Excel(name = "客二", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 5)
    private String cust2;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 5)
    private Integer cust2C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 6)
    private Integer cust2D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 7)
    private Integer cust2Sum;

    @Excel(name = "客三", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 8)
    private String cust3;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 8)
    private Integer cust3C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 9)
    private Integer cust3D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 10)
    private Integer cust3Sum;

    @Excel(name = "客四", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 11)
    private String cust4;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 11)
    private Integer cust4C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 12)
    private Integer cust4D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 13)
    private Integer cust4Sum;

    @Excel(name = "小计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 14)
    private String custSubTotal;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 14)
    private Integer custCSubTotal;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 15)
    private Integer custDSubTotal;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 16)
    private Integer custSubSum;

    @Excel(name = "货车", onlyHeader = true, mergeRow = 1, mergeColumn = 21, headerRow = 2)
    private String trust;

    @Excel(name = "货一", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 17)
    private String trust1;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 17)
    private Integer trust1C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 18)
    private Integer trust1D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 19)
    private Integer trust1Sum;

    @Excel(name = "货二", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 20)
    private String trust2;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 20)
    private Integer trust2C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 21)
    private Integer trust2D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 22)
    private Integer trust2Sum;

    @Excel(name = "货三", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 23)
    private String trust3;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 23)
    private Integer trust3C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 24)
    private Integer trust3D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 25)
    private Integer trust3Sum;

    @Excel(name = "货四", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 26)
    private String trust4;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 26)
    private Integer trust4C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 27)
    private Integer trust4D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 28)
    private Integer trust4Sum;

    @Excel(name = "货五", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 29)
    private String trust5;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 29)
    private Integer trust5C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 30)
    private Integer trust5D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 31)
    private Integer trust5Sum;

    @Excel(name = "货六", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 32)
    private String trust6;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 32)
    private Integer trust6C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 33)
    private Integer trust6D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 34)
    private Integer trust6Sum;

    @Excel(name = "小计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 35)
    private String trustSubTotal;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 35)
    private Integer trustCSubTotal;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 36)
    private Integer trustDSubTotal;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 37)
    private Integer trustSubSum;

    @Excel(name = "专车", onlyHeader = true, mergeRow = 1, mergeColumn = 21, headerRow = 2)
    private String spec;

    @Excel(name = "专一", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 38)
    private String spec1;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 38)
    private Integer spec1C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 39)
    private Integer spec1D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 40)
    private Integer spec1Sum;

    @Excel(name = "专二", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 41)
    private String spec2;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 41)
    private Integer spec2C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 42)
    private Integer spec2D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 43)
    private Integer spec2Sum;

    @Excel(name = "专三", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 44)
    private String spec3;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 44)
    private Integer spec3C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 45)
    private Integer spec3D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 46)
    private Integer spec3Sum;

    @Excel(name = "专四", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 47)
    private String spec4;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 47)
    private Integer spec4C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 48)
    private Integer spec4D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 49)
    private Integer spec4Sum;

    @Excel(name = "专五", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 50)
    private String spec5;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 50)
    private Integer spec5C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 51)
    private Integer spec5D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 52)
    private Integer spec5Sum;

    @Excel(name = "专六", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 53)
    private String spec6;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 53)
    private Integer spec6C;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 54)
    private Integer spec6D;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 55)
    private Integer spec6Sum;

    @Excel(name = "小计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 3, startColumn = 56)
    private String specSubTotal;

    @Excel(name = "C卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 56)
    private Integer specCSubTotal;

    @Excel(name = "D卡", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 57)
    private Integer specDSubTotal;

    @Excel(name = "合计", mergeRow = 1, mergeColumn = 1, headerRow = 4, startColumn = 58)
    private Integer specSubSum;

    @Excel(name = "总计", onlyHeader = true, mergeRow = 1, mergeColumn = 3, headerRow = 2)
    private String total;

    @Excel(name = "C卡", mergeRow = 2, mergeColumn = 1, startColumn = 59, headerRow = 3)
    private Integer cTotal;

    @Excel(name = "D卡", mergeRow = 2, mergeColumn = 1, startColumn = 60, headerRow = 3)
    private Integer dTotal;

    @Excel(name = "合计", mergeRow = 2, mergeColumn = 1, startColumn = 61, headerRow = 3)
    private Integer totalSum;
}
