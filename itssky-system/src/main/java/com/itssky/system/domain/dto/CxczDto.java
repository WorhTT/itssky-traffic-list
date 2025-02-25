package com.itssky.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class CxczDto {

    private Integer stationId;

    @NotNull(message = "统计开始日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date beginTime;

    @NotNull(message = "统计结束日期不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date endTime;

    private List<Integer> stationIdList;

    /**
     * 0 全部
     * 1 大件运输
     * 2 限重27吨(3轴)
     * 3 限重32吨(4轴)
     * 4 限重35吨(4轴)
     * 5 限重36吨(4轴)
     * 6 限重37吨(4轴)
     * 7 限重55吨(特种)
     * 8 其他超限放行
     * 9 超限拦截
     */
    private String cxczType;

    private Integer intBeginTime;

    private Integer intEndTime;

    private List<String> tableNameList;

}
