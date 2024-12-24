package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.*;

/**
 * 通行卡类统计Vo
 * @author ITSSKY
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CardStatisticsVo {

    private String statType;

    /**
     * 班次ID
     */
    private Integer shiftId;

    /**
     * 统计日期
     */
    private Integer StaDate;

    /**
     * 班组ID
     */
    private Integer teamId;

    /**
     * 月份
     */
    private String monthDate;

    /**
     * 收费站ID
     */
    private Integer stationId;

    /**
     * 收费站名称
     */
    private String stationName;

    /**
     * 工号
     */
    private Integer operatorId;

    /**
     * 客一
     */
    private Integer cust1 = 0;

    /**
     * 客二
     */
    private Integer cust2 = 0;

    /**
     * 客三
     */
    private Integer cust3 = 0;

    /**
     * 客四
     */
    private Integer cust4 = 0;

    /**
     * 客车小计
     */
    private Integer custSubTotal = 0;

    /**
     * 货一
     */
    private Integer truck1 = 0;

    /**
     * 货二
     */
    private Integer truck2 = 0;

    /**
     * 货三
     */
    private Integer truck3 = 0;

    /**
     * 货四
     */
    private Integer truck4 = 0;

    /**
     * 货五
     */
    private Integer truck5 = 0;

    /**
     * 货六
     */
    private Integer truck6 = 0;

    /**
     * 货车小计
     */
    private Integer truckSubTotal = 0;

    /**
     * 专一
     */
    private Integer spec1 = 0;

    /**
     * 专二
     */
    private Integer spec2 = 0;

    /**
     * 专三
     */
    private Integer spec3 = 0;

    /**
     * 专四
     */
    private Integer spec4 = 0;

    /**
     * 专五
     */
    private Integer spec5 = 0;

    /**
     * 专六
     */
    private Integer spec6 = 0;

    /**
     * 专车小计
     */
    private Integer specSubTotal = 0;


    /**
     * 军车
     */
    private Integer militaryNum = 0;

    /**
     * 公务
     */
    private Integer officialNum = 0;

    /**
     * 车队
     */
    private Integer fleetNum = 0;

    /**
     * 优惠
     */
    private Integer preferNum = 0;

    /**
     * ETC
     */
    private Integer etcNum = 0;

    /**
     * 恢复卡
     */
    private Integer recoverNum = 0;

    /**
     * 纸券
     */
    private Integer paperNum = 0;


    /**
     * 应发卡 = 客小计 + 货小计 + 专小计
     */
    private Integer issuedNum = 0;

    /**
     * 实发卡
     */
    private Integer actualNum = 0;

    /**
     * 总流量 = 应发卡 + 公务 + 车队 + 优惠 + ETC
     */
    private Integer totalFlow = 0;


    /**
     * 无卡
     */
    private Integer noneNum = 0;

    /**
     * 卡损
     */
    private Integer badNum = 0;

}
