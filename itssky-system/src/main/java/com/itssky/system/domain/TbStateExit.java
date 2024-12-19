package com.itssky.system.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author ITSSKY
 * 入口班次统计表
 */
@TableName(value = "tbstatexit", schema = "dbstats")
@Data
public class TbStateExit {

    /**
     * 序号
     */
    @TableId
    private Long ID;

    /**
     * 站编码
     */
    private Integer StationID;

    /**
     * 统计日期
     */
    private int StaDate;

    /**
     * 班次
     */
    private int ShiftID;

    /**
     * 收费员工号
     */
    private Integer OperatorID;

    /**
     * 班组
     */
    private int TeamID;

    /**
     * 车道编号
     */
    private int LaneID;

    /**
     * 终端类型
     */
    private int termMode;

    /**
     * 车型
     */
    private int VehicleClass;

    /**
     * 车情
     */
    private int VehicleStatus;

    /**
     * 交易状态
     */
    private int DealStatus;

    /**
     * 记录类型
     */
    private int RecordType;

    /**
     * 卡类型
     */
    private int CardType;

    /**
     * 发卡数量
     */
    private int CardNum;

    /**
     * 车流量数
     */
    private int CarNum;

    /**
     * 作废标记
     */
    private int CancelFlag;

    /**
     * 传输标记
     */
    private int Transfermark;

    /**
     * 结算时间
     */
    private Date BalanceTime;

    /**
     * 结算人员
     */
    private int BalanceOP;

    /**
     * 备注
     */
    private int Spare;

    /**
     * 支付方式
     */
    private int PayType;

    /**
     * 欠费总金额
     */
    private Integer TotalArrearage;

    /**
     * 应收通行费总金额
     */
    private Integer TotalFee;

    /**
     * 通行费总金额
     */
    private Integer TotalToll;
}
