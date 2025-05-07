package com.itssky.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.itssky.common.annotation.DynamicTableName;
import com.itssky.system.domain.dto.FD06Dto;
import com.itssky.system.domain.vo.FD06Vo;
import com.itssky.system.mapper.ExamineMapper;
import com.itssky.system.mapper.SpecialMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ExamineServiceImpl {

    private final ExamineMapper examineMapper;

    private final SpecialMapper specialMapper;

    @DynamicTableName(dateParam = "#dto.beginTime")
    public List<FD06Vo> getFd06(FD06Dto dto) {
        dto.setIntBeginTime(Integer.parseInt(DateUtil.format(dto.getBeginTime(), DatePattern.PURE_DATE_PATTERN)));
        dto.setIntEndTime(Integer.parseInt(DateUtil.format(dto.getEndTime(), DatePattern.PURE_DATE_PATTERN)));
        List<FD06Vo> list = examineMapper.getFd06(dto);
        if (!CollectionUtils.isEmpty(list)) {
            Set<Integer> operatorIdSet = list.stream().map(FD06Vo::getOperateId).collect(Collectors.toSet());
            List<Map> map = specialMapper.buildOperatorName(operatorIdSet);
            Map<Integer, String> operatorMap = new HashMap<>();
            map.forEach(m -> operatorMap.put(Integer.valueOf(m.get("operatorId").toString()),
                    m.get("operatorName").toString()));
            list.forEach(item -> {
                if (Objects.nonNull(operatorMap.get(item.getOperateId()))) {
                    item.setOperateName(operatorMap.get(item.getOperateId()));
                }
            });
        }
        return list;
    }
}
