package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AfvVehicleVo {

    @Excel(name = "统计方式")
    private String statType = "";

    @Excel(name = "客一")
    private BigDecimal cust1 = BigDecimal.ZERO;

    @Excel(name = "客二")
    private BigDecimal cust2 = BigDecimal.ZERO;

    @Excel(name = "客三")
    private BigDecimal cust3 = BigDecimal.ZERO;

    @Excel(name = "客四")
    private BigDecimal cust4 = BigDecimal.ZERO;

    @Excel(name = "客车小计")
    private BigDecimal custSubTotal = BigDecimal.ZERO;

    @Excel(name = "货一")
    private BigDecimal truck1 = BigDecimal.ZERO;

    @Excel(name = "货二")
    private BigDecimal truck2 = BigDecimal.ZERO;

    @Excel(name = "货三")
    private BigDecimal truck3 = BigDecimal.ZERO;

    @Excel(name = "货四")
    private BigDecimal truck4 = BigDecimal.ZERO;

    @Excel(name = "货五")
    private BigDecimal truck5 = BigDecimal.ZERO;

    @Excel(name = "货六")
    private BigDecimal truck6 = BigDecimal.ZERO;

    @Excel(name = "货车小计")
    private BigDecimal truckSubTotal = BigDecimal.ZERO;

    @Excel(name = "专一")
    private BigDecimal spec1 = BigDecimal.ZERO;

    @Excel(name = "专二")
    private BigDecimal spec2 = BigDecimal.ZERO;

    @Excel(name = "专三")
    private BigDecimal spec3 = BigDecimal.ZERO;

    @Excel(name = "专四")
    private BigDecimal spec4 = BigDecimal.ZERO;

    @Excel(name = "专五")
    private BigDecimal spec5 = BigDecimal.ZERO;

    @Excel(name = "专六")
    private BigDecimal spec6 = BigDecimal.ZERO;

    @Excel(name = "货车小计")
    private BigDecimal specSubTotal = BigDecimal.ZERO;

    @Excel(name = "加收")
    private BigDecimal addedAmount = BigDecimal.ZERO;

    @Excel(name = "合计")
    private BigDecimal totalAmount = BigDecimal.ZERO;
}
