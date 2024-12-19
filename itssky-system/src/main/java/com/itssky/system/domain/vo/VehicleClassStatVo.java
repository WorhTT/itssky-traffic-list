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
    private Integer cust1;

    /**
     * 客二
     */
    private Integer cust2;

    /**
     * 客三
     */
    private Integer cust3;

    /**
     * 客四
     */
    private Integer cust4;

    /**
     * 客车小计
     */
    private Integer custSubTotal;

    /**
     * 货一
     */
    private Integer truck1;

    /**
     * 货二
     */
    private Integer truck2;

    /**
     * 货三
     */
    private Integer truck3;

    /**
     * 货四
     */
    private Integer truck4;

    /**
     * 货五
     */
    private Integer truck5;

    /**
     * 货六
     */
    private Integer truck6;

    /**
     * 货车小计
     */
    private Integer truckSubTotal;

    /**
     * 专一
     */
    private Integer spec1;

    /**
     * 专二
     */
    private Integer spec2;

    /**
     * 专三
     */
    private Integer spec3;

    /**
     * 专四
     */
    private Integer spec4;

    /**
     * 专五
     */
    private Integer spec5;

    /**
     * 专六
     */
    private Integer spec6;

    /**
     * 专车小计
     */
    private Integer specSubTotal;

    /**
     * 加收款
     */
    private Integer addedAmount;

    /**
     * 总计
     */
    private Integer totalAmount;

}
