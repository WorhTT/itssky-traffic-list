package com.itssky.system.domain;

import com.itssky.common.annotation.Excel;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 高速入口流量报表信息
 * @author ITSSKY
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReportFlowInfo {

    private Integer staDate;

    private String monthDate;

    private String hour;

    private Integer stationId;

    private String stationName;

    @Excel(name = "统计方式", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private String statType;

    @Excel(name = "客一", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int k1;

    @Excel(name = "客二", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int k2;

    @Excel(name = "客三", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int k3;

    @Excel(name = "客四", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int k4;

    @Excel(name = "客车小计", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int kAmount;

    @Excel(name = "货一", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int h1;

    @Excel(name = "货二", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int h2;

    @Excel(name = "货三", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int h3;

    @Excel(name = "货四", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int h4;

    @Excel(name = "货五", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int h5;

    @Excel(name = "货六", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int h6;

    @Excel(name = "货车小计", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int hAmount;

    @Excel(name = "专一", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int z1;

    @Excel(name = "专二", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int z2;

    @Excel(name = "专三", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int z3;

    @Excel(name = "专四", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int z4;

    @Excel(name = "专五", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int z5;

    @Excel(name = "专六", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int z6;

    @Excel(name = "专车小计", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int zAmount;

    @Excel(name = "公务", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int official;

    @Excel(name = "军车", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int military;

    @Excel(name = "优惠", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int discount;

    @Excel(name = "免费", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int free;

    @Excel(name = "车队", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int fleet;

    @Excel(name = "总计", mergeRow = 1, mergeColumn = 1, headerRow = 2)
    private int allAmount;
}
