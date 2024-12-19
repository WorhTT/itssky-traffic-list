package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.*;

/**
 * @author ITSSKY
 * F1收费站通行费收入班统计表
 */
@Data
@Builder
@ToString
public class F1StationShiftTollVo {

    @Excel(name = "班次",  mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer shiftId;

    @Excel(name = "班组", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer teamId;

    @Excel(name = "工号", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer operatorId;

    @Excel(name = "通行费收入总额", mergeColumn = 6, headerRow = 2, mergeRow = 1, onlyHeader = true)
    private String tollAmount;

    @Excel(name = "统计金额", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 4)
    private Integer statAmount = 0;

    @Excel(name = "应缴金额", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 5)
    private Integer dueAmount = 0;

    @Excel(name = "实缴金额", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 6)
    private Integer paidAmount = 0;

    @Excel(name = "金额差异", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 7)
    private Integer amountDiff = 0;

    @Excel(name = "欠款", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 8)
    private Integer arrearsAmount = 0;

    @Excel(name = "加收款", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 9)
    private Integer extraTotal = 0;

    @Excel(name = "移动支付", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer mobilePaymentAmount = 0;

    @Excel(name = "电子支付", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer ePaymentAmount = 0;

    @Excel(name = "公务IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer officialIcCardCount = 0;

    @Excel(name = "军车IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer militaryIcCardCount = 0;

    @Excel(name = "免费IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer freeIcCardCount = 0;

    @Excel(name = "应缴IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer dueIcCardCount = 0;

}
