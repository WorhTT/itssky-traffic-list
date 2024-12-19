package com.itssky.system.domain.vo;
import lombok.*;


/**
 * @author ITSSKY
 * 卡库存表VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbStcVo {

    /**
     * 月份
     */
    private String monthDate;

    private Integer staDate;

    private Integer shiftId;

    private Integer stationId;

    /**
     * 正常卡库存
     */
    private Integer stockNum;

    /**
     * 发出卡数
     */
    private Integer sendNum;

    /**
     * 回收卡数
     */
    private Integer receivedNum;

    /**
     * 调入卡数
     */
    private Integer inNum;

    /**
     * 调出卡数
     */
    private Integer outNum;

    /**
     * 恢复卡数
     */
    private Integer recoverNum;

    /**
     * 坏卡库存数
     */
    private Integer badStockNum;

    /**
     * 坏卡回收数
     */
    private Integer badRecNum;

    /**
     * 坏卡调出(上缴)数
     */
    private Integer badOutNum;

    /**
     * 库存调整数
     */
    private Integer adjustNum;

    /**
     *  结算人员
     */
    private Integer balanceOp;


}
