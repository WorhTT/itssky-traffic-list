package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.page.TableDataInfo;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.dto.CardStatisticsDto;
import com.itssky.system.domain.dto.CardStatisticsDtoV2;
import com.itssky.system.domain.vo.CdtStatisticsVo;
import com.itssky.system.domain.vo.F1StationShiftTollVo;
import com.itssky.system.mapper.CardMapper;
import com.itssky.system.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public TableDataInfo s1StationShift(@RequestBody @Valid CardStatisticsDto dto) {
        return getDataTable(cardService.s1StationShift(dto));
    }

    /**
     * S2收费站通行卡发放日统计表
     */
    @PostMapping(value = "/s2station")
    public TableDataInfo s2StationShift(@RequestBody @Valid CardStatisticsDto dto) {
        return getDataTable(cardService.s2StationShift(dto));
    }

    /**
     * SDT通行卡发放统计表
     */
    @PostMapping(value = "/sdtstation")
    public TableDataInfo sdtStationShift(@RequestBody @Valid CardStatisticsDtoV2 dto) {
        return getDataTable(cardService.sdtStationShift(dto));
    }

    /**
     * CDT通行卡回收统计表
     */
    @PostMapping(value = "/cdtstation")
    public TableDataInfo cdtstation(@RequestBody @Valid CardStatisticsDtoV2 dto) {
        return getDataTable(cardService.cdtCardRecycle(dto));
    }

    /**
     * CDT通行卡回收统计表导出
     */
    @PostMapping(value = "/export/cdtstation")
    public AjaxResult exportCdtStation(@RequestBody @Valid CardStatisticsDtoV2 dto) throws IOException {
        List<CdtStatisticsVo> list = cardService.cdtCardRecycle(dto);
        ExcelUtil<CdtStatisticsVo> util = new ExcelUtil<CdtStatisticsVo>(CdtStatisticsVo.class);
        List<String> conditionList = new ArrayList<>();
        conditionList.add("收费站：中心");
        conditionList.add("统计日期：2024-12-04");
        return util.exportDynamic(list, "CDT通行卡回收统计表", conditionList, 29);
    }
}
