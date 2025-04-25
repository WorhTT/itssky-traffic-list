package com.itssky.report.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.page.TableDataInfo;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.SysConfig;
import com.itssky.system.domain.TbStationInfo;
import com.itssky.system.domain.dto.VehicleClassStatDto;
import com.itssky.system.domain.dto.FtStationDto;
import com.itssky.system.domain.dto.StationShiftDto;
import com.itssky.system.domain.vo.*;
import com.itssky.system.mapper.TbStationInfoMapper;
import com.itssky.system.service.CardService;
import com.itssky.system.service.ITollService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 通行类报表
 * @author ITSSKY
 */
@Slf4j
@RestController
@RequestMapping("/toll")
public class TollController extends BaseController {

    @Value(("${reportTitleName}"))
    private String reportTitleName;

    @Autowired
    private ITollService tollService;

    @Autowired
    private TbStationInfoMapper tbStationInfoMapper;

    @Autowired
    private CardService cardService;


    /**
     * F1收费站通行费收入班统计表
     */
    @PostMapping(value = "/f1station")
    public TableDataVo f1StationShift(@RequestBody @Valid StationShiftDto dto) {
        List<StationShiftVo> result = tollService.f1StationShift(dto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(result);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getTime(), dto.getShiftId()));
        return tableDataVo;
    }

    /**
     * 导出F1收费站通行费收入班统计表
     */
    @PostMapping(value = "/export/f1station")
    public AjaxResult exportF1Station(@RequestBody @Valid StationShiftDto dto) throws IOException {
        List<F1StationShiftTollVo> f1StationShiftToll = tollService.getF1StationShiftToll(dto);
        ExcelUtil<F1StationShiftTollVo> util = new ExcelUtil<F1StationShiftTollVo>(F1StationShiftTollVo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getTime(), dto.getShiftId());
        return util.exportDynamic(f1StationShiftToll, "F1收费站通行费收入班统计表", conditionList, 15, reportTitleName);
    }

    /**
     * F2收费站通行费收入日统计表
     */
    @PostMapping(value = "/f2station")
    public TableDataVo f2StationShift(@RequestBody @Valid StationShiftDto dto) {
        List<StationShiftVo> result = tollService.f2StationShift(dto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(result);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getTime()));
        return tableDataVo;
    }

    /**
     * 导出F2收费站通行费收入日统计表
     */
    @PostMapping(value = "/export/f2station")
    public AjaxResult exportF2Station(@RequestBody @Valid StationShiftDto dto) throws IOException {
        List<F2StationShiftTollVo> f2StationShiftToll = tollService.getF2StationShiftToll(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getTime());
        ExcelUtil<F2StationShiftTollVo> util = new ExcelUtil<F2StationShiftTollVo>(F2StationShiftTollVo.class);
        return util.exportDynamic(f2StationShiftToll, "F2收费站通行费收入日统计表", conditionList, 14, reportTitleName);
    }

    /**
     * FT通行费收入统计表
     */
    @PostMapping(value = "/fttoll")
    public TableDataVo ftToll(@RequestBody @Valid FtStationDto dto) {
        List<StationShiftVo> result = tollService.ftToll(dto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(result);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return tableDataVo;
    }

    /**
     * 导出FT通行费收入统计表
     */
    @PostMapping(value = "/export/fttoll")
    public AjaxResult exportFtToll(@RequestBody @Valid FtStationDto dto) throws IOException{
        List<FtTollVo> ftToll = tollService.getFtToll(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<FtTollVo> util = new ExcelUtil<FtTollVo>(FtTollVo.class);
        return util.exportDynamic(ftToll, "FT通行费收入统计表", conditionList, 13, reportTitleName);
    }

    /**
     * AFV综合(MTC+ETC)按车型统计表
     */
    @PostMapping(value = "/afvgeneral")
    public TableDataVo afvGeneral(@RequestBody @Valid VehicleClassStatDto dto) {
        List<VehicleClassStatVo> result = tollService.afvGeneral(dto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(result);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return tableDataVo;
    }

    /**
     * AFV综合(MTC+ETC)按车型统计表
     */
    @PostMapping(value = "/export/afvgeneral")
    public AjaxResult exportAfvGeneral(@RequestBody @Valid VehicleClassStatDto dto) throws IOException{
        List<AfvVehicleVo> afvGeneral = tollService.getAfvGeneral(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        ExcelUtil<AfvVehicleVo> util = new ExcelUtil<AfvVehicleVo>(AfvVehicleVo.class);
        return util.exportDynamic(afvGeneral, "AFV综合MTC、ETC按车型统计表", conditionList, 22, reportTitleName);
    }

    /**
     * EEF电子支付通行费(MTC+ETC)统计表
     */
    @PostMapping(value = "/eefepay")
    public TableDataVo eefEPay(@RequestBody @Valid VehicleClassStatDto dto) {
        TableDataVo tableDataVo = new TableDataVo();
        List<EPayTollStatVo> ePayTollStatVos = tollService.eefEPay(dto);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        tableDataVo.setRows(ePayTollStatVos);
        return tableDataVo;
    }

    /**
     * EEF电子支付通行费(MTC+ETC)统计表导出
     * @param dto
     * @return
     */
    @PostMapping(value = "/export/eefepay")
    public AjaxResult exportEefEPay(@RequestBody @Valid VehicleClassStatDto dto) throws IOException {
        List<EPayTollStatVo> list = tollService.eefEPay(dto);
        ExcelUtil<EPayTollStatVo> util = new ExcelUtil<EPayTollStatVo>(EPayTollStatVo.class);
        List<String> conditionList = new ArrayList<>();
        conditionList.add("收费站：中心");
        conditionList.add("统计日期：2024-11-30至2024-12-04");
        AjaxResult ajaxResult = util.exportDynamic(list, "EEF电子支付通行费MTC、ETC统计表", conditionList, 61, reportTitleName);
        return ajaxResult;
    }
}
