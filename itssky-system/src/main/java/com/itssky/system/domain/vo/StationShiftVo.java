package com.itssky.system.domain.vo;

import lombok.*;

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
     * 收费站ID
     */
    private Integer stationId;

    /**
     * 收费站名称
     */
    private String stationName;


    /**
     * 班次ID
     */
    private Integer shiftId;

    /**
     * 班次
     */
    private String shiftName;

    /**
     * 班组ID
     */
    private Integer teamId;

    /**
     * 班组
     */
    private String teamName;

    /**
     * 工号
     */
    private Integer operatorId;

    /**
     * 统计金额
     */
    private Integer statAmount = 0;

    /**
     * 应缴金额
     */
    private Integer dueAmount = 0;

    /**
     * 实缴金额
     */
    private Integer paidAmount = 0;

    /**
     * 金额差异
     */
    private Integer amountDiff = 0;

    /**
     * 欠款车次
     */
    private Integer arrearsTrips = 0;

    /**
     * 欠款金额
     */
    private Integer arrearsAmount = 0;

    /**
     * 加收款现金
     */
    private Integer extraCash = 0;

    /**
     * 移动支付加收款
     */
    private Integer extraMobilePayment = 0;

    /**
     * 加收款合计
     */
    private Integer extraTotal = 0;

    /**
     * 移动支付金额
     */
    private Integer mobilePaymentAmount = 0;

    /**
     * 电子支付金额
     */
    private Integer ePaymentAmount = 0;

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

    private Integer staDate;
}
