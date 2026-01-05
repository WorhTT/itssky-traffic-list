package com.itssky.report.controller;

import com.itssky.common.core.controller.BaseController;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.system.domain.TbUserInfo;
import com.itssky.system.domain.dto.*;
import com.itssky.system.domain.vo.*;
import com.itssky.system.service.CardService;
import com.itssky.system.service.ITollService;
import com.itssky.system.service.TbUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
    private CardService cardService;

    @Autowired
    private TbUserInfoService userInfoService;


    /**
     * F1收费站通行费收入班统计表
     */
    @PostMapping(value = "/f1station")
    public TableDataVo f1StationShift(@RequestBody @Valid StationTimeDto dto) {
        List<StationShiftVo> result = tollService.f1StationShift(dto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(result);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        tableDataVo.setOperatorName(loginUser.getUsername());
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getTime(), dto.getShiftId()));
        tableDataVo.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        tableDataVo.setTitle(reportTitleName);
        return tableDataVo;
    }

    /**
     * 导出F1收费站通行费收入班统计表
     */
    @PostMapping(value = "/export/f1station")
    public AjaxResult exportF1Station(@RequestBody @Valid StationTimeDto dto) throws IOException {
        List<F1StationShiftTollVo> f1StationShiftToll = tollService.getF1StationShiftToll(dto);
        ExcelUtil<F1StationShiftTollVo> util = new ExcelUtil<F1StationShiftTollVo>(F1StationShiftTollVo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getTime(), dto.getShiftId());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(f1StationShiftToll, "F1收费站通行费收入班统计表", conditionList, 15, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * F2收费站通行费收入日统计表
     */
    @PostMapping(value = "/f2station")
    public TableDataVo f2StationShift(@RequestBody @Valid StationTimeDto dto) {
        List<StationShiftVo> result = tollService.f2StationShift(dto);
        TableDataVo tableDataVo = new TableDataVo();
        tableDataVo.setRows(result);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getTime()));
        tableDataVo.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        tableDataVo.setTitle(reportTitleName);
        return tableDataVo;
    }

    /**
     * 导出F2收费站通行费收入日统计表
     */
    @PostMapping(value = "/export/f2station")
    public AjaxResult exportF2Station(@RequestBody @Valid StationTimeDto dto) throws IOException {
        List<F2StationShiftTollVo> f2StationShiftToll = tollService.getF2StationShiftToll(dto);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getTime());
        ExcelUtil<F2StationShiftTollVo> util = new ExcelUtil<F2StationShiftTollVo>(F2StationShiftTollVo.class);
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(f2StationShiftToll, "F2收费站通行费收入日统计表", conditionList, 14, reportTitleName, loginUserInfo.getUsername());
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
        tableDataVo.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        tableDataVo.setTitle(reportTitleName);
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
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(ftToll, "FT通行费收入统计表", conditionList, 13, reportTitleName, loginUserInfo.getUsername());
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
        tableDataVo.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        tableDataVo.setTitle(reportTitleName);
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
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(afvGeneral, "AFV综合MTC、ETC按车型统计表", conditionList, 22, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * EEF电子支付通行费(MTC+ETC)统计表
     * Flag 1:MTC 2:ETC 前端不传就是MTC+ETC
     */
    @PostMapping(value = "/eefepay")
    public TableDataVo eefEPay(@RequestBody @Valid VehicleClassStatDto dto) {
        TableDataVo tableDataVo = new TableDataVo();
        List<EPayTollStatVo> ePayTollStatVos = tollService.eefEPay(dto);
        tableDataVo.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        tableDataVo.setRows(ePayTollStatVos);
        tableDataVo.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        tableDataVo.setTitle(reportTitleName);
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
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "EEF电子支付通行费MTC、ETC统计表", conditionList, 61, reportTitleName, loginUserInfo.getUsername());
    }

    @PostMapping(value = "/export/eefepay/mtc")
    public AjaxResult exportEefEPayMtc(@RequestBody @Valid VehicleClassStatDto dto) throws IOException {
        List<EPayTollStatVo> list = tollService.eefEPay(dto);
        ExcelUtil<EPayTollStatVo> util = new ExcelUtil<EPayTollStatVo>(EPayTollStatVo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "EEF_MTC电子支付通行费统计表", conditionList, 61, reportTitleName, loginUserInfo.getUsername());
    }

    @PostMapping(value = "/export/eefepay/etc")
    public AjaxResult exportEefEPayEtc(@RequestBody @Valid VehicleClassStatDto dto) throws IOException {
        List<EPayTollStatVo> list = tollService.eefEPay(dto);
        ExcelUtil<EPayTollStatVo> util = new ExcelUtil<EPayTollStatVo>(EPayTollStatVo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "EEF_ETC电子支付通行费统计表", conditionList, 61, reportTitleName, loginUserInfo.getUsername());
    }

    @PostMapping(value = "/f6toll")
    public TableDataVo getF6Toll(@RequestBody @Valid StationTimeDto dto) {
        TableDataVo data = new TableDataVo();
        data.setRows(tollService.f6Toll(dto));
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getTime(), dto.getShiftId()));
        data.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        data.setTitle(reportTitleName);
        return data;
    }

    @PostMapping(value = "/export/f6toll")
    public AjaxResult exportF6Toll(@RequestBody @Valid StationTimeDto dto) throws IOException {
        List<F6TollVo> list = tollService.f6Toll(dto);
        ExcelUtil<F6TollVo> util = new ExcelUtil<F6TollVo>(F6TollVo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getTime(), dto.getShiftId());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "F6收费站通行费收入班对账表", conditionList, 6, reportTitleName, loginUserInfo.getUsername());
    }

    @PostMapping(value = "/cf1toll")
    public TableDataVo getCf1Toll(@RequestBody @Valid StationTimeDto dto) {
        TableDataVo data = new TableDataVo();
        data.setRows(tollService.cf1Toll(dto));
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getTime(), dto.getShiftId()));
        data.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        data.setTitle(reportTitleName);
        return data;
    }

    @PostMapping(value = "/export/cf1toll")
    public AjaxResult exportCf1Toll(@RequestBody @Valid StationTimeDto dto) throws IOException {
        List<Cf1Vo> list = tollService.cf1Toll(dto);
        ExcelUtil<Cf1Vo> util = new ExcelUtil<Cf1Vo>(Cf1Vo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getTime(), dto.getShiftId());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "CF1收费中心通行费收入班统计表", conditionList, 13, reportTitleName, loginUserInfo.getUsername());
    }

    @PostMapping(value = "/mobtoll")
    public TableDataVo getMobToll(@RequestBody @Valid FtStationDto dto) {
        TableDataVo data = new TableDataVo();
        data.setRows(tollService.mobToll(dto));
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        data.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        data.setTitle(reportTitleName);
        return data;
    }

    @PostMapping(value = "/export/mobtoll")
    public AjaxResult exportMobToll(@RequestBody @Valid FtStationDto dto) throws IOException {
        List<MOBTollVo> list = tollService.mobToll(dto);
        ExcelUtil<MOBTollVo> util = new ExcelUtil<>(MOBTollVo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "MOB移动支付收费统计报表", conditionList, 19, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * 通行费收入综合统计表 沿江总值
     */
    @PostMapping(value = "/yjzz")
    public TableDataVo getYjzz(@RequestBody @Valid FtStationDto dto) {
        TableDataVo data = new TableDataVo();
        List<StationShiftVo> list = tollService.yjzz(dto);
        data.setRows(list);
        data.setTitle(reportTitleName);
        data.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        //构建查询条件
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return data;
    }


    /**
     * YH优惠金额综合报表 2025年12月29日 10:24:54
     * 集装箱、绿色通道、抗震救灾、运管苏通卡货车、军车、专用工作卡、收割机、应急、大件运输、合计
     * 统计方式 人员、日、月、站
     */
    @PostMapping(value = "/yh")
    public TableDataVo yh(@RequestBody @Valid TollYhDto dto) {
        TableDataVo data = new TableDataVo();
        List<TollYhVo> list = tollService.yh(dto);
        data.setRows(list);
        data.setTitle(reportTitleName);
        data.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        //构建查询条件
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return data;
    }

    @PostMapping(value = "/export/yh")
    public AjaxResult exportYh(@RequestBody @Valid TollYhDto dto) throws IOException {
        List<TollYhVo> list = tollService.yh(dto);
        ExcelUtil<TollYhVo> util = new ExcelUtil<>(TollYhVo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "YH优惠金额综合报表", conditionList, 31, reportTitleName, loginUserInfo.getUsername());
    }


    /**
     * EU电子支付通行费(MTC+ETC)按车型统计 2026年1月4日 14:17:49
     * 统计方式 日、月、站
     */
    @PostMapping(value = "/eu")
    public TableDataVo eu(@RequestBody @Valid CommonReportDto dto) {
        TableDataVo data = new TableDataVo();
        List<EuVo> list = tollService.eu(dto);
        data.setRows(list);
        data.setTitle(reportTitleName);
        data.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        //构建查询条件
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return data;
    }

    @PostMapping(value = "/export/eu")
    public AjaxResult exportEu(@RequestBody @Valid CommonReportDto dto) throws IOException {
        List<EuVo> list = tollService.eu(dto);
        ExcelUtil<EuVo> util = new ExcelUtil<>(EuVo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "EU电子支付通行费(MTC+ETC)按车型统计", conditionList, 25, reportTitleName, loginUserInfo.getUsername());
    }

    /**
     * MOB移动支付统计按车型统计 2026年1月4日 16:23:54
     * 统计方式 日、月、站
     */
    @PostMapping(value = "/mobvc")
    public TableDataVo mobVc(@RequestBody @Valid CommonReportDto dto) {
        TableDataVo data = new TableDataVo();
        List<MobVcVo> list = tollService.mobVc(dto);
        data.setRows(list);
        data.setTitle(reportTitleName);
        data.setOperatorName(userInfoService.getLoginUserInfo().getUsername());
        //构建查询条件
        data.setConditionList(cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return data;
    }

    @PostMapping(value = "/export/mobvc")
    public AjaxResult exportMobVc(@RequestBody @Valid CommonReportDto dto) throws IOException {
        List<MobVcVo> list = tollService.mobVc(dto);
        ExcelUtil<MobVcVo> util = new ExcelUtil<>(MobVcVo.class);
        List<String> conditionList = cardService.buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime());
        TbUserInfo loginUserInfo = userInfoService.getLoginUserInfo();
        return util.exportDynamic(list, "MOB移动支付统计按车型统计", conditionList, 41, reportTitleName, loginUserInfo.getUsername());
    }
}
