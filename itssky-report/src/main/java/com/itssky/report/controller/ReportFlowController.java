package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.page.TableDataInfo;
import com.itssky.report.domain.ReportFlowInfo;
import com.itssky.report.service.ReportFlowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author ITSSKY
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReportFlowController extends BaseController {

    private final ReportFlowService reportFlowService;


    /**
     * 获取高速入口流量报表
     */
    @GetMapping("/entry/flow")
    public TableDataInfo getEntryFlow() {
        List<ReportFlowInfo> entryFlow = reportFlowService.getEntryFlow();
        return getDataTable(entryFlow);
    }
}
