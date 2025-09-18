package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

/**
 * TK入出口(MTC)交通流量按车种统计表
 */
@Data
public class TkFlowVo {

    private Integer stationId;

    private String stationName;

    private String staDate;

    private String monthDate;

    @Excel(name = "统计方式", mergeRow = 2)
    private String statType = "";

    @Excel(name = "入口", onlyHeader = true, mergeColumn = 7)
    private String entry;

    @Excel(name = "现金", startColumn = 2, headerRow = 3)
    private int rxj = 0;

    @Excel(name = "电子支付", startColumn = 3, headerRow = 3)
    private int rdz = 0;

    @Excel(name = "公务车", startColumn = 4, headerRow = 3)
    private int rgw = 0;

    @Excel(name = "军车", startColumn = 5, headerRow = 3)
    private int rjc = 0;

    @Excel(name = "优惠车", startColumn = 6, headerRow = 3)
    private int ryh = 0;

    @Excel(name = "车队", startColumn = 7, headerRow = 3)
    private int rcd = 0;

    @Excel(name = "入口小计", startColumn = 8, headerRow = 3)
    private int rsum = 0;

    @Excel(name = "出口", onlyHeader = true, mergeColumn = 9)
    private String exit;

    @Excel(name = "现金", startColumn = 9, headerRow = 3)
    private int cxj = 0;

    @Excel(name = "电子支付", startColumn = 10, headerRow = 3)
    private int cdz = 0;

    @Excel(name = "移动支付", startColumn = 11, headerRow = 3)
    private int cyd = 0;

    @Excel(name = "公务", startColumn = 12, headerRow = 3)
    private int cgw = 0;

    @Excel(name = "军车", startColumn = 13, headerRow = 3)
    private int cjc = 0;

    @Excel(name = "优惠车", startColumn = 14, headerRow = 3)
    private int cyh = 0;

    @Excel(name = "免费车", startColumn = 15, headerRow = 3)
    private int cmf = 0;

    @Excel(name = "车队", startColumn = 16, headerRow = 3)
    private int ccd = 0;

    @Excel(name = "出口小计", startColumn = 17, headerRow = 3)
    private int csum = 0;

    @Excel(name = "总计", mergeRow = 2)
    private int sumCount = 0;

    private boolean totalRow = false;
}
