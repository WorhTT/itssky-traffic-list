package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.page.TableDataInfo;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.dto.CardStatisticsDto;
import com.itssky.system.domain.dto.CardStatisticsDtoV2;
import com.itssky.system.domain.vo.*;
import com.itssky.system.mapper.CardMapper;
import com.itssky.system.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通行卡类报表
 * @author ITSSKY
 */
@Slf4j
@RestController
@RequestMapping("/card")
public class CardController extends BaseController {


    @Autowired
    private CardService cardService;


    /**
     * S1收费站通行卡发放班统计表
     */
    @PostMapping(value = "/s1station")
    public TableDataVo s1StationShift(@RequestBody @Valid CardStatisticsDto dto) {
        List<CardStatisticsVo> result = cardService.s1StationShift(dto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(result);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getTime(), dto.getShiftId()));
        return tableDataVo;
    }

    /**
     * 导出S1收费站通行卡发放班统计表
     */
    @PostMapping(value = "/export/s1station")
    public AjaxResult exportS1CardStation(@RequestBody @Valid CardStatisticsDto dto) throws IOException{
        ExportVo exportVo = cardService.getSCardStationShift(dto);
        List<SCardStatVo> result = exportVo.getResult().stream().filter(i -> i instanceof SCardStatVo)
                .map(i -> (SCardStatVo) i)
                .collect(Collectors.toList());
        List<String> conditionList = exportVo.getConditionList();
        ExcelUtil<SCardStatVo> util = new ExcelUtil<SCardStatVo>(SCardStatVo.class);
        return util.exportDynamic(result, "S1收费站通行卡发放班统计表", conditionList, 31);
    }

    /**
     * 导出C1收费站通行卡发放班统计表
     */
    @PostMapping(value = "/export/c1station")
    public AjaxResult exportC1CardStation(@RequestBody @Valid CardStatisticsDto dto) throws IOException{
        ExportVo exportVo = cardService.getCCardStationShift(dto);
        List<CCardStatVo> result = exportVo.getResult().stream().filter(i -> i instanceof CCardStatVo)
                .map(i -> (CCardStatVo) i)
                .collect(Collectors.toList());
        List<String> conditionList = exportVo.getConditionList();
        ExcelUtil<CCardStatVo> util = new ExcelUtil<CCardStatVo>(CCardStatVo.class);
        return util.exportDynamic(result, "C1收费站通行卡回收班统计表", conditionList, 32);
    }

    /**
     * S2收费站通行卡发放日统计表
     */
    @PostMapping(value = "/s2station")
    public TableDataInfo s2StationShift(@RequestBody @Valid CardStatisticsDto dto) {
        return getDataTable(cardService.s2StationShift(dto));
    }

    /**
     * 导出S2收费站通行卡发放日统计表
     */
    @PostMapping(value = "/export/s2station")
    public AjaxResult exportS2CardStation(@RequestBody @Valid CardStatisticsDto dto) throws IOException{
        ExportVo exportVo = cardService.getSCardStationDay(dto);
        List<SCardStatVo> result = exportVo.getResult().stream().filter(i -> i instanceof SCardStatVo)
                .map(i -> (SCardStatVo) i)
                .collect(Collectors.toList());
        List<String> conditionList = exportVo.getConditionList();
        ExcelUtil<SCardStatVo> util = new ExcelUtil<SCardStatVo>(SCardStatVo.class);
        return util.exportDynamic(result, "S2收费站通行卡发放日统计表", conditionList, 31);
    }


    /**
     * 导出C2收费站通行卡发放日统计表
     */
    @PostMapping(value = "/export/c2station")
    public AjaxResult exportC2CardStation(@RequestBody @Valid CardStatisticsDto dto) throws IOException{
        ExportVo exportVo = cardService.getCCardStationDay(dto);
        List<CCardStatVo> result = exportVo.getResult().stream().filter(i -> i instanceof CCardStatVo)
                .map(i -> (CCardStatVo) i)
                .collect(Collectors.toList());
        List<String> conditionList = exportVo.getConditionList();
        ExcelUtil<CCardStatVo> util = new ExcelUtil<CCardStatVo>(CCardStatVo.class);
        return util.exportDynamic(result, "C2收费站通行卡回收日统计表", conditionList, 32);
    }
    /**
     * SDT通行卡发放统计表
     */
    @PostMapping(value = "/sdtstation")
    public TableDataVo sdtStationShift(@RequestBody @Valid CardStatisticsDtoV2 dto) {
        List<CardStatisticsVo> result = cardService.sdtStationShift(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(result);
        tableDataVo.setConditionList(conditionList);
        return tableDataVo;
    }

    /**
     * 导出SDT通行卡发放统计表
     */
    @PostMapping(value = "/export/sdtstation")
    public AjaxResult exportSdtStationShift(@RequestBody @Valid CardStatisticsDtoV2 dto) throws IOException{
        ExportVo exportVo = cardService.getSdtStationShift(dto);
        List<SdtCardStatVo> result = exportVo.getResult().stream().filter(i -> i instanceof SdtCardStatVo)
                .map(i -> (SdtCardStatVo) i)
                .collect(Collectors.toList());
        ExcelUtil<SdtCardStatVo> util = new ExcelUtil<SdtCardStatVo>(SdtCardStatVo.class);
        return util.exportDynamic(result, "SDT通行卡发放统计表", exportVo.getConditionList(), 31);
    }

    /**
     * CDT通行卡回收统计表
     */
    @PostMapping(value = "/cdtstation")
    public TableDataVo cdtstation(@RequestBody @Valid CardStatisticsDtoV2 dto) {
        List<CdtStatisticsVo> result = cardService.cdtCardRecycle(dto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(result);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return tableDataVo;
    }

    /**
     * CDT通行卡回收统计表导出
     */
    @PostMapping(value = "/export/cdtstation")
    public AjaxResult exportCdtStation(@RequestBody @Valid CardStatisticsDtoV2 dto) throws IOException {
        ExportVo exportVo = cardService.getCdtStationShift(dto);
        List<CdtCardStatVo> result = exportVo.getResult().stream().filter(i -> i instanceof CdtCardStatVo)
                .map(i -> (CdtCardStatVo) i)
                .collect(Collectors.toList());
        ExcelUtil<CdtCardStatVo> util = new ExcelUtil<CdtCardStatVo>(CdtCardStatVo.class);
        return util.exportDynamic(result, "CDT通行卡回收统计表", exportVo.getConditionList(), 32);
    }
}
