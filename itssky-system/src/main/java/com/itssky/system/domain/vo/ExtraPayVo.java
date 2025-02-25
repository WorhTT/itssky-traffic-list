package com.itssky.system.domain.vo;

import lombok.Data;


/**
 * 交款记录
 */
@Data
public class ExtraPayVo {

    /**
     * 加收
     */
    private Double extAddToll;

    /**
     * 现金加收
     */
    private Double xjmoney;

    /**
     * 移动支付加收
     */
    private Double ydzfmoney;

}
