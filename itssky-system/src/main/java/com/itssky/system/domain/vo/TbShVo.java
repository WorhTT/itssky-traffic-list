package com.itssky.system.domain.vo;

import lombok.*;

/**
 * 交款记录表Vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbShVo {

    /**
     * 收费员工号
     */
    private Integer operatorId;

    /**
     * 班次ID
     */
    private Integer shiftId;

    /**
     * 实缴金额
     */
    private Integer handToll;

    /**
     * 加收款
     */
    private Integer addedToll;

    /**
     * 统计日期
     */
    private Integer staDate;

    /**
     * 统计方式 月份
     */
    private String monthDate;

    /**
     * 收费站ID
     */
    private Integer stationId;

    /**
     * 通行卡实收数
     */
    private Integer handInCNum;

    /**
     * 通行卡实发数
     */
    private Integer handOutCNum;

}
