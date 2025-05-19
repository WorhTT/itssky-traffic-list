package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class FD29Vo {

    @Excel(name = "收费员工号")
    private String operatorId = "0";

    @Excel(name = "收费员姓名")
    private String operatorName = "";

    @Excel(name = "总车辆数")
    private Integer totalCarNum = 0;

    @Excel(name = "升档车辆数")
    private Integer sdCarNum = 0;

    @Excel(name = "升档比例(%)")
    private Double sdCarRate = 0D;
}
