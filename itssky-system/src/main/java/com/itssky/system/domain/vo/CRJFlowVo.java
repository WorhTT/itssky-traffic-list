package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class CRJFlowVo {

    @Excel(name = "统计方式")
    private String statType = "";

    private Integer stationId;

    private String stationName = "";

    private String staDate;

    private String monthDate;

    @Excel(name = "客一")
    private int k1 = 0;

    @Excel(name = "客二")
    private int k2 = 0;

    @Excel(name = "客三")
    private int k3 = 0;

    @Excel(name = "客四")
    private int k4 = 0;

    @Excel(name = "客车小计")
    private int kSum = 0;

    @Excel(name = "货一")
    private int h1 = 0;

    @Excel(name = "货二")
    private int h2 = 0;

    @Excel(name = "货三")
    private int h3 = 0;

    @Excel(name = "货四")
    private int h4 = 0;

    @Excel(name = "货五")
    private int h5 = 0;

    @Excel(name = "货六")
    private int h6 = 0;

    @Excel(name = "货车小计")
    private int hSum = 0;

    @Excel(name = "专一")
    private int z1 = 0;

    @Excel(name = "专二")
    private int z2 = 0;

    @Excel(name = "专三")
    private int z3 = 0;

    @Excel(name = "专四")
    private int z4 = 0;

    @Excel(name = "专五")
    private int z5 = 0;

    @Excel(name = "专六")
    private int z6 = 0;

    @Excel(name = "专车小计")
    private int zSum = 0;

    @Excel(name = "公务")
    private int gw = 0;

    @Excel(name = "军车")
    private int jc = 0;

    @Excel(name = "优惠")
    private int yh = 0;

    @Excel(name = "免费")
    private int mf = 0;

    @Excel(name = "车队")
    private int cd;

    @Excel(name = "客车比例(%)")
    private Double kcbl = 0D;

    @Excel(name = "货车比例(%)")
    private Double hcbl = 0D;

    @Excel(name = "专车比例(%)")
    private Double zcbl = 0D;

    @Excel(name = "总计")
    private int sumCount = 0;

    private boolean totalRow = false;

}
