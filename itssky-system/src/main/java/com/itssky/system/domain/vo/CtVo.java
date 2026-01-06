package com.itssky.system.domain.vo;


import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CtVo {

    private String staDate;

    private String monthDate;

    private String stationId;

    private String stationName;

    @Excel(name = "统计方式")
    private String statType;
    
    @Excel(name = "客一")
    private int k1 = 0;
    
    @Excel(name = "客二")
    private int k2 = 0;
    
    @Excel(name = "客三")
    private int k3 = 0;
    
    @Excel(name = "客四")
    private int k4 = 0;
    
    @Excel(name = "客车小计")
    private int ksum = 0;
    
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
    private int hsum = 0;

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
    private int zsum = 0;

    @Excel(name = "客车比例(%)")
    private BigDecimal kbl = BigDecimal.ZERO;

    @Excel(name = "货车比例(%)")
    private BigDecimal hbl = BigDecimal.ZERO;

    @Excel(name = "专车比例(%)")
    private BigDecimal zbl = BigDecimal.ZERO;

    @Excel(name = "总计")
    private int total = 0;
    
    private boolean totalRow = false;
}
