package com.itssky.report.controller;

import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.dto.GreenDto;
import com.itssky.system.domain.vo.GreenVo;
import com.itssky.system.domain.vo.TableDataVo;
import com.itssky.system.service.CardService;
import com.itssky.system.service.ISpecialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/special")
@RequiredArgsConstructor
public class SpecialController {

    private final ISpecialService specialService;

    private final CardService cardService;


    /**
     * 绿优台账
     */
    @PostMapping(value = "/green")
    public TableDataVo greenTable(@RequestBody GreenDto greenDto) {
        List<GreenVo> greenVos = specialService.greenTable(greenDto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(greenVos);
        tableDataVo.setConditionList(cardService.buildConditionList(greenDto.getStationId(),
                greenDto.getBeginTime(), greenDto.getEndTime()));
        return tableDataVo;
    }

    /**
     * 导出绿优台账
     * @param greenDto
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/export/green")
    public AjaxResult exportGreenTable(@RequestBody GreenDto greenDto) throws IOException {
        List<GreenVo> greenVos = specialService.greenTable(greenDto);
        List<String> conditionList = cardService.buildConditionList(greenDto.getStationId(),
                greenDto.getBeginTime(), greenDto.getEndTime());
        ExcelUtil<GreenVo> util = new ExcelUtil<GreenVo>(GreenVo.class);
        return util.exportDynamic(greenVos, "绿优台账", conditionList, 7);
    }
}
