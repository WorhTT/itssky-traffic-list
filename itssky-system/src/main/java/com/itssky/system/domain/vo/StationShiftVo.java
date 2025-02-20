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
     * 统计方式
     */
    private String statType;

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
    private Double statAmount = 0D;

    /**
     * 应缴金额
     */
    private Double dueAmount = 0D;

    /**
     * 实缴金额
     */
    private Double paidAmount = 0D;

    /**
     * 金额差异
     */
    private Double amountDiff = 0D;

    /**
     * 欠款车次
     */
    private Integer arrearsTrips = 0;

    /**
     * 欠款金额
     */
    private Double arrearsAmount = 0D;

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
    private Double extraTotal = 0D;

    /**
     * 移动支付金额
     */
    private Double mobilePaymentAmount = 0D;

    /**
     * 电子支付金额
     */
    private Double ePaymentAmount = 0D;

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
