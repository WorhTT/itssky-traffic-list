package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AfvVehicleVo {

    @Excel(name = "统计方式")
    private String statType = "";

    @Excel(name = "客一")
    private Double cust1 = 0D;

    @Excel(name = "客二")
    private Double cust2 = 0D;

    @Excel(name = "客三")
    private Double cust3 = 0D;

    @Excel(name = "客四")
    private Double cust4 = 0D;

    @Excel(name = "客车小计")
    private Double custSubTotal = 0D;

    @Excel(name = "货一")
    private Double truck1 = 0D;

    @Excel(name = "货二")
    private Double truck2 = 0D;

    @Excel(name = "货三")
    private Double truck3 = 0D;

    @Excel(name = "货四")
    private Double truck4 = 0D;

    @Excel(name = "货五")
    private Double truck5 = 0D;

    @Excel(name = "货六")
    private Double truck6 = 0D;

    @Excel(name = "货车小计")
    private Double truckSubTotal = 0D;

    @Excel(name = "专一")
    private Double spec1 = 0D;

    @Excel(name = "专二")
    private Double spec2 = 0D;

    @Excel(name = "专三")
    private Double spec3 = 0D;

    @Excel(name = "专四")
    private Double spec4 = 0D;

    @Excel(name = "专五")
    private Double spec5 = 0D;

    @Excel(name = "专六")
    private Double spec6 = 0D;

    @Excel(name = "货车小计")
    private Double specSubTotal = 0D;

    @Excel(name = "加收")
    private Double addedAmount = 0D;

    @Excel(name = "合计")
    private Double totalAmount = 0D;
}
