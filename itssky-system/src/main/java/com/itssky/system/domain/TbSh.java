package com.itssky.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 交款记录表
 */
@Data
@AllArgsConstructor
@NotNull
@Builder
@ToString
@TableName(value = "tbsh")
public class TbSh {

    /**
     * 序号
     */
    @TableId(value = "ID")
    public Integer ID;

    /**
     * 网络编号
     */
    @TableField(value = "Network")
    public Integer Network;

    /**
     * 站编码
     */
    @TableField(value = "StationID")
    public Integer StationID;

    /**
     * 统计日期
     */
    @TableField(value = "StaDate")
    public Integer StaDate;

    /**
     * 班次
     */
    @TableField(value = "ShiftID")
    public Integer ShiftID;

    /**
     * 收费员工号
     */
    @TableField(value = "OperatorID")
    public Integer OperatorID;

    /**
     * 班组
     */
    @TableField(value = "TeamID")
    public Integer TeamID;

    /**
     * 通行卡实收数
     */
    @TableField(value = "HandInCNum")
    public Integer HandInCNum;

    /**
     * 通行卡实发数
     */
    @TableField(value = "HandOutCNum")
    public Integer HandOutCNum;

    /**
     * 实缴通行费
     */
    @TableField(value = "HandToll")
    public Integer HandToll;

    /**
     * 定额票张数
     */
    @TableField(value = "TicketNum")
    public Integer TicketNum;

    /**
     * 定额票金额
     */
    @TableField(value = "TicketFee")
    public Integer TicketFee;

    /**
     * 废票张数
     */
    @TableField(value = "FTicketNum")
    public Integer FTicketNum;

    /**
     * 废票金额
     */
    @TableField(value = "FTicketFee")
    public Integer FTicketFee;

    /**
     * 加收款
     */
    @TableField(value = "AddedToll")
    public Integer AddedToll;

    /**
     * 作废标记
     */
    @TableField(value = "CancelFlag")
    public Integer CancelFlag;

    /**
     * 传输标记
     */
    @TableField(value = "Transfermark")
    public Integer Transfermark;

    /**
     * 记录时间
     */
    @TableField(value = "RecordTime")
    public Date RecordTime;

    /**
     * 操作人员
     */
    @TableField(value = "RecordOP")
    public Integer RecordOP;

    /**
     * 入出口标记 0入口 1出口
     */
    @TableField(value = "Flag")
    public Integer Flag;

    /**
     * 对账状态
     */
    @TableField(value = "Spare")
    public String Spare;
}
