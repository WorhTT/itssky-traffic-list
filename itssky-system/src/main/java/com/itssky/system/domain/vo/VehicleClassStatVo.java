package com.itssky.system.domain.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * 车型统计表Vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class VehicleClassStatVo {

    /**
     * 统计方式
     */
    private String statType;

    /**
     * 收费站名称
     */
    private String stationName;

    /**
     * 收费站ID
     */
    private String stationId;

    /**
     * 员工ID
     */
    private String operatorId;

    /**
     * 统计日期
     */
    private String StaDate;

    /**
     * 月份
     */
    private String monthDate;

    /**
     * 客一
     */
    private BigDecimal cust1;

    /**
     * 客二
     */
    private BigDecimal cust2;

    /**
     * 客三
     */
    private BigDecimal cust3;

    /**
     * 客四
     */
    private BigDecimal cust4;

    /**
     * 客车小计
     */
    private BigDecimal custSubTotal;

    /**
     * 货一
     */
    private BigDecimal truck1;

    /**
     * 货二
     */
    private BigDecimal truck2;

    /**
     * 货三
     */
    private BigDecimal truck3;

    /**
     * 货四
     */
    private BigDecimal truck4;

    /**
     * 货五
     */
    private BigDecimal truck5;

    /**
     * 货六
     */
    private BigDecimal truck6;

    /**
     * 货车小计
     */
    private BigDecimal truckSubTotal;

    /**
     * 专一
     */
    private BigDecimal spec1;

    /**
     * 专二
     */
    private BigDecimal spec2;

    /**
     * 专三
     */
    private BigDecimal spec3;

    /**
     * 专四
     */
    private BigDecimal spec4;

    /**
     * 专五
     */
    private BigDecimal spec5;

    /**
     * 专六
     */
    private BigDecimal spec6;

    /**
     * 专车小计
     */
    private BigDecimal specSubTotal;

    /**
     * 加收款
     */
    private BigDecimal addedAmount = BigDecimal.ZERO;

    /**
     * 总计
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;

    private boolean totalRow;

}
