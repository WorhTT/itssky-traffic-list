package com.itssky.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class TollYhDto {

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginTime;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    private int intBeginTime;

    private int intEndTime;

    /**
     * 1 -> 人员
     * 2 -> 日
     * 3 -> 月
     * 4 -> 站
     */
    @NotEmpty(message = "统计方式不能为空")
    private String statType;

    private Integer stationId;

    private List<Integer> stationIdList;

    private List<String> tableNameList;


}
