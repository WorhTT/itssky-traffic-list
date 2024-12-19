package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.*;

/**
 * CDT通行卡回收统计表
 * @author ITSSKY
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CdtStatisticsVo {

    @Excel(name = "统计方式", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private String statType;

    private Integer staDate;

    private String monthDate;

    private Integer stationId;

    private String stationName;

    @Excel(name = "客一", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer cust1 = 0;

    @Excel(name = "客二", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer cust2 = 0;

    @Excel(name = "客三", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer cust3 = 0;

    @Excel(name = "客四", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer cust4 = 0;

    @Excel(name = "客车小计", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer custSubTotal = 0;

    @Excel(name = "货一", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer truck1 = 0;

    @Excel(name = "货二", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer truck2 = 0;

    @Excel(name = "货三", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer truck3 = 0;

    @Excel(name = "货四", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer truck4 = 0;

    @Excel(name = "货五", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer truck5 = 0;

    @Excel(name = "货六", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer truck6 = 0;

    @Excel(name = "货车小计", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer truckSubTotal = 0;

    @Excel(name = "专一", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer spec1 = 0;

    @Excel(name = "专二", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer spec2 = 0;

    @Excel(name = "专三", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer spec3 = 0;

    @Excel(name = "专四", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer spec4 = 0;

    @Excel(name = "专五", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer spec5 = 0;

    @Excel(name = "专六", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer spec6 = 0;

    @Excel(name = "专车小计", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer specSubTotal = 0;

    @Excel(name = "军车", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer militaryNum = 0;

    @Excel(name = "公务", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer officialNum = 0;

    @Excel(name = "车队", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer fleetNum = 0;

    @Excel(name = "优惠", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer preferNum = 0;

//    @Excel(name = "无卡", mergeRow = 1, mergeColumn = 1, headerRow = 2)
//    private Integer noneNum = 0;
//
//    @Excel(name = "卡损", mergeRow = 1, mergeColumn = 1, headerRow = 2)
//    private Integer badNum = 0;

    @Excel(name = "ETC", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer etcNum = 0;

    @Excel(name = "纸券", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer paperNum = 0;

    @Excel(name = "应收卡", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer issuedNum = 0;

    @Excel(name = "实收卡", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer actualNum = 0;

    @Excel(name = "总流量", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private Integer totalFlow = 0;
}
