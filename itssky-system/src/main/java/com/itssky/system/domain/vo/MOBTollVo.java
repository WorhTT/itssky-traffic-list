package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

/**
 * MOB移动支付统计
 */
@Data
public class MOBTollVo {

    private String operatorId;

    private String stationId;

    private String stationName;

    private String monthDate;

    private String StaDate;

    @Excel(name = "统计方式", mergeRow = 2)
    private String statType = "";

    @Excel(name = "银联支付", onlyHeader = true, mergeColumn = 2)
    private String yl;

    @Excel(name = "笔数", startColumn = 2, headerRow = 3)
    private String ylCount = "0";

    @Excel(name = "金额", startColumn = 3, headerRow = 3)
    private String ylToll = "0";

    @Excel(name = "微信支付", onlyHeader = true, mergeColumn = 2)
    private String wx;

    @Excel(name = "笔数", startColumn = 4, headerRow = 3)
    private String wxCount = "0";

    @Excel(name = "金额", startColumn = 5, headerRow = 3)
    private String wxToll = "0";

    @Excel(name = "支付宝支付", onlyHeader = true, mergeColumn = 2)
    private String zfb;

    @Excel(name = "笔数", startColumn = 6, headerRow = 3)
    private String zfbCount = "";

    @Excel(name = "金额", startColumn = 7, headerRow = 3)
    private String zfbToll = "0";

    @Excel(name = "百度支付", onlyHeader = true, mergeColumn = 2)
    private String bd;

    @Excel(name = "笔数", startColumn = 8, headerRow = 3)
    private String bdCount = "0";

    @Excel(name = "金额", startColumn = 9, headerRow = 3)
    private String bdToll = "0";

    @Excel(name = "京东支付", onlyHeader = true, mergeColumn = 2)
    private String jd;

    @Excel(name = "笔数", startColumn = 10, headerRow = 3)
    private String jdCount = "0";

    @Excel(name = "金额", startColumn = 11, headerRow = 3)
    private String jdToll = "0";

    @Excel(name = "通行宝", onlyHeader = true, mergeColumn = 2)
    private String txb;

    @Excel(name = "笔数", startColumn = 12, headerRow = 3)
    private String txbCount = "0";

    @Excel(name = "金额", startColumn = 13, headerRow = 3)
    private String txbToll = "0";

    @Excel(name = "数字人民币", onlyHeader = true, mergeColumn = 2)
    private String szrmb;

    @Excel(name = "笔数", startColumn = 14, headerRow = 3)
    private String szrmbCount = "0";

    @Excel(name = "金额", startColumn = 15, headerRow = 3)
    private String szrmbToll = "0";

    @Excel(name = "其他", onlyHeader = true, mergeColumn = 2)
    private String qt;

    @Excel(name = "笔数", startColumn = 16, headerRow = 3)
    private String qtCount = "0";

    @Excel(name = "金额", startColumn = 17, headerRow = 3)
    private String qtToll = "0";

    @Excel(name = "合计", onlyHeader = true, mergeColumn = 2)
    private String hj;

    @Excel(name = "笔数", startColumn = 18, headerRow = 3)
    private String hjCount = "0";

    @Excel(name = "金额", startColumn = 19, headerRow = 3)
    private String hjToll = "0";

    private boolean totalRow = false;




}
