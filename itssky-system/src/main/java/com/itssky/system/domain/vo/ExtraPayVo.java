package com.itssky.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;


/**
 * 交款记录
 */
@Data
public class ExtraPayVo {


    /**
     * 统计日期
     */
    private Integer staDate;

    /**
     * 统计方式 月份
     */
    private Integer monthDate;

    private Integer operatorId;

    private Integer stationId;

    /**
     * 加收
     */
    private BigDecimal extAddToll = BigDecimal.ZERO;

    /**
     * 现金加收
     */
    private BigDecimal xjMoney = BigDecimal.ZERO;

    /**
     * 移动支付加收
     */
    private BigDecimal ydzfMoney = BigDecimal.ZERO;

}
