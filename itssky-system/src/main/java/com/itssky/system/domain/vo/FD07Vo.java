package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class FD07Vo {

    @Excel(name = "收费员工号")
    private Integer operatorId;

    @Excel(name = "收费员姓名")
    private String operatorName;

    @Excel(name = "收费流量")
    private Integer totalCarNum;

    @Excel(name = "回收卡", onlyHeader = true, mergeColumn = 2)
    private String recoverCard;

    @Excel(name = "应缴卡数", headerRow = 3, startColumn = 4)
    private Integer yjCardNum;

    @Excel(name = "实缴卡数", headerRow = 3, startColumn = 5)
    private Integer sjCardNum;

    @Excel(name = "现金收入", onlyHeader = true, mergeColumn = 2)
    private Integer cash;

    @Excel(name = "应缴金额", headerRow = 3, startColumn = 6)
    private Double yjCash;

    @Excel(name = "实缴金额", headerRow = 3, startColumn = 7)
    private Double sjCash;

    @Excel(name = "电子支付")
    private Double ePay;

    @Excel(name = "移动支付")
    private Double mPay;

    @Excel(name = "收费金额合计")
    private Double totalToll;

    /**
     * 加收金额
     */
    private Double addedToll;
}
