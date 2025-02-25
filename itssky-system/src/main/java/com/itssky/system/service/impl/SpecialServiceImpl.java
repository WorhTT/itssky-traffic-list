package com.itssky.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.utils.StringUtils;
import com.itssky.system.domain.TbStationInfo;
import com.itssky.system.domain.dto.CxczDto;
import com.itssky.system.domain.dto.GreenDto;
import com.itssky.system.domain.vo.CxczVo;
import com.itssky.system.domain.vo.GreenVo;
import com.itssky.system.mapper.SpecialMapper;
import com.itssky.system.mapper.TbStationInfoMapper;
import com.itssky.system.service.ISpecialService;
import com.itssky.util.TableUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 特情类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialServiceImpl implements ISpecialService {

    private final TbStationInfoMapper tbStationInfoMapper;

    private final SpecialMapper specialMapper;



    @Override
    public List<GreenVo> greenTable(GreenDto dto) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //判断用户的corpno
        if (dto.getStationId() == -1 && loginUser.getCorpNo().length() == 2) {
            LambdaQueryWrapper<TbStationInfo> tbStationInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tbStationInfoLambdaQueryWrapper.select(TbStationInfo::getStationname, TbStationInfo::getStationhex,
                    TbStationInfo::getStationid).likeRight(TbStationInfo::getCorpno, loginUser.getCorpNo());
            List<TbStationInfo> tbStationInfoList = tbStationInfoMapper.selectList(tbStationInfoLambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(tbStationInfoList)) {
                List<Integer> stationIdList = tbStationInfoList.stream().filter(i -> i.getStationid() != null)
                        .map(TbStationInfo::getStationid).collect(Collectors.toList());
                dto.setStationIdList(stationIdList);
            }
        } else {
            dto.setStationIdList(Collections.singletonList(dto.getStationId()));
        }
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "exit",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        if (CollectionUtils.isEmpty(dto.getTableNameList())) {
            return new ArrayList<>();
        }
        //时间传参格式化
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<GreenVo> greenVos = specialMapper.greenTable(dto);
        if (!CollectionUtils.isEmpty(greenVos)) {
            List<Map> operatorNameMap =
                    specialMapper.buildOperatorName(greenVos.stream()
                            .distinct().map(GreenVo::getOperatorId).collect(Collectors.toSet()));
            List<Map> stationNameMap = specialMapper.buildStationName(greenVos.stream()
                    .distinct().map(GreenVo::getStationId).collect(Collectors.toSet()));
            Map<Integer, String> operatorMap = new HashMap<>();
            Map<Integer, String> stationMap = new HashMap<>();
            operatorNameMap.forEach(m -> operatorMap.put(Integer.valueOf(m.get("operatorId").toString()),
                    m.get("operatorName").toString()));
            stationNameMap.forEach(m -> stationMap.put(Integer.valueOf(m.get("stationId").toString()),
                    m.get("stationName").toString()));
            greenVos.forEach(i -> {
                if (Objects.nonNull(operatorMap.get(i.getOperatorId()))) {
                    i.setOperatorName(operatorMap.get(i.getOperatorId()));
                }
                if (Objects.nonNull(stationMap.get(i.getStationId()))) {
                    i.setStationName(stationMap.get(i.getStationId()));
                }
                i.setExitTimeStr(DateUtil.format(i.getExitTime(), DatePattern.NORM_DATETIME_PATTERN));
            });
        }
        //优惠金额合计行
        GreenVo hj = new GreenVo();
        hj.setTollfee(
                greenVos.stream().map(i -> BigDecimal.valueOf(i.getTollfee()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_UP).doubleValue()
        );
        hj.setHj(true);
        hj.setStaDate("优惠前金额合计");
        greenVos.add(hj);
        return greenVos;
    }

    @Override
    public List<CxczVo> cxczTable(CxczDto dto) {
        //获取收费站ID列表
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //判断用户的corpno
        if (dto.getStationId() == -1 && loginUser.getCorpNo().length() == 2) {
            LambdaQueryWrapper<TbStationInfo> tbStationInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tbStationInfoLambdaQueryWrapper.select(TbStationInfo::getStationname, TbStationInfo::getStationhex,
                    TbStationInfo::getStationid).likeRight(TbStationInfo::getCorpno, loginUser.getCorpNo());
            List<TbStationInfo> tbStationInfoList = tbStationInfoMapper.selectList(tbStationInfoLambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(tbStationInfoList)) {
                List<Integer> stationIdList = tbStationInfoList.stream().filter(i -> i.getStationid() != null)
                        .map(TbStationInfo::getStationid).collect(Collectors.toList());
                dto.setStationIdList(stationIdList);
            }
        } else {
            dto.setStationIdList(Collections.singletonList(dto.getStationId()));
        }
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "entry",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        if (CollectionUtils.isEmpty(dto.getTableNameList())) {
            return new ArrayList<>();
        }
        //时间传参格式化
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<CxczVo> cxczVos = specialMapper.cxczTable(dto);
        if (!CollectionUtils.isEmpty(cxczVos)) {
            cxczVos.forEach(i -> {
                i.setEntryTimeStr(DateUtil.format(i.getEntryTime(), DatePattern.NORM_DATETIME_PATTERN));
                if (StringUtils.isNotEmpty(i.getCardId())) {
                    if (i.getCardType() == 22 || i.getCardType() == 23) {
                        i.setCardId(i.getNetWork() + i.getCardId());
                    }
                }
            });
            return cxczVos;
        } else {
            return new ArrayList<>();
        }
    }
}
