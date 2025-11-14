package com.itssky.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 自助卡机求助响应VO
 */
@Data
public class CardboxResortVo {

    private Integer stationId;

    @Excel(name = "收费站")
    private String stationName;

    @Excel(name = "车道号")
    private Integer laneId;

    private Integer operatorId;

    @Excel(name = "收费员")
    private String operatorName;

    @Excel(name = "车牌")
    private String vehicleLicense;

    @Excel(name = "求助时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date resortTime;

    @Excel(name = "响应时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date operateTime;

    /**
     * 耗时毫秒数
     */
    private long durationMills;

    /**
     * 最后结果为 x小时x分钟x秒
     */
    @Excel(name = "响应时长")
    private String duration;

    /**
     * 8-> 取消报警
     * 24 -> 求助
     */
    private Integer keyType;

    /**
     * 操作时间
     */
    private Date inTime;
}
