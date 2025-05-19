package com.itssky.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class FD27Dto {

    private String tableName;

    @NotNull(message = "请选择收费站!")
    private Integer stationId;

    private List<Integer> stationIdList;

    @NotNull(message = "统计日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date beginTime;

    @NotNull(message = "结束日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date endTime;

    private Integer intBeginTime;

    private Integer intEndTime;

    /**
     * 0 1 2
     * 全部 客车 货车
     */
    private String beginVehicleType;

    /**
     * 0 1 2
     * 全部 客车 货车
     */
    private String endVehicleType;
}
