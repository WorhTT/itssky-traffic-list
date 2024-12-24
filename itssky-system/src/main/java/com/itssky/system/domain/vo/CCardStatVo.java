package com.itssky.system.domain.vo;


import com.itssky.common.annotation.Excel;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CCardStatVo {
    @Excel(name = "班次")
    private Integer shiftId;

    @Excel(name = "工号")
    private Integer operatorId;

    @Excel(name = "客一")
    private Integer cust1 = 0;

    @Excel(name = "客二")
    private Integer cust2 = 0;

    @Excel(name = "客三")
    private Integer cust3 = 0;

    @Excel(name = "客四")
    private Integer cust4 = 0;

    @Excel(name = "客车小计")
    private Integer custSubTotal = 0;

    @Excel(name = "货一")
    private Integer truck1 = 0;

    @Excel(name = "货二")
    private Integer truck2 = 0;

    @Excel(name = "货三")
    private Integer truck3 = 0;

    @Excel(name = "货四")
    private Integer truck4 = 0;

    @Excel(name = "货五")
    private Integer truck5 = 0;

    @Excel(name = "货六")
    private Integer truck6 = 0;

    @Excel(name = "货车小计")
    private Integer truckSubTotal = 0;

    @Excel(name = "专一")
    private Integer spec1 = 0;

    @Excel(name = "专二")
    private Integer spec2 = 0;

    @Excel(name = "专三")
    private Integer spec3 = 0;

    @Excel(name = "专四")
    private Integer spec4 = 0;

    @Excel(name = "专五")
    private Integer spec5 = 0;

    @Excel(name = "专六")
    private Integer spec6 = 0;

    @Excel(name = "专车小计")
    private Integer specSubTotal = 0;

    @Excel(name = "军车")
    private Integer militaryNum = 0;

    @Excel(name = "公务")
    private Integer officialNum = 0;

    @Excel(name = "车队")
    private Integer fleetNum = 0;

    @Excel(name = "优惠")
    private Integer preferNum = 0;

    @Excel(name = "ETC")
    private Integer etcNum = 0;

    @Excel(name = "纸券")
    private Integer paperNum = 0;


    /**
     * 应发卡 = 客小计 + 货小计 + 专小计
     */
    @Excel(name = "应收卡")
    private Integer issuedNum = 0;

    /**
     *
     */
    @Excel(name = "实收卡")
    private Integer actualNum = 0;

    @Excel(name = "无卡")
    private Integer noneNum = 0;

    @Excel(name = "卡损")
    private Integer badNum = 0;

    /**
     * 总流量 = 应发卡 + 公务 + 车队 + 优惠 + ETC
     */
    @Excel(name = "总流量")
    private Integer totalFlow = 0;
}
