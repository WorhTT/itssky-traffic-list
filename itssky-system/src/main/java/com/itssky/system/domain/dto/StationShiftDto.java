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
@ToString
@Builder
public class StationShiftDto {

    private List<List<Integer>> stationIdArray;

    private List<Integer> stationIdList;

    @NotNull(message = "收费站不能为空")
    private Integer stationId;

    @NotNull(message = "统计日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date time;

    private Integer shiftId;

    private Integer timeFormat;


}
