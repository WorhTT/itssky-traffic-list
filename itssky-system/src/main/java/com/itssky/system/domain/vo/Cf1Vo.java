package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class Cf1Vo {

    private Integer stationId;

    @Excel(name = "收费站")
    private String stationName;

    @Excel(name = "通行费收入总额", mergeColumn = 6, headerRow = 2, mergeRow = 1, onlyHeader = true)
    private String tollAmount;

    @Excel(name = "统计金额", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 1)
    private Double statAmount = 0D;

    @Excel(name = "应缴金额", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 2)
    private Double dueAmount = 0D;

    @Excel(name = "实缴金额", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 3)
    private Double paidAmount = 0D;

    @Excel(name = "金额差异", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 4)
    private Double amountDiff = 0D;

    @Excel(name = "欠款", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 5)
    private Double arrearsAmount = 0D;

    @Excel(name = "加收款", mergeRow = 2, headerRow = 3, mergeColumn = 1, startColumn = 6)
    private Double extraTotal = 0D;

    @Excel(name = "移动支付", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Double mobilePaymentAmount = 0D;

    @Excel(name = "电子支付", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Double ePaymentAmount = 0D;

    @Excel(name = "公务IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer officialIcCardCount = 0;

    @Excel(name = "军车IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer militaryIcCardCount = 0;

    @Excel(name = "免费IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer freeIcCardCount = 0;

    @Excel(name = "应缴IC卡", mergeRow = 3, headerRow = 2, mergeColumn = 1)
    private Integer dueIcCardCount = 0;

    private boolean totalRow = false;

    private Double totalToll;
}
