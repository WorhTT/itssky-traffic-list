package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class FlowGroupVo {

    private String stationName;

    private String stationId;

    private String monthDate;

    private String staDate;

    @Excel(name = "统计方式", mergeRow = 2)
    private String statType;

    @Excel(name = "客一", onlyHeader = true, mergeColumn = 2)
    private String k1;

    @Excel(name = "C卡", startColumn = 2, headerRow = 3)
    private int k1c = 0;

    @Excel(name = "D卡", startColumn = 3, headerRow = 3)
    private int k1d = 0;

    @Excel(name = "客二", onlyHeader = true, mergeColumn = 2)
    private String k2;

    @Excel(name = "C卡", startColumn = 4, headerRow = 3)
    private int k2c = 0;

    @Excel(name = "D卡", startColumn = 5, headerRow = 3)
    private int k2d = 0;

    @Excel(name = "客三", onlyHeader = true, mergeColumn = 2)
    private String k3;

    @Excel(name = "C卡", startColumn = 6, headerRow = 3)
    private int k3c = 0;

    @Excel(name = "D卡", startColumn = 7, headerRow = 3)
    private int k3d = 0;

    @Excel(name = "客四", onlyHeader = true, mergeColumn = 2)
    private String k4;

    @Excel(name = "C卡", startColumn = 8, headerRow = 3)
    private int k4c = 0;

    @Excel(name = "D卡", startColumn = 9, headerRow = 3)
    private int k4d = 0;

    @Excel(name = "客车小计", onlyHeader = true, mergeColumn = 2)
    private String kAmount;

    @Excel(name = "C卡", startColumn = 10, headerRow = 3)
    private int kAmountc = 0;

    @Excel(name = "D卡", startColumn = 11, headerRow = 3)
    private int kAmountd = 0;

    @Excel(name = "货一", onlyHeader = true, mergeColumn = 2)
    private String h1;

    @Excel(name = "C卡", startColumn = 12, headerRow = 3)
    private int h1c = 0;

    @Excel(name = "D卡", startColumn = 13, headerRow = 3)
    private int h1d = 0;

    @Excel(name = "货二", onlyHeader = true, mergeColumn = 2)
    private String h2;

    @Excel(name = "C卡", startColumn = 14, headerRow = 3)
    private int h2c = 0;

    @Excel(name = "D卡", startColumn = 15, headerRow = 3)
    private int h2d = 0;

    @Excel(name = "货三", onlyHeader = true, mergeColumn = 2)
    private String h3;

    @Excel(name = "C卡", startColumn = 16, headerRow = 3)
    private int h3c = 0;

    @Excel(name = "D卡", startColumn = 17, headerRow = 3)
    private int h3d = 0;

    @Excel(name = "货四", onlyHeader = true, mergeColumn = 2)
    private String h4;

    @Excel(name = "C卡", startColumn = 18, headerRow = 3)
    private int h4c = 0;

    @Excel(name = "D卡", startColumn = 19, headerRow = 3)
    private int h4d = 0;

    @Excel(name = "货五", onlyHeader = true, mergeColumn = 2)
    private String h5;

    @Excel(name = "C卡", startColumn = 20, headerRow = 3)
    private int h5c = 0;

    @Excel(name = "D卡", startColumn = 21, headerRow = 3)
    private int h5d = 0;

    @Excel(name = "货六", onlyHeader = true, mergeColumn = 2)
    private String h6;

    @Excel(name = "C卡", startColumn = 22, headerRow = 3)
    private int h6c = 0;

    @Excel(name = "D卡", startColumn = 23, headerRow = 3)
    private int h6d = 0;

    @Excel(name = "货车小计", onlyHeader = true, mergeColumn = 2)
    private String hAmount;

    @Excel(name = "C卡", startColumn = 24, headerRow = 3)
    private int hAmountc = 0;

    @Excel(name = "D卡", startColumn = 25, headerRow = 3)
    private int hAmountd = 0;

    @Excel(name = "专一", onlyHeader = true, mergeColumn = 2)
    private String z1;

    @Excel(name = "C卡", startColumn = 26, headerRow = 3)
    private int z1c = 0;

    @Excel(name = "D卡", startColumn = 27, headerRow = 3)
    private int z1d = 0;

    @Excel(name = "专二", onlyHeader = true, mergeColumn = 2)
    private String z2;

    @Excel(name = "C卡", startColumn = 28, headerRow = 3)
    private int z2c = 0;

    @Excel(name = "D卡", startColumn = 29, headerRow = 3)
    private int z2d = 0;

    @Excel(name = "专三", onlyHeader = true, mergeColumn = 2)
    private String z3;

    @Excel(name = "C卡", startColumn = 30, headerRow = 3)
    private int z3c = 0;

    @Excel(name = "D卡", startColumn = 31, headerRow = 3)
    private int z3d = 0;

    @Excel(name = "专四", onlyHeader = true, mergeColumn = 2)
    private String z4;

    @Excel(name = "C卡", startColumn = 32, headerRow = 3)
    private int z4c = 0;

    @Excel(name = "D卡", startColumn = 33, headerRow = 3)
    private int z4d = 0;

    @Excel(name = "专五", onlyHeader = true, mergeColumn = 2)
    private String z5;

    @Excel(name = "C卡", startColumn = 34, headerRow = 3)
    private int z5c = 0;

    @Excel(name = "D卡", startColumn = 35, headerRow = 3)
    private int z5d = 0;

    @Excel(name = "专六", onlyHeader = true, mergeColumn = 2)
    private String z6;

    @Excel(name = "C卡", startColumn = 36, headerRow = 3)
    private int z6c = 0;

    @Excel(name = "D卡", startColumn = 37, headerRow = 3)
    private int z6d = 0;

    @Excel(name = "专车小计", onlyHeader = true, mergeColumn = 2)
    private String zAmount;

    @Excel(name = "C卡", startColumn = 38, headerRow = 3)
    private int zAmountc = 0;

    @Excel(name = "D卡", startColumn = 39, headerRow = 3)
    private int zAmountd = 0;

    @Excel(name = "总计", onlyHeader = true, mergeColumn = 3)
    private String allAmount;

    @Excel(name = "C卡", startColumn = 40, headerRow = 3)
    private int allAmountc = 0;

    @Excel(name = "D卡", startColumn = 41, headerRow = 3)
    private int allAmountd = 0;

    @Excel(name = "合计", startColumn = 42, headerRow = 3)
    private int total = 0;

    private boolean totalRow;
}
