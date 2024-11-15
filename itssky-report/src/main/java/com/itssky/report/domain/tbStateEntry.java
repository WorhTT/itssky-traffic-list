package com.itssky.report.domain;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ITSSKY
 * 入口班次统计表
 */
@Data
public class tbStateEntry {

    /**
     * 序号
     */
    private int ID;

    /**
     * 站编码
     */
    private Long StationID;

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
    private Long OperatorID;

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
    private Double TotalArrearage;

    /**
     * 应收通行费总金额
     */
    private Double TotalFee;

    /**
     * 通行费总金额
     */
    private Double TotalToll;
}
