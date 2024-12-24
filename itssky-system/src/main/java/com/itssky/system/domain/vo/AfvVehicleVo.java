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
    private String statType;

    @Excel(name = "客一")
    private Integer cust1;

    @Excel(name = "客二")
    private Integer cust2;

    @Excel(name = "客三")
    private Integer cust3;

    @Excel(name = "客四")
    private Integer cust4;

    @Excel(name = "客车小计")
    private Integer custSubTotal;

    @Excel(name = "货一")
    private Integer truck1;

    @Excel(name = "货二")
    private Integer truck2;

    @Excel(name = "货三")
    private Integer truck3;

    @Excel(name = "货四")
    private Integer truck4;

    @Excel(name = "货五")
    private Integer truck5;

    @Excel(name = "货六")
    private Integer truck6;

    @Excel(name = "货车小计")
    private Integer truckSubTotal;

    @Excel(name = "专一")
    private Integer spec1;

    @Excel(name = "专二")
    private Integer spec2;

    @Excel(name = "专三")
    private Integer spec3;

    @Excel(name = "专四")
    private Integer spec4;

    @Excel(name = "专五")
    private Integer spec5;

    @Excel(name = "专六")
    private Integer spec6;

    @Excel(name = "货车小计")
    private Integer specSubTotal;

    @Excel(name = "加收")
    private Integer addedAmount;

    @Excel(name = "合计")
    private Integer totalAmount;
}
