package com.itssky.system.domain.vo;

import lombok.*;

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
    private Integer stationId;

    /**
     * 员工ID
     */
    private Integer operatorId;

    /**
     * 统计日期
     */
    private Integer StaDate;

    /**
     * 月份
     */
    private String monthDate;

    /**
     * 客一
     */
    private Double cust1;

    /**
     * 客二
     */
    private Double cust2;

    /**
     * 客三
     */
    private Double cust3;

    /**
     * 客四
     */
    private Double cust4;

    /**
     * 客车小计
     */
    private Double custSubTotal;

    /**
     * 货一
     */
    private Double truck1;

    /**
     * 货二
     */
    private Double truck2;

    /**
     * 货三
     */
    private Double truck3;

    /**
     * 货四
     */
    private Double truck4;

    /**
     * 货五
     */
    private Double truck5;

    /**
     * 货六
     */
    private Double truck6;

    /**
     * 货车小计
     */
    private Double truckSubTotal;

    /**
     * 专一
     */
    private Double spec1;

    /**
     * 专二
     */
    private Double spec2;

    /**
     * 专三
     */
    private Double spec3;

    /**
     * 专四
     */
    private Double spec4;

    /**
     * 专五
     */
    private Double spec5;

    /**
     * 专六
     */
    private Double spec6;

    /**
     * 专车小计
     */
    private Double specSubTotal;

    /**
     * 加收款
     */
    private Double addedAmount = 0D;

    /**
     * 总计
     */
    private Double totalAmount = 0D;

}
