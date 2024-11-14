package com.itssky.report.domain;

import com.itssky.common.annotation.Excel;
import lombok.Data;

/**
 * 高速入口流量报表信息
 * @author ITSSKY
 */
@Data
public class ReportFlowInfo {

    /**
     * 站点名称
     */
    @Excel(name = "站点名称")
    private String stationName;

    @Excel(name = "客一")
    private int k1;

    @Excel(name = "客二")
    private int k2;

    @Excel(name = "客三")
    private int k3;

    @Excel(name = "客四")
    private int k4;

    @Excel(name = "客车小计")
    private int kAmount;

    @Excel(name = "货一")
    private int h1;

    @Excel(name = "货二")
    private int h2;

    @Excel(name = "货三")
    private int h3;

    @Excel(name = "货四")
    private int h4;

    @Excel(name = "货五")
    private int h5;

    @Excel(name = "货六")
    private int h6;

    @Excel(name = "货车小计")
    private int hAmount;

    @Excel(name = "专一")
    private int z1;

    @Excel(name = "专二")
    private int z2;

    @Excel(name = "专三")
    private int z3;

    @Excel(name = "专四")
    private int z4;

    @Excel(name = "专五")
    private int z5;

    @Excel(name = "专六")
    private int z6;

    @Excel(name = "专车小计")
    private int zAmount;

    @Excel(name = "公务")
    private int official;

    @Excel(name = "军车")
    private int military;

    @Excel(name = "优惠")
    private int discount;

    @Excel(name = "免费")
    private int free;

    @Excel(name = "车队")
    private int fleet;

    @Excel(name = "总计")
    private int allAmount;
}
