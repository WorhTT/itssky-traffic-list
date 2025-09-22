package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

@Data
public class Ccq2CardVo {

    private String stationId;

    @Excel(name = "收费站", mergeRow = 2)
    private String stationName = "";

    @Excel(name = "库存增加", onlyHeader = true, mergeColumn = 4)
    private String kczj;

    @Excel(name = "通行卡调入", startColumn = 2, headerRow = 3)
    private int txkdr = 0;

    @Excel(name = "出口回收", startColumn = 3, headerRow = 3)
    private int ckhs = 0;

    @Excel(name = "坏卡回收", startColumn = 4, headerRow = 3)
    private int hkhs = 0;

    @Excel(name = "通行卡恢复", startColumn = 5, headerRow = 3)
    private int txkhf = 0;

    @Excel(name = "库存减少", onlyHeader = true, mergeColumn = 3)
    private String kcjs;

    @Excel(name = "通行卡调出", startColumn = 6, headerRow = 3)
    private int txkdc = 0;

    @Excel(name = "入口发卡", startColumn = 7, headerRow = 3)
    private int rkfk = 0;

    @Excel(name = "坏卡上缴", startColumn = 8, headerRow = 3)
    private int hksj = 0;

    @Excel(name = "库存维护数", mergeRow = 2)
    private int kcwhs = 0;

    @Excel(name = "库存变动", mergeRow = 2)
    private int kcbd = 0;

    @Excel(name = "库存坏卡", mergeRow = 2)
    private int kchk = 0;

    @Excel(name = "库存正常卡", mergeRow = 2)
    private int kczck = 0;

    private boolean totalRow;
}
