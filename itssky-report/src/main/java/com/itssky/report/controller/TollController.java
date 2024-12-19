package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.page.TableDataInfo;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.SysConfig;
import com.itssky.system.domain.dto.VehicleClassStatDto;
import com.itssky.system.domain.dto.FtStationDto;
import com.itssky.system.domain.dto.StationShiftDto;
import com.itssky.system.domain.vo.EPayTollStatVo;
import com.itssky.system.domain.vo.F1StationShiftTollVo;
import com.itssky.system.service.ITollService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通行类报表
 * @author ITSSKY
 */
@Slf4j
@RestController
@RequestMapping("/toll")
public class TollController extends BaseController {

    @Autowired
    private ITollService tollService;


    /**
     * F1收费站通行费收入班统计表
     */
    @PostMapping(value = "/f1station")
    public TableDataInfo f1StationShift(@RequestBody @Valid StationShiftDto dto) {
        return getDataTable(tollService.f1StationShift(dto));
    }

    /**
     * 导出F1收费站通行费收入班统计表
     */
    @PostMapping(value = "/export/f1station")
    public AjaxResult exportF1Station(@RequestBody @Valid StationShiftDto dto) throws IOException {
        List<F1StationShiftTollVo> f1StationShiftToll = tollService.getF1StationShiftToll(dto);
        ExcelUtil<F1StationShiftTollVo> util = new ExcelUtil<F1StationShiftTollVo>(F1StationShiftTollVo.class);
        List<String> conditionList = new ArrayList<>();
        conditionList.add("收费站：中心");
        conditionList.add("统计日期：2024-12-04");
        conditionList.add("班次：早班");
        return util.exportDynamic(f1StationShiftToll, "F1收费站通行费收入班统计表", conditionList, 15);
    }

    /**
     * F2收费站通行费收入日统计表
     */
    @PostMapping(value = "/f2station")
    public TableDataInfo f2StationShift(@RequestBody @Valid StationShiftDto dto) {
        return getDataTable(tollService.f2StationShift(dto));
    }

    /**
     * FT通行费收入统计表
     */
    @PostMapping(value = "/fttoll")
    public TableDataInfo ftToll(@RequestBody @Valid FtStationDto dto) {
        return getDataTable(tollService.ftToll(dto));
    }

    /**
     * AFV综合(MTC+ETC)按车型统计表
     */
    @PostMapping(value = "/afvgeneral")
    public TableDataInfo afvGeneral(@RequestBody @Valid VehicleClassStatDto dto) {
        return getDataTable(tollService.afvGeneral(dto));
    }

    /**
     * EEF电子支付通行费(MTC+ETC)统计表
     */
    @PostMapping(value = "/eefepay")
    public TableDataInfo eefEPay(@RequestBody @Valid VehicleClassStatDto dto) {
        return getDataTable(tollService.eefEPay(dto));
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
        AjaxResult ajaxResult = util.exportDynamic(list, "EEF电子支付通行费MTC、ETC统计表", conditionList, 61);
        return ajaxResult;
    }
}
