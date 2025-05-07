package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.page.TableDataInfo;
import com.itssky.common.exception.biz.BizException;
import com.itssky.common.utils.DateUtils;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.dto.FD06Dto;
import com.itssky.system.domain.vo.FD06Vo;
import com.itssky.system.domain.vo.GreenVo;
import com.itssky.system.domain.vo.TableDataVo;
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
        return util.exportDynamic(list, "FD06收费员发卡统计表", conditionList, 3, reportTitleName);
    }
}
