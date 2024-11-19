package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.page.TableDataInfo;
import com.itssky.report.domain.ReportChargeInfo;
import com.itssky.report.domain.ReportFlowInfo;
import com.itssky.report.service.ExportService;
import com.itssky.report.service.ReportFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
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
    private ExportService exportService;


    /**
     * 获取高速入口流量报表
     */
    @GetMapping("/entry/flow")
    public TableDataInfo getEntryFlow() {
        List<ReportFlowInfo> entryFlow = reportFlowService.getEntryFlow();
        return getDataTable(entryFlow);
    }

    /**
     * 获取高速出口流量报表
     */
    @GetMapping("/exit/flow")
    public TableDataInfo getExitFlow() {
        List<ReportFlowInfo> exitFlow = reportFlowService.getExitFlow();
        return getDataTable(exitFlow);
    }

    /**
     * 获取高速收费报表
     */
    @GetMapping("/charge")
    public TableDataInfo getCharge() {
        List<ReportChargeInfo> charge = reportFlowService.getCharge();
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

    @PostMapping(value = "/export")
    public AjaxResult exportCharge() {
        try {
            exportService.exportCharge();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return AjaxResult.success("export success");
    }
}
