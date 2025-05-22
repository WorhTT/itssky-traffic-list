package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.exception.biz.BizException;
import com.itssky.common.utils.DateUtils;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.dto.FD06Dto;
import com.itssky.system.domain.dto.FD26Dto;
import com.itssky.system.domain.dto.FD27Dto;
import com.itssky.system.domain.vo.*;
import com.itssky.system.service.CardService;
import com.itssky.system.service.impl.ExamineServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/examine")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ExamineController extends BaseController {

    private final ExamineServiceImpl examineService;

    private final CardService cardService;

    @Value(("${reportTitleName}"))
    private String reportTitleName;

    @PostMapping(value = "/fd06")
    public TableDataVo fd06(@RequestBody @Valid FD06Dto dto) {
        if (!DateUtils.isSameYearAndMonth(dto.getBeginTime(), dto.getEndTime())) {
            throw new BizException("时间选择范围不可跨年跨月！");
        }
        TableDataVo data = new TableDataVo();
        List<FD06Vo> list = examineService.getFd06(dto);
        data.setRows(list);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        data.setConditionList(conditionList);
        return data;
    }

    @PostMapping(value = "/export/fd06")
    public AjaxResult exportFd06(@RequestBody @Valid FD06Dto dto) throws IOException {
        if (!DateUtils.isSameYearAndMonth(dto.getBeginTime(), dto.getEndTime())) {
            throw new BizException("时间选择范围不可跨年跨月！");
        }
        List<FD06Vo> list = examineService.getFd06(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<FD06Vo> util = new ExcelUtil<FD06Vo>(FD06Vo.class);
        return util.exportDynamic(list, "FD06收费员发卡统计表", conditionList, 4, reportTitleName);
    }

    @PostMapping(value = "/fd07")
    public TableDataVo fd07(@RequestBody @Valid FD06Dto dto) {
        if (!DateUtils.isSameYearAndMonth(dto.getBeginTime(), dto.getEndTime())) {
            throw new BizException("时间选择范围不可跨年跨月！");
        }
        TableDataVo data = new TableDataVo();
        List<FD07Vo> list = examineService.getFd07(dto);
        data.setRows(list);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        data.setConditionList(conditionList);
        return data;
    }

    @PostMapping(value = "/export/fd07")
    public AjaxResult exportFd07(@RequestBody @Valid FD06Dto dto) throws IOException {
        if (!DateUtils.isSameYearAndMonth(dto.getBeginTime(), dto.getEndTime())) {
            throw new BizException("时间选择范围不可跨年跨月！");
        }
        List<FD07Vo> list = examineService.getFd07(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<FD07Vo> util = new ExcelUtil<FD07Vo>(FD07Vo.class);
        return util.exportDynamic(list, "FD07收费员收费统计表", conditionList, 10, reportTitleName);
    }

    @PostMapping(value = "/fd27")
    public TableDataVo fd27(@RequestBody @Valid FD27Dto dto) {
        if (!DateUtils.isSameYearAndMonth(dto.getBeginTime(), dto.getEndTime())) {
            throw new BizException("时间选择范围不可跨年跨月！");
        }
        TableDataVo data = new TableDataVo();
        data.setRows(examineService.getFd27(dto));
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return data;
    }

    @PostMapping(value = "/export/fd27")
    public AjaxResult exportFd27(@RequestBody @Valid FD27Dto dto) throws IOException {
        if (!DateUtils.isSameYearAndMonth(dto.getBeginTime(), dto.getEndTime())) {
            throw new BizException("时间选择范围不可跨年跨月");
        }
        List<FD27Vo> list = examineService.getFd27(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<FD27Vo> util = new ExcelUtil<FD27Vo>(FD27Vo.class);
        return util.exportDynamic(list, "FD27变档明细统计表", conditionList, 12, reportTitleName);
    }

    @PostMapping(value = "/fd26")
    public TableDataVo fd26(@RequestBody @Valid FD26Dto dto) {
        TableDataVo data = new TableDataVo();
        data.setRows(examineService.getFd26(dto));
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getTime()));
        return data;
    }

    @PostMapping(value = "/export/fd26")
    public AjaxResult exportFd26(@RequestBody @Valid FD26Dto dto) throws IOException {
        List<FD26Vo> list = examineService.getFd26(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getTime());
        ExcelUtil<FD26Vo> util = new ExcelUtil<FD26Vo>(FD26Vo.class);
        return util.exportDynamic(list, "FD26误判率明细统计", conditionList, 13, reportTitleName);
    }

    @PostMapping(value = "/fd29")
    public TableDataVo fd29(@RequestBody @Valid FD06Dto dto) {
        if (!DateUtils.isSameYearAndMonth(dto.getBeginTime(), dto.getEndTime())) {
            throw new BizException("时间选择范围不可跨年跨月！");
        }
        TableDataVo data = new TableDataVo();
        data.setRows(examineService.getFd29(dto));
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return data;
    }

    @PostMapping(value = "/export/fd29")
    public AjaxResult exportFd29(@RequestBody @Valid FD06Dto dto) throws IOException {
        if (!DateUtils.isSameYearAndMonth(dto.getBeginTime(), dto.getEndTime())) {
            throw new BizException("时间选择范围不可跨年跨月");
        }
        List<FD29Vo> list = examineService.getFd29(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<FD29Vo> util = new ExcelUtil<FD29Vo>(FD29Vo.class);
        return util.exportDynamic(list, "FD29升档排名汇总表", conditionList, 5, reportTitleName);
    }
}
