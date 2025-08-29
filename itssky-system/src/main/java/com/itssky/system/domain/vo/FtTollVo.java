package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FtTollVo {

    @Excel(name = "统计方式", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private String statType;

    @Excel(name = "通行费收入总额", mergeColumn = 6, headerRow = 2, mergeRow = 1, onlyHeader = true)
    private String tollAmount;

    @Excel(name = "统计金额", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 2)
    private BigDecimal statAmount = BigDecimal.ZERO;

    @Excel(name = "应缴金额", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 3)
    private BigDecimal dueAmount = BigDecimal.ZERO;

    @Excel(name = "实缴金额", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 4)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Excel(name = "金额差异", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 5)
    private BigDecimal amountDiff = BigDecimal.ZERO;

    @Excel(name = "欠款", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 6)
    private BigDecimal arrearsAmount = BigDecimal.ZERO;

    @Excel(name = "加收款", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 7)
    private BigDecimal extraTotal = BigDecimal.ZERO;

    @Excel(name = "移动支付", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private BigDecimal mobilePaymentAmount = BigDecimal.ZERO;

    @Excel(name = "电子支付", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private BigDecimal ePaymentAmount = BigDecimal.ZERO;

    @Excel(name = "公务IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer officialIcCardCount = 0;

    @Excel(name = "军车IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer militaryIcCardCount = 0;

    @Excel(name = "免费IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer freeIcCardCount = 0;

    @Excel(name = "应缴IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer dueIcCardCount = 0;
}
