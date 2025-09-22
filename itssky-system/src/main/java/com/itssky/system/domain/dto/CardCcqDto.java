package com.itssky.system.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class CardCcqDto {

    @NotNull(message = "统计日期不能为空")
    private Date time;

    @NotNull(message = "收费站不能为空")
    private Integer stationId;

    /**
     * 1 -> 日表
     * 2 -> 月表
     */
    private String flag;

    private String tableName;

    private String staDate;

    private int intBeginDate;

    private int intEndDate;

    private List<Integer> stationIdList;
}
