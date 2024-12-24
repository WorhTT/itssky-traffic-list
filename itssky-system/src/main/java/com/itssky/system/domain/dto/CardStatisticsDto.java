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
public class CardStatisticsDto {

    private List<List<Integer>> stationIdArray;

    private Integer stationId;

    @NotNull(message = "统计日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date time;

    private List<Integer> stationIdList;

    private Integer shiftId;

    private Integer intTime;

    /**
     * 0  查entry表
     * 1  查exit表
     */
    private Integer tableFlag = 0;
}
