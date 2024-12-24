package com.itssky.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author ITSSKY
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CardStatisticsDtoV2 {

    private Integer stationId;

    private List<List<Integer>> stationIdArray;

    @NotNull(message = "统计开始日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date beginTime;

    @NotNull(message = "统计结束日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date endTime;

    private List<Integer> stationIdList;

    private String statisticsType;

    private Integer shiftId;

    private Integer intBeginTime;

    private Integer intEndTime;

    private List<String> tableNameList;
}
