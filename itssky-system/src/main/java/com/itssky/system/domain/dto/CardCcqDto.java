package com.itssky.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class CardCcqDto {

    @NotNull(message = "统计日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
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
