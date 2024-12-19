package com.itssky.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Queue;

/**
 * @author ITSSKY
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FtStationDto {

    @NotEmpty(message = "收费站不能为空!")
    private List<List<Integer>> stationIdArray;

    @NotNull(message = "统计日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date beginTime;

    @NotNull(message = "结束日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date endTime;

    @NotNull(message = "统计类型不能为空!")
    private String statisticsType;

    private List<String> tableNameList;

    private Integer intBeginTime;

    private Integer intEndTime;

    private List<Integer> stationIdList;

}
