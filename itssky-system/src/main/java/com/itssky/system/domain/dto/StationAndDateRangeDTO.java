package com.itssky.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class StationAndDateRangeDTO {

    @NotNull(message = "站点不能为空!")
    private Integer stationId;

    @NotNull(message = "统计开始日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date beginTime;

    @NotNull(message = "统计结束日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date endTime;

    private List<Integer> stationIdList;

    private Integer intBeginTime;

    private Integer intEndTime;

    private List<String> tableNameList;
}
