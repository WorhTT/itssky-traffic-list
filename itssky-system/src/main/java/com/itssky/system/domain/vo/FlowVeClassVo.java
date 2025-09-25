package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

/**
 * 入出口流量按车型统计表
 */
@Data
public class FlowVeClassVo {

    private String stationId;

    @Excel(name = "统计方式", mergeRow = 2)
    private String statType;

    @Excel(name = "入口车道交通流量", mergeColumn = 8)
    private String rk;

    @Excel(name = "一车型", headerRow = 3, startColumn = 2)
    private int r1 = 0;

    @Excel(name = "二车型", headerRow = 3, startColumn = 3)
    private int r2 = 0;

    @Excel(name = "三车型", headerRow = 3, startColumn = 4)
    private int r3 = 0;

    @Excel(name = "四车型", headerRow = 3, startColumn = 5)
    private int r4 = 0;

    @Excel(name = "五车型", headerRow = 3, startColumn = 6)
    private int r5 = 0;

    @Excel(name = "六车型", headerRow = 3, startColumn = 7)
    private int r6 = 0;

    @Excel(name = "专项车", headerRow = 3, startColumn = 8)
    private int rzx = 0;

    @Excel(name = "小计", headerRow = 3, startColumn = 9)
    private int rsum = 0;

    @Excel(name = "出口车道交通流量", mergeColumn = 8)
    private String ck;

    @Excel(name = "一车型", headerRow = 3, startColumn = 10)
    private int c1 = 0;

    @Excel(name = "二车型", headerRow = 3, startColumn = 11)
    private int c2 = 0;

    @Excel(name = "三车型", headerRow = 3, startColumn = 12)
    private int c3 = 0;

    @Excel(name = "四车型", headerRow = 3, startColumn = 13)
    private int c4 = 0;

    @Excel(name = "五车型", headerRow = 3, startColumn = 14)
    private int c5 = 0;

    @Excel(name = "六车型", headerRow = 3, startColumn = 15)
    private int c6 = 0;

    @Excel(name = "专项车", headerRow = 3, startColumn = 16)
    private int czx = 0;

    @Excel(name = "小计", headerRow = 3, startColumn = 17)
    private int csum = 0;

    @Excel(name = "总计", mergeRow = 2)
    private int allSum;

    private boolean totalRow;
}
