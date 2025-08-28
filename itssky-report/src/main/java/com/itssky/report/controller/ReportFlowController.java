package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.page.TableDataInfo;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.ReportChargeInfo;
import com.itssky.system.domain.ReportFlowInfo;
import com.itssky.system.domain.TbUserInfo;
import com.itssky.system.domain.dto.FlowStatisticsDto;
import com.itssky.system.domain.vo.*;
import com.itssky.system.service.CardService;
import com.itssky.system.service.ITollService;
import com.itssky.system.service.TbUserInfoService;
import com.itssky.system.service.impl.ReportFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author ITSSKY
 */
@RestController
@RequestMapping("/report")
public class ReportFlowController extends BaseController {

    @Value(("${reportTitleName}"))
    private String reportTitleName;

    @Autowired
    private ReportFlowService reportFlowService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TbUserInfoService userInfoService;

    /**
     * CSJ获取高速出口流量报表
     */
    @PostMapping("/exit/flow")
    public TableDataVo getExitFlow(@RequestBody @Valid FlowStatisticsDto dto) {
        List<ReportFlowInfo> exitFlow = reportFlowService.getExitFlow(dto, 2);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(exitFlow);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        tableDataVo.setOperatorName(loginUserInfo.getUsername());
        return tableDataVo;
    }

    /**
     * RSJ获得高速入口流量报表
     */
    @PostMapping(value = "/entry/flow")
    public TableDataVo getEntryFlow(@RequestBody @Valid FlowStatisticsDto dto) {
        List<ReportFlowInfo> exitFlow = reportFlowService.getExitFlow(dto, 1);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(exitFlow);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        tableDataVo.setOperatorName(loginUserInfo.getUsername());
        return tableDataVo;
    }

    /**
     * RSJ获得高速入口机器人流量报表
     */
    @PostMapping(value = "/rsj/robot")
    public TableDataVo getRsjRobotFlow(@RequestBody @Valid FlowStatisticsDto dto) {
        List<ReportFlowInfo> exitFlow = reportFlowService.getExitFlow(dto, 3);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(exitFlow);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        tableDataVo.setOperatorName(loginUserInfo.getUsername());
        return tableDataVo;
    }

    /**
     * CSJ获得高速入口机器人流量报表
     */
    @PostMapping(value = "/csj/robot")
    public TableDataVo getCsjRobotFlow(@RequestBody @Valid FlowStatisticsDto dto) {
        List<ReportFlowInfo> exitFlow = reportFlowService.getExitFlow(dto, 4);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(exitFlow);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        tableDataVo.setOperatorName(loginUserInfo.getUsername());
        return tableDataVo;
    }

    /**
     * 导出CSJ
     */
    @PostMapping(value = "/export/exit/flow")
    public AjaxResult exportExitFlow(@RequestBody @Valid FlowStatisticsDto dto) throws IOException {
        ExportVo exportVo = reportFlowService.getFlowExportVo(dto, 2);
        List<ReportFlowInfo> result = exportVo.getResult().stream().filter(i -> i instanceof ReportFlowInfo)
                .map(i -> (ReportFlowInfo) i)
                .collect(Collectors.toList());
        List<String> conditionList = exportVo.getConditionList();
        ExcelUtil<ReportFlowInfo> util = new ExcelUtil<ReportFlowInfo>(ReportFlowInfo.class);
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(result, "CSJ出口MTC、ETC交通流量统计表", conditionList, 26, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * 导出CSJ
     */
    @PostMapping(value = "/export/entry/flow")
    public AjaxResult exportEntryFlow(@RequestBody @Valid FlowStatisticsDto dto) throws IOException {
        ExportVo exportVo = reportFlowService.getFlowExportVo(dto, 1);
        List<ReportFlowInfo> result = exportVo.getResult().stream().filter(i -> i instanceof ReportFlowInfo)
                .map(i -> (ReportFlowInfo) i)
                .collect(Collectors.toList());
        List<String> conditionList = exportVo.getConditionList();
        ExcelUtil<ReportFlowInfo> util = new ExcelUtil<ReportFlowInfo>(ReportFlowInfo.class);
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(result, "RSJ入口MTC、ETC交通流量统计表", conditionList, 26, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * 导出RSJ机器人
     */
    @PostMapping(value = "/export/rsj/robot")
    public AjaxResult exportRsjRobot(@RequestBody @Valid FlowStatisticsDto dto) throws IOException {
        ExportVo exportVo = reportFlowService.getFlowExportVo(dto, 3);
        List<ReportFlowInfo> result = exportVo.getResult().stream().filter(i -> i instanceof ReportFlowInfo)
                .map(i -> (ReportFlowInfo) i)
                .collect(Collectors.toList());
        List<String> conditionList = exportVo.getConditionList();
        ExcelUtil<ReportFlowInfo> util = new ExcelUtil<ReportFlowInfo>(ReportFlowInfo.class);
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(result, "RSJ入口机器人流量统计表", conditionList, 26, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * 导出CSJ机器人
     */
    @PostMapping(value = "/export/csj/robot")
    public AjaxResult exportCsjRobot(@RequestBody @Valid FlowStatisticsDto dto) throws IOException {
        ExportVo exportVo = reportFlowService.getFlowExportVo(dto, 4);
        List<ReportFlowInfo> result = exportVo.getResult().stream().filter(i -> i instanceof ReportFlowInfo)
                .map(i -> (ReportFlowInfo) i)
                .collect(Collectors.toList());
        List<String> conditionList = exportVo.getConditionList();
        ExcelUtil<ReportFlowInfo> util = new ExcelUtil<ReportFlowInfo>(ReportFlowInfo.class);
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(result, "CSJ出口机器人流量统计表", conditionList, 26, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * 获取高速收费报表
     */
    @GetMapping("/charge")
    public TableDataInfo getCharge() {
        List<ReportChargeInfo> charge = reportFlowService.getCharge();
        charge.get(charge.size() - 6).setSubTotalRow(true);
        charge.get(charge.size() - 1).setTotalRow(true);
        return getDataTable(charge);
    }

    /**
     *
     */
    @PostMapping(value = "/mock/tbsh")
    public AjaxResult mockTbSh() {
        reportFlowService.mockTbsh();
        return AjaxResult.success("OK");
    }

    @PostMapping(value = "/yh")
    public TableDataVo yh(@RequestBody @Valid FlowStatisticsDto dto) {
        TableDataVo data = new TableDataVo();
        data.setRows(reportFlowService.getFlowYh(dto));
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        data.setOperatorName(loginUserInfo.getUsername());
        return data;
    }

    @PostMapping(value = "/export/yh")
    public AjaxResult exportYh(@RequestBody @Valid FlowStatisticsDto dto) throws IOException {
        List<FlowYhVo> list = reportFlowService.getFlowYh(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<FlowYhVo> util = new ExcelUtil<FlowYhVo>(FlowYhVo.class);
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "YH优惠流量综合报表", conditionList, 13, reportTitleName, loginUserInfo.getUsername());
    }

    @PostMapping(value = "/crjflow")
    public TableDataVo crjFlow(@RequestBody @Valid FlowStatisticsDto dto) {
        TableDataVo data = new TableDataVo();
        data.setRows(reportFlowService.getCRJFlow(dto));
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        data.setOperatorName(loginUserInfo.getUsername());
        return data;
    }

    @PostMapping(value = "/export/crjflow")
    public AjaxResult exportCrjFlow(@RequestBody @Valid FlowStatisticsDto dto) throws IOException {
        List<CRJFlowVo> list = reportFlowService.getCRJFlow(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<CRJFlowVo> util = new ExcelUtil<CRJFlowVo>(CRJFlowVo.class);
        String sheetName = "";
        if (dto.getFlag() == ReportFlowService.ENTRY) {
            sheetName = "RJ入口(MTC)交通流量统计表";
        } else if (dto.getFlag() == ReportFlowService.EXIT) {
            sheetName = "CJ出口(MTC)交通流量统计表";
        }
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, sheetName, conditionList, 29, reportTitleName, loginUserInfo.getUsername());
    }

    @PostMapping(value = "/tkflow")
    public TableDataVo tkFlow(@RequestBody @Valid FlowStatisticsDto dto) {
        TableDataVo data = new TableDataVo();
        data.setRows(reportFlowService.tkFlow(dto));
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        data.setOperatorName(loginUserInfo.getUsername());
        return data;
    }

    @PostMapping(value = "/export/tkflow")
    public AjaxResult exportTkFlow(@RequestBody @Valid FlowStatisticsDto dto) throws IOException {
        List<TkFlowVo> list = reportFlowService.tkFlow(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<TkFlowVo> util = new ExcelUtil<TkFlowVo>(TkFlowVo.class);
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "TK入出口(MTC)交通流量按车种统计表", conditionList, 22, reportTitleName, loginUserInfo.getUsername());
    }

//    @PostMapping(value = "/od")
//    public TableDataVo od(@RequestBody @Valid FlowStatisticsDto dto) {
//        TableDataVo data = new TableDataVo();
//        data.setRows();
//    }

//    @GetMapping(value = "/export")
//    public AjaxResult exportCharge(ReportChargeInfo param) {
//        try {
//            return exportService.exportCharge(param);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResult.error(e.getMessage());
//        }
//    }

//    @GetMapping(value = "/export/flow")
//    public AjaxResult exportFlow(ReportFlowInfo reportFlowInfo) {
//        try {
//            return exportService.exportFlow(reportFlowInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResult.error(e.getMessage());
//        }
//    }

}
