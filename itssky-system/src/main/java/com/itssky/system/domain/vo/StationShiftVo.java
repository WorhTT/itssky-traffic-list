package com.itssky.system.domain.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author ITSSKY
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StationShiftVo {

    /**
     * 统计方式
     */
    private String statType = "";

    /**
     * 收费站ID
     */
    private String stationId = "";

    /**
     * 收费站名称
     */
    private String stationName = "";


    /**
     * 班次ID
     */
    private String shiftId = "";

    /**
     * 班次
     */
    private String shiftName = "";

    /**
     * 班组ID
     */
    private String teamId = "";

    /**
     * 班组
     */
    private String teamName = "";

    /**
     * 工号
     */
    private String operatorId = "";

    /**
     * 统计金额
     */
    private BigDecimal statAmount = BigDecimal.ZERO;

    /**
     * 应缴金额
     */
    private BigDecimal dueAmount = BigDecimal.ZERO;

    /**
     * 实缴金额
     */
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /**
     * 金额差异
     */
    private BigDecimal amountDiff = BigDecimal.ZERO;

    /**
     * 欠款车次
     */
    private Integer arrearsTrips = 0;

    /**
     * 欠款金额
     */
    private BigDecimal arrearsAmount = BigDecimal.ZERO;

    /**
     * 加收款现金
     */
    private Integer extraCash = 0;

    /**
     * doutotaltoll
     */
    private BigDecimal douTotalToll = BigDecimal.ZERO;

    /**
     * 移动支付加收款
     */
    private Integer extraMobilePayment = 0;

    /**
     * 加收款合计
     */
    private BigDecimal extraTotal = BigDecimal.ZERO;

    /**
     * 移动支付金额
     */
    private BigDecimal mobilePaymentAmount = BigDecimal.ZERO;

    /**
     * 电子支付金额
     */
    private BigDecimal ePaymentAmount = BigDecimal.ZERO;

    /**
     * 公务IC卡数
     */
    private Integer officialIcCardCount = 0;

    /**
     * 军车IC卡数
     */
    private Integer militaryIcCardCount = 0;

    /**
     * 免费IC卡数
     */
    private Integer freeIcCardCount = 0;

    /**
     * 应缴IC卡数
     */
    private Integer dueIcCardCount = 0;

    /**
     * 小计行
     */
    private boolean subTotalRow;

    /**
     * 合计行
     */
    private boolean totalRow;

    /**
     * 统计方式 月份
     */
    private String monthDate;

    private String staDate;
}
