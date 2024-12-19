package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.page.TableDataInfo;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.ReportChargeInfo;
import com.itssky.system.domain.ReportFlowInfo;
import com.itssky.system.domain.dto.FlowStatisticsDto;
import com.itssky.system.service.ITollService;
import com.itssky.system.service.impl.ReportFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author ITSSKY
 */
@RestController
@RequestMapping("/report")
public class ReportFlowController extends BaseController {

    @Autowired
    private ReportFlowService reportFlowService;

    @Autowired
    private ITollService tollService;


//    /**
//     * 获取高速入口流量报表
//     */
//    @GetMapping("/entry/flow")
//    public TableDataInfo getEntryFlow(ReportFlowInfo reportFlowInfo) {
//        List<ReportFlowInfo> entryFlow = reportFlowService.getEntryFlow(reportFlowInfo);
//        return getDataTable(entryFlow);
//    }

    /**
     * CSJ获取高速出口流量报表
     */
    @PostMapping("/exit/flow")
    public TableDataInfo getExitFlow(@RequestBody @Valid FlowStatisticsDto dto) {
        List<ReportFlowInfo> exitFlow = reportFlowService.getExitFlow(dto, 2);
        return getDataTable(exitFlow);
    }

    /**
     * RSJ获得高速入口流量报表
     */
    @PostMapping(value = "/entry/flow")
    public TableDataInfo getEntryFlow(@RequestBody @Valid FlowStatisticsDto dto) {
        List<ReportFlowInfo> exitFlow = reportFlowService.getExitFlow(dto, 1);
        return getDataTable(exitFlow);
    }

    /**
     * 导出CSJ
     */
    @PostMapping(value = "/export/exit/flow")
    public AjaxResult exportExitFlow(@RequestBody @Valid FlowStatisticsDto dto) throws IOException {
        List<ReportFlowInfo> list = reportFlowService.getExitFlow(dto, 2);
        ExcelUtil<ReportFlowInfo> util = new ExcelUtil<ReportFlowInfo>(ReportFlowInfo.class);
        List<String> conditionList = new ArrayList<>();
        conditionList.add("收费站：中心");
        conditionList.add("统计日期：2024-12-04");
        return util.exportDynamic(list, "CSJ出口MTC、ETC交通流量统计表", conditionList, 26);
    }

    /**
     * 导出CSJ
     */
    @PostMapping(value = "/export/entry/flow")
    public AjaxResult exportEntryFlow(@RequestBody @Valid FlowStatisticsDto dto) throws IOException {
        List<ReportFlowInfo> list = reportFlowService.getExitFlow(dto, 1);
        ExcelUtil<ReportFlowInfo> util = new ExcelUtil<ReportFlowInfo>(ReportFlowInfo.class);
        List<String> conditionList = new ArrayList<>();
        conditionList.add("收费站：中心");
        conditionList.add("统计日期：2024-12-04");
        return util.exportDynamic(list, "RSJ入口MTC、ETC交通流量统计表", conditionList, 26);
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
     * 随机生成测试数据
     */
    @PostMapping(value = "/mock/data")
    public AjaxResult mockData() {
        reportFlowService.mockData();
        return AjaxResult.success("OK");
    }

    @PostMapping(value = "/mock/entry/data")
    public AjaxResult mockEntryData() {
        reportFlowService.mockTbEntryData();
        return AjaxResult.success("OK");
    }

    /**
     *
     */
    @PostMapping(value = "/mock/tbsh")
    public AjaxResult mockTbSh() {
        reportFlowService.mockTbsh();
        return AjaxResult.success("OK");
    }

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
