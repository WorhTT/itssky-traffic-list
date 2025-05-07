package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class FD06Vo {

    @Excel(name = "收费员工号")
    private Integer operateId;

    @Excel(name = "收费员姓名")
    private String operateName;

    @Excel(name = "发卡数")
    private int cardNum;
}
