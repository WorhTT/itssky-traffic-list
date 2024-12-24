package com.itssky.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itssky.common.annotation.DynamicTableName;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.utils.MybatisPlusTableNameHelper;
import com.itssky.system.domain.TbStationInfo;
import com.itssky.system.domain.dto.*;
import com.itssky.system.domain.vo.*;
import com.itssky.system.mapper.CardMapper;
import com.itssky.system.mapper.TbStationInfoMapper;
import com.itssky.system.mapper.TollMapper;
import com.itssky.system.service.CardService;
import com.itssky.util.TableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ITSSKY
 */
@Slf4j
@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private TollMapper tollMapper;

    @Autowired
    private TbStationInfoMapper tbStationInfoMapper;

    /**
     * S1收费站通行卡发放班统计表
     *
     * @param dto
     */
    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public List<CardStatisticsVo> s1StationShift(CardStatisticsDto dto) {
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
        if (Objects.nonNull(dto.getTime())) {
            dto.setIntTime(Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN)));
        }
        List<CardStatisticsVo> cardStatisticsVos = new ArrayList<>();
        if (dto.getTableFlag() == 0) {
            cardStatisticsVos = cardMapper.s1StationShift(dto);
        } else if (dto.getTableFlag() == 1) {
            cardStatisticsVos = cardMapper.c1StationShift(dto);
        }
        StationShiftDto tbShDto = new StationShiftDto();
        tbShDto.setTimeFormat(dto.getIntTime());
        tbShDto.setStationIdList(dto.getStationIdList());
        tbShDto.setShiftId(dto.getShiftId());
        List<TbShVo> tbShData = tollMapper.getTbShData(tbShDto);
        //切换动态切换表名格式
        Map<String, Object> map = new HashMap<>();
        map.put(MybatisPlusTableNameHelper.TABLE_TIME, DateUtil.format(dto.getTime(), DatePattern.NORM_YEAR_PATTERN));
        MybatisPlusTableNameHelper.setRequestData(map);
        TbStcDto paramDto = new TbStcDto();
        paramDto.setShiftId(dto.getShiftId());
        paramDto.setStaDate(dto.getIntTime());
        paramDto.setStationIdList(dto.getStationIdList());
        List<TbStcVo> tbStcList = cardMapper.getTbStcList(paramDto);
        for (CardStatisticsVo cardStatisticsVo : cardStatisticsVos) {
            for (TbStcVo tbStcVo : tbStcList) {
                //卡损
                if (cardStatisticsVo.getOperatorId().equals(tbStcVo.getBalanceOp())) {
                    if (dto.getTableFlag() == 0) {
                        cardStatisticsVo.setRecoverNum(tbStcVo.getRecoverNum());
                    } else if (dto.getTableFlag() == 1) {
                        cardStatisticsVo.setBadNum(tbStcVo.getBadRecNum());
                    }
                }
            }
        }
        for (CardStatisticsVo cardStatisticsVo : cardStatisticsVos) {
            for (TbShVo tbShVo : tbShData) {
                //实际
                if (cardStatisticsVo.getOperatorId().equals(tbShVo.getOperatorId())) {
                    if (dto.getTableFlag() == 0) {
                        cardStatisticsVo.setActualNum(tbShVo.getHandOutCNum());
                    } else if (dto.getTableFlag() == 1) {
                        cardStatisticsVo.setActualNum(tbShVo.getHandInCNum());
                    }
                }
            }
        }
        //计算应发卡和总流量
        cardStatisticsVos.forEach(i -> {
            i.setIssuedNum(i.getCustSubTotal() + i.getTruckSubTotal() + i.getSpecSubTotal());
            i.setTotalFlow(i.getIssuedNum() + i.getOfficialNum() + i.getMilitaryNum() + i.getPreferNum() + i.getEtcNum());
        });
        return cardStatisticsVos;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public ExportVo getSCardStationShift(CardStatisticsDto dto) {
        List<CardStatisticsVo> cardStatisticsVos = s1StationShift(dto);
        if (CollectionUtils.isEmpty(cardStatisticsVos)) {
            return new ExportVo(new ArrayList<>(), new ArrayList<>());
        }
        List<SCardStatVo> result = new ArrayList<>();
        cardStatisticsVos.forEach(i -> {
            SCardStatVo sCardStatVo = new SCardStatVo();
            BeanUtils.copyProperties(i, sCardStatVo);
            result.add(sCardStatVo);
        });
        ExportVo exportVo = new ExportVo();
        exportVo.setResult(result);
        exportVo.setConditionList(buildConditionList(dto.getStationId(), dto.getTime()));
        return exportVo;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public ExportVo getCCardStationShift(CardStatisticsDto dto) {
        List<CardStatisticsVo> cardStatisticsVos = s1StationShift(dto);
        if (CollectionUtils.isEmpty(cardStatisticsVos)) {
            return new ExportVo(new ArrayList<>(), new ArrayList<>());
        }
        List<CCardStatVo> result = new ArrayList<>();
        cardStatisticsVos.forEach(i -> {
            CCardStatVo cCardStatVo = new CCardStatVo();
            BeanUtils.copyProperties(i, cCardStatVo);
            result.add(cCardStatVo);
        });
        ExportVo exportVo = new ExportVo();
        exportVo.setResult(result);
        exportVo.setConditionList(buildConditionList(dto.getStationId(), dto.getTime()));
        return exportVo;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public ExportVo getSCardStationDay(CardStatisticsDto dto) {
        List<CardStatisticsVo> cardStatisticsVos = s2StationShift(dto);
        if (CollectionUtils.isEmpty(cardStatisticsVos)) {
            return new ExportVo(new ArrayList<>(), new ArrayList<>());
        }
        List<SCardStatVo> result = new ArrayList<>();
        cardStatisticsVos.forEach(i -> {
            SCardStatVo sCardStatVo = new SCardStatVo();
            BeanUtils.copyProperties(i, sCardStatVo);
            result.add(sCardStatVo);
        });
        ExportVo exportVo = new ExportVo();
        exportVo.setResult(result);
        exportVo.setConditionList(buildConditionList(dto.getStationId(), dto.getTime()));
        return exportVo;
    }

    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public ExportVo getCCardStationDay(CardStatisticsDto dto) {
        List<CardStatisticsVo> cardStatisticsVos = s2StationShift(dto);
        if (CollectionUtils.isEmpty(cardStatisticsVos)) {
            return new ExportVo(new ArrayList<>(), new ArrayList<>());
        }
        List<CCardStatVo> result = new ArrayList<>();
        cardStatisticsVos.forEach(i -> {
            CCardStatVo cCardStatVo = new CCardStatVo();
            BeanUtils.copyProperties(i, cCardStatVo);
            result.add(cCardStatVo);
        });
        ExportVo exportVo = new ExportVo();
        exportVo.setResult(result);
        exportVo.setConditionList(buildConditionList(dto.getStationId(), dto.getTime()));
        return exportVo;
    }

    @Override
    public List<String> buildConditionList(Integer stationId, Date time, Integer shiftId) {
        List<String> conditionList = new ArrayList<>();
        if (stationId == -1) {
            conditionList.add("收费站：中心");
        } else {
            LambdaQueryWrapper<TbStationInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TbStationInfo::getStationid, stationId);
            TbStationInfo tbStationInfo = tbStationInfoMapper.selectOne(wrapper);
            if (Objects.nonNull(tbStationInfo)) {
                conditionList.add("收费站：" + Objects.requireNonNull(tbStationInfo.getStationname()));
            }
        }
        conditionList.add("统计日期：" + DateUtil.format(time, DatePattern.NORM_DATE_PATTERN));
        String shiftName = null;
        if (!Objects.isNull(shiftId)) {
            if (shiftId == 1) {
                shiftName = "早班";
            } else if (shiftId == 2) {
                shiftName = "中班";
            } else if (shiftId == 3) {
                shiftName = "晚班";
            }
            conditionList.add("班次：" + shiftName);
        }
        return conditionList;
    }

    @Override
    public List<String> buildConditionList(Integer stationId, Date beginTime, Date endTime) {
        List<String> conditionList = new ArrayList<>();
        if (stationId == -1) {
            conditionList.add("收费站：中心");
        } else {
            LambdaQueryWrapper<TbStationInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TbStationInfo::getStationid, stationId);
            TbStationInfo tbStationInfo = tbStationInfoMapper.selectOne(wrapper);
            if (Objects.nonNull(tbStationInfo)) {
                conditionList.add("收费站：" + Objects.requireNonNull(tbStationInfo.getStationname()));
            }
        }
        conditionList.add("统计日期：" + DateUtil.format(beginTime, DatePattern.NORM_DATE_PATTERN) + "-" +
                DateUtil.format(endTime, DatePattern.NORM_DATE_PATTERN));
        return conditionList;
    }

    @Override
    public List<String> buildConditionList(Integer stationId, Date time) {
        List<String> conditionList = new ArrayList<>();
        if (stationId == -1) {
            conditionList.add("收费站：中心");
        } else {
            LambdaQueryWrapper<TbStationInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TbStationInfo::getStationid, stationId);
            TbStationInfo tbStationInfo = tbStationInfoMapper.selectOne(wrapper);
            if (Objects.nonNull(tbStationInfo)) {
                conditionList.add("收费站：" + Objects.requireNonNull(tbStationInfo.getStationname()));
            }
        }
        conditionList.add("统计日期：" + DateUtil.format(time, DatePattern.NORM_DATE_PATTERN));
        return conditionList;
    }

    /**
     * S2收费站通行卡发放日统计表
     */
    @Override
    @DynamicTableName(dateParam = "#dto.time")
    public List<CardStatisticsVo> s2StationShift(CardStatisticsDto dto) {
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
        if (Objects.nonNull(dto.getTime())) {
            dto.setIntTime(Integer.parseInt(DateUtil.format(dto.getTime(), DatePattern.PURE_DATE_PATTERN)));
        }
        List<CardStatisticsVo> cardStatisticsVos = cardMapper.s1StationShift(dto);
        StationShiftDto tbShDto = new StationShiftDto();
        tbShDto.setTimeFormat(dto.getIntTime());
        tbShDto.setStationIdList(dto.getStationIdList());
        tbShDto.setShiftId(dto.getShiftId());
        List<TbShVo> tbShData = tollMapper.getTbShData(tbShDto);
        //切换动态切换表名格式
        Map<String, Object> map = new HashMap<>();
        map.put(MybatisPlusTableNameHelper.TABLE_TIME, DateUtil.format(dto.getTime(), DatePattern.NORM_YEAR_PATTERN));
        MybatisPlusTableNameHelper.setRequestData(map);
        TbStcDto paramDto = new TbStcDto();
        paramDto.setShiftId(dto.getShiftId());
        paramDto.setStaDate(dto.getIntTime());
        paramDto.setStationIdList(dto.getStationIdList());
        List<TbStcVo> tbStcList = cardMapper.getTbStcList(paramDto);
        for (CardStatisticsVo cardStatisticsVo : cardStatisticsVos) {
            for (TbStcVo tbStcVo : tbStcList) {
                //恢复卡数量
                if (cardStatisticsVo.getOperatorId().equals(tbStcVo.getBalanceOp())) {
                    cardStatisticsVo.setRecoverNum(tbStcVo.getRecoverNum());
                }
            }
        }
        for (CardStatisticsVo cardStatisticsVo : cardStatisticsVos) {
            for (TbShVo tbShVo : tbShData) {
                //实发卡数量
                if (cardStatisticsVo.getOperatorId().equals(tbShVo.getOperatorId())) {
                    cardStatisticsVo.setActualNum(tbShVo.getHandOutCNum());
                }
            }
        }
        //计算应发卡和总流量
        cardStatisticsVos.forEach(i -> {
            i.setIssuedNum(i.getCustSubTotal() + i.getTruckSubTotal() + i.getSpecSubTotal());
            i.setTotalFlow(i.getIssuedNum() + i.getOfficialNum() + i.getMilitaryNum() + i.getPreferNum() + i.getEtcNum());
        });
        return cardStatisticsVos;
    }

    /**
     * SDT通行卡发放统计表
     *
     * @param dto
     * @return
     */
    @Override
    public List<CardStatisticsVo> sdtStationShift(CardStatisticsDtoV2 dto) {
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
        //构建会查询到的表集合
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbstatentry",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        if (CollectionUtils.isEmpty(dto.getTableNameList())) {
            return new ArrayList<>();
        }
        //时间传参格式化
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<CardStatisticsVo> cardStatisticsVos = cardMapper.sdtStationShift(dto);
        FtStationDto tbShDto = FtStationDto.builder()
                .tableNameList(TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbsh",
                        DatePattern.SIMPLE_MONTH_PATTERN))
                .intBeginTime(dto.getIntBeginTime())
                .intEndTime(dto.getIntEndTime())
                .stationIdList(dto.getStationIdList())
                .statisticsType(dto.getStatisticsType())
                .build();
        List<TbShVo> tbShDataV2 = tollMapper.getTbShDataV2(tbShDto);
        TbStcDtoV2 tbStcDto = TbStcDtoV2.builder()
                .tableNameList(TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbstc",
                        DatePattern.NORM_YEAR_PATTERN))
                .intBeginTime(dto.getIntBeginTime())
                .intEndTime(dto.getIntEndTime())
                .stationIdList(dto.getStationIdList())
                .statisticsType(dto.getStatisticsType())
                .build();
        List<TbStcVo> tbStcListV2 = cardMapper.getTbStcListV2(tbStcDto);
        for (CardStatisticsVo cardStatisticsVo : cardStatisticsVos) {
            for (TbShVo tbSh : tbShDataV2) {
                //日
                if (dto.getStatisticsType().equals("0")) {
                    if (cardStatisticsVo.getStaDate().equals(tbSh.getStaDate())) {
                        cardStatisticsVo.setActualNum(tbSh.getHandOutCNum());
                    }
                }
                //月
                else if (dto.getStatisticsType().equals("1")) {
                    if (cardStatisticsVo.getMonthDate().equals(tbSh.getMonthDate())) {
                        //实缴金额
                        cardStatisticsVo.setActualNum(tbSh.getHandOutCNum());
                    }
                }
                //站
                else if (dto.getStatisticsType().equals("2")) {
                    if (cardStatisticsVo.getStationId().equals(tbSh.getStationId())) {
                        cardStatisticsVo.setActualNum(tbSh.getHandOutCNum());
                    }
                }
            }
        }
        for (CardStatisticsVo cardStatisticsVo : cardStatisticsVos) {
            for (TbStcVo tbStcVo : tbStcListV2) {
                //日
                if (dto.getStatisticsType().equals("0")) {
                    if (cardStatisticsVo.getStaDate().equals(tbStcVo.getStaDate())) {
                        cardStatisticsVo.setRecoverNum(tbStcVo.getRecoverNum());
                    }
                }
                //月
                else if (dto.getStatisticsType().equals("1")) {
                    if (cardStatisticsVo.getMonthDate().equals(tbStcVo.getMonthDate())) {
                        //实缴金额
                        cardStatisticsVo.setRecoverNum(tbStcVo.getRecoverNum());
                    }
                }
                //站
                else if (dto.getStatisticsType().equals("2")) {
                    if (cardStatisticsVo.getStationId().equals(tbStcVo.getStationId())) {
                        cardStatisticsVo.setRecoverNum(tbStcVo.getRecoverNum());
                    }
                }
            }
        }
        //计算应发卡和总流量
        cardStatisticsVos.forEach(i -> {
            i.setIssuedNum(i.getCustSubTotal() + i.getTruckSubTotal() + i.getSpecSubTotal());
            i.setTotalFlow(i.getIssuedNum() + i.getOfficialNum() + i.getMilitaryNum() + i.getPreferNum() + i.getEtcNum());
            if (dto.getStatisticsType().equals("0")) {
                i.setStatType(i.getStaDate().toString());
            }
            else if (dto.getStatisticsType().equals("1")) {
                i.setStatType(i.getMonthDate());
            }
            else if (dto.getStatisticsType().equals("2")) {
                i.setStatType(i.getStationName());
            }
            else if (dto.getStatisticsType().equals("3")) {
                i.setStatType(i.getOperatorId().toString());
            }
        });
        return cardStatisticsVos;
    }

    @Override
    public ExportVo getSdtStationShift(CardStatisticsDtoV2 dto) {
        List<CardStatisticsVo> cardStatisticsVos = sdtStationShift(dto);
        if (CollectionUtils.isEmpty(cardStatisticsVos)) {
            return new ExportVo(new ArrayList<>(), new ArrayList<>());
        }
        List<SdtCardStatVo> result = new ArrayList<>();
        cardStatisticsVos.forEach(i -> {
            SdtCardStatVo sdtCardStatVo = new SdtCardStatVo();
            BeanUtils.copyProperties(i, sdtCardStatVo);
            result.add(sdtCardStatVo);
        });
        ExportVo exportVo = new ExportVo();
        exportVo.setResult(result);
        exportVo.setConditionList(buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return exportVo;
    }

    /**
     * CDT通行卡回收统计表
     */
    @Override
    public List<CdtStatisticsVo> cdtCardRecycle(CardStatisticsDtoV2 dto) {
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
        //构建会查询到的表集合
        dto.setTableNameList(
                TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbstatexit",
                        DatePattern.SIMPLE_MONTH_PATTERN));
        if (CollectionUtils.isEmpty(dto.getTableNameList())) {
            return new ArrayList<>();
        }
        //时间传参格式化
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<CdtStatisticsVo> cdtStatisticsVos = cardMapper.cdtStationShift(dto);
        FtStationDto tbShDto = FtStationDto.builder()
                .tableNameList(TableUtil.generateTableNamesList(dto.getBeginTime(), dto.getEndTime(), "tbsh",
                        DatePattern.SIMPLE_MONTH_PATTERN))
                .intBeginTime(dto.getIntBeginTime())
                .intEndTime(dto.getIntEndTime())
                .stationIdList(dto.getStationIdList())
                .statisticsType(dto.getStatisticsType())
                .build();
        List<TbShVo> tbShDataV2 = tollMapper.getTbShDataV2(tbShDto);
        for (CdtStatisticsVo cardStatisticsVo : cdtStatisticsVos) {
            for (TbShVo tbSh : tbShDataV2) {
                //日
                if (dto.getStatisticsType().equals("0")) {
                    if (cardStatisticsVo.getStaDate().equals(tbSh.getStaDate())) {
                        cardStatisticsVo.setActualNum(tbSh.getHandOutCNum());
                    }
                }
                //月
                else if (dto.getStatisticsType().equals("1")) {
                    if (cardStatisticsVo.getMonthDate().equals(tbSh.getMonthDate())) {
                        //实缴金额
                        cardStatisticsVo.setActualNum(tbSh.getHandOutCNum());
                    }
                }
                //站
                else if (dto.getStatisticsType().equals("2")) {
                    if (cardStatisticsVo.getStationId().equals(tbSh.getStationId())) {
                        cardStatisticsVo.setActualNum(tbSh.getHandOutCNum());
                    }
                }
            }
        }
        //计算应发卡和总流量
        cdtStatisticsVos.forEach(i -> {
            i.setIssuedNum(i.getCustSubTotal() + i.getTruckSubTotal() + i.getSpecSubTotal());
            i.setTotalFlow(i.getIssuedNum() + i.getOfficialNum() + i.getMilitaryNum() + i.getPreferNum() + i.getEtcNum());
            if (dto.getStatisticsType().equals("0")) {
                i.setStatType(i.getStaDate().toString());
            } else if (dto.getStatisticsType().equals("1")) {
                i.setStatType(i.getMonthDate());
            } else if (dto.getStatisticsType().equals("2")) {
                i.setStatType(i.getStationName());
            }
        });

        return cdtStatisticsVos;
    }

    @Override
    public ExportVo getCdtStationShift(CardStatisticsDtoV2 dto) {
        List<CdtStatisticsVo> cdtStatisticsVos = cdtCardRecycle(dto);
        if (CollectionUtils.isEmpty(cdtStatisticsVos)) {
            return new ExportVo(new ArrayList<>(), new ArrayList<>());
        }
        List<CdtCardStatVo> result = new ArrayList<>();
        cdtStatisticsVos.forEach(i -> {
            CdtCardStatVo cdtCardStatVo = new CdtCardStatVo();
            BeanUtils.copyProperties(i, cdtCardStatVo);
            result.add(cdtCardStatVo);
        });
        ExportVo exportVo = new ExportVo();
        exportVo.setResult(result);
        exportVo.setConditionList(buildConditionList(dto.getStationId(), dto.getBeginTime(), dto.getEndTime()));
        return exportVo;
    }
}
