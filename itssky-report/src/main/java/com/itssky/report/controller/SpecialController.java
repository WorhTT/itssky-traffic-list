package com.itssky.report.controller;

import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.common.utils.poi.DynamicHeaderExcelExporter;
import com.itssky.common.utils.poi.ExcelHeaderNode;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.TbUserInfo;
import com.itssky.system.domain.dto.CxczDto;
import com.itssky.system.domain.dto.GreenDto;
import com.itssky.system.domain.dto.UnUseEtcDto;
import com.itssky.system.domain.vo.CxczVo;
import com.itssky.system.domain.vo.GreenVo;
import com.itssky.system.domain.vo.TableDataVo;
import com.itssky.system.domain.vo.UnUseEtcVo;
import com.itssky.system.service.CardService;
import com.itssky.system.service.ISpecialService;
import com.itssky.system.service.TbUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping(value = "/special")
@RequiredArgsConstructor
public class SpecialController {

    private final ISpecialService specialService;

    private final CardService cardService;

    private final TbUserInfoService userInfoService;

    @Value(("${reportTitleName}"))
    private String reportTitleName;


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
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        tableDataVo.setOperatorName(loginUserInfo.getUsername());
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
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(greenVos, "绿优台账", conditionList, 7, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * 入口超限操作明细表
     */
    @PostMapping(value = "/cxcz")
    public TableDataVo cxczTable(@RequestBody CxczDto dto) {
        List<CxczVo> cxczVos = specialService.cxczTable(dto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(cxczVos);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        tableDataVo.setOperatorName(loginUserInfo.getUsername());
        return tableDataVo;
    }

    /**
     * 导出入口超限操作明细表
     */
    @PostMapping(value = "/export/cxcz")
    public AjaxResult exportCxczTable(@RequestBody CxczDto dto) throws IOException {
        List<CxczVo> cxczVos = specialService.cxczTable(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(),
                dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<CxczVo> util = new ExcelUtil<CxczVo>(CxczVo.class);
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(cxczVos, "入口超限操作明细表", conditionList, 14, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * 非ETC车辆开票数统计表
     * @param dto
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/unuse/etc")
    public TableDataVo unuseEtcTable(@RequestBody UnUseEtcDto dto) throws IOException {
        List<UnUseEtcVo> result = new ArrayList<>();
        specialService.unuseEtcTable(dto);
        return new TableDataVo();
    }

    /**
     * 天扬高速新增报表需求：
     * 用于每月跟行云科技核对车辆开票数(统计未使用ETC，金额非0，非军车公务应急等的车辆次，时间按自然日期统计)
     */
    @PostMapping(value = "/export/unuse/etc")
    public AjaxResult exportUnuseEtcTable(@RequestBody UnUseEtcDto dto) throws IOException {
        List<UnUseEtcVo> result = specialService.unuseEtcTable(dto);

        if (CollectionUtils.isEmpty(result)) {
            return AjaxResult.warn("无数据");
        }
        // 收费站名称列表
        List<String> stationNames = result.get(0).getStations();

        // 构建表头结构
        List<List<DynamicHeaderExcelExporter.HeaderCell>> headerData = new ArrayList<>();

        // 第一行表头
        List<DynamicHeaderExcelExporter.HeaderCell> firstRow = Arrays.asList(
                new DynamicHeaderExcelExporter.HeaderCell("日期", "staDate", 2, 1, 1), // startColumn=0
                new DynamicHeaderExcelExporter.HeaderCell("收费站", "", 1, stationNames.size(), 2),    // startColumn=1, 注意这里字段名为空，因为它不是叶子节点
                new DynamicHeaderExcelExporter.HeaderCell("小计", "dailyTotal", 2, 1, 2 + stationNames.size()) // startColumn=4
        );

        // 第二行表头（收费站明细）
        List<DynamicHeaderExcelExporter.HeaderCell> secondRow = new ArrayList<>();
        int startCol = 2; // 从第1列开始
        for (String stationName : stationNames) {
            DynamicHeaderExcelExporter.HeaderCell headerCell = new DynamicHeaderExcelExporter.HeaderCell(
                    stationName, stationName, 1, 1, startCol);
            secondRow.add(headerCell);
            startCol++;
        }

        headerData.add(firstRow);
        headerData.add(secondRow);

        // 总列数 = 日期(1) + 收费站(3) + 小计(1) = 5
        int totalColumns = 1 + stationNames.size() + 1;

        // 自定义数据提取器
        Function<Object, Map<String, Object>> dataExtractor = data -> {
            UnUseEtcVo dailyData = (UnUseEtcVo) data;
            Map<String, Object> values = new HashMap<>();
            values.put("staDate", dailyData.getStaDate());
            values.put("dailyTotal", dailyData.getDailyTotal());
            if (dailyData.getStationData() != null) {
                for (String stationName : stationNames) {
                    values.put(stationName, dailyData.getStationData().get(stationName));
                }
            }
            return values;
        };

        // 调用导出方法
        return DynamicHeaderExcelExporter.exportToExcel(
                result,
                "收费站报表",
                null,
                totalColumns,
                "南通绕城",
                "张三",
                headerData,
                dataExtractor
        );
    }
}
