package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class FD07Vo {

    @Excel(name = "收费员工号", mergeRow = 2)
    private Integer operatorId = 0;

    @Excel(name = "收费员姓名", mergeRow = 2)
    private String operatorName = "";

    @Excel(name = "收费流量", mergeRow = 2)
    private Integer totalCarNum = 0;

    @Excel(name = "回收卡", onlyHeader = true, mergeColumn = 2)
    private String recoverCard;

    @Excel(name = "应缴卡数", headerRow = 3, startColumn = 4)
    private Integer yjCardNum = 0;

    @Excel(name = "实缴卡数", headerRow = 3, startColumn = 5)
    private Integer sjCardNum = 0;

    @Excel(name = "现金收入", onlyHeader = true, mergeColumn = 2)
    private Integer cash = 0;

    @Excel(name = "应缴金额", headerRow = 3, startColumn = 6)
    private Double yjCash = 0D;

    @Excel(name = "实缴金额", headerRow = 3, startColumn = 7)
    private Double sjCash = 0D;

    @Excel(name = "电子支付", mergeRow = 2)
    private Double ePay = 0D;

    @Excel(name = "移动支付", mergeRow = 2)
    private Double mPay = 0D;

    @Excel(name = "收费金额合计", mergeRow = 2)
    private Double totalToll = 0D;

    /**
     * 加收金额
     */
    private Double addedToll = 0D;
}
