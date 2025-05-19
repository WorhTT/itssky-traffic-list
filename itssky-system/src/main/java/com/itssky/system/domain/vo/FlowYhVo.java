package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class FlowYhVo {

    private String stationId;

    private String staDate;

    private String monthDate;

    private String stationName;

    @Excel(name = "统计方式")
    private String statType;

    @Excel(name = "集装箱")
    private int jzx = 0;

    @Excel(name = "邮政车")
    private int yz = 0;

    @Excel(name = "绿色通道")
    private int ls = 0;

    @Excel(name = "抗震救灾")
    private int kz = 0;

    @Excel(name = "秸秆车")
    private int jg = 0;

    @Excel(name = "运管苏通卡货车")
    private int yg = 0;

    @Excel(name = "军车")
    private int jc = 0;

    @Excel(name = "专用工作卡")
    private int zy = 0;

    @Excel(name = "收割机")
    private int sgj = 0;

    @Excel(name = "中欧班列")
    private int zobl = 0;

    @Excel(name = "应急车")
    private int yj = 0;

    @Excel(name = "大件运输")
    private int djys = 0;

    private boolean totalRow = false;
}
