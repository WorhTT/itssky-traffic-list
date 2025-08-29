package com.itssky.system.domain.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * 交款记录表Vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbShVo {

    private String stationName;

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
    private BigDecimal handToll = BigDecimal.ZERO;

    /**
     * 加收款
     */
    private BigDecimal addedToll = BigDecimal.ZERO;

    /**
     * 统计日期
     */
    private Integer staDate;

    /**
     * 统计方式 月份
     */
    private Integer monthDate;

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
