package com.itssky.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.exception.biz.BizException;
import com.itssky.system.domain.TbCorpInfo;
import com.itssky.system.domain.TbStationInfo;
import com.itssky.system.mapper.TbStationInfoMapper;
import com.itssky.system.service.TbCorpInfoService;
import com.itssky.system.service.TbStationInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TB 站信息服务实现
 *
 * @author xiaoma
 * @date 2024/11/27
 */
@Service
public class TbStationInfoServiceImpl extends ServiceImpl<TbStationInfoMapper, TbStationInfo>
        implements TbStationInfoService {

    @Autowired
    private TbCorpInfoService tbCorpInfoService;

    /**
     * 前端下拉选择项
     *
     * @return {@link Map }<{@link String }, {@link Object }>
     */
    @Override
    public Map<String, Object> stationSelectList() {
        //获取当前登陆用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer level = loginUser.getLevel();
        if (level == null) {
            throw new BizException("该用户层级异常");
        }
        if (loginUser == null) {
            throw new BizException("不存在该用户");
        }

        //获取当前登陆用户所在路公司
        String corpNoStr = String.valueOf(loginUser.getCorpNo());
        if (StringUtils.isBlank(corpNoStr)) {
            throw new BizException("该用户所属路公司异常");
        }

        //获取收费站数据
        LambdaQueryWrapper<TbStationInfo> tbstationInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tbstationInfoLambdaQueryWrapper.likeRight(TbStationInfo::getCorpno, corpNoStr);
        List<TbStationInfo> tbstationInfoList = baseMapper.selectList(tbstationInfoLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(tbstationInfoList)) {
            return null;
        }

        //获取路公司相关数据
        LambdaQueryWrapper<TbCorpInfo> tbcorpInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tbcorpInfoLambdaQueryWrapper.likeRight(TbCorpInfo::getCorpno, corpNoStr);
        List<TbCorpInfo> tbcorpInfoList = tbCorpInfoService.list(tbcorpInfoLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(tbstationInfoList)) {
            return null;
        }
        int length = corpNoStr.length();
        Map<String, Object> resultMap = new HashMap<>();
        if (level == 1) {
            //中心
            resultMap.put("value", -1);
            resultMap.put("label", tbcorpInfoList.stream().filter(Objects::nonNull)
                    .filter(obj -> StringUtils.isNotBlank(obj.getCorpno()) && obj.getCorpno().equals(corpNoStr))
                    .map(TbCorpInfo::getCorpname).findFirst().orElse(""));
            List<Map<String, Object>> tmpList = tbcorpInfoList.stream().filter(Objects::nonNull).filter(
                    obj -> StringUtils.isNotBlank(obj.getCorpno()) && obj.getCorpno()
                            .length() == length + 2 && obj.getCorpno().startsWith(corpNoStr)).map(obj -> {
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("label", obj.getCorpname());
                tmpMap.put("value", -1);
                List<Map<String, Object>> tmpThreeList = tbstationInfoList.stream().filter(Objects::nonNull).filter(
                        station -> StringUtils.isNotBlank(station.getCorpno()) && station.getCorpno()
                                .length() == 6 && station.getCorpno().startsWith(obj.getCorpno())).map(station -> {
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put("value", station.getStationid());
                    tempMap.put("label", station.getStationname());
                    return tempMap;
                }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(tmpThreeList)) {
                    tmpMap.put("children", tmpThreeList);
                }
                return tmpMap;
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(tmpList)) {
                resultMap.put("children", tmpList);
            }
        } else if (level == 2) {
            //分中心
            resultMap.put("value", -1);
            resultMap.put("label", tbcorpInfoList.stream().filter(Objects::nonNull)
                    .filter(obj -> StringUtils.isNotBlank(obj.getCorpno()) && obj.getCorpno().equals(corpNoStr))
                    .map(TbCorpInfo::getCorpname).findFirst().orElse(""));
            List<Map<String, Object>> tmpTwoList = tbstationInfoList.stream().filter(Objects::nonNull).filter(
                    obj -> StringUtils.isNotBlank(obj.getCorpno()) && obj.getCorpno()
                            .length() == length + 2 && obj.getCorpno().startsWith(corpNoStr)).map(obj -> {
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("label", obj.getStationname());
                tmpMap.put("value", obj.getStationid());
                return tmpMap;
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(tmpTwoList)) {
                resultMap.put("children", tmpTwoList);
            }
        } else {
            //站
            resultMap.put("value", loginUser.getStationId());
            resultMap.put("label", tbstationInfoList.stream().filter(Objects::nonNull)
                    .filter(obj -> obj.getStationid() != null && obj.getStationid().equals(loginUser.getStationId()))
                    .map(TbStationInfo::getStationname).findFirst().orElse(null));
        }
        return resultMap;
    }

    /**
     * 列表下拉
     *
     * @return {@link Map }<{@link String }, {@link Object }>
     */
    @Override
    public List<Map<String, Object>> listStationSelect() {
        LoginUser loginUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof LoginUser) {
            loginUser = (LoginUser) authentication.getPrincipal();
        }
        if (Objects.isNull(loginUser)) {
            log.error("获取当前登录用户为空");
            throw new RuntimeException("获取当前登录用户为空");
        }
        String corpNo = loginUser.getCorpNo().substring(0, 2);
        LambdaQueryWrapper<TbStationInfo> tbStationInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tbStationInfoLambdaQueryWrapper.select(TbStationInfo::getStationname, TbStationInfo::getStationhex,
                TbStationInfo::getStationid);
        tbStationInfoLambdaQueryWrapper.likeRight(TbStationInfo::getCorpno, corpNo)
                .apply(" length(corpno)=6 ");
        List<TbStationInfo> tbStationInfoList = baseMapper.selectList(tbStationInfoLambdaQueryWrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> center = new HashMap<>();
        center.put("value", 0);
        center.put("label", "中心");
        result.add(center);
        if (CollectionUtils.isEmpty(tbStationInfoList)) {
            return result;
        }
        tbStationInfoList.stream().filter(Objects::nonNull).forEach(obj -> {
            Map<String, Object> tempMap = new HashMap<>();
            if (Objects.nonNull(obj.getStationid())) {
                tempMap.put("value", obj.getStationid());
            } else {
                tempMap.put("value", -1);
            }
            tempMap.put("label", obj.getStationname());
            result.add(tempMap);
        });
        return result;
    }

    /**
     * 当前所属收费站的stationId
     *
     * @return {@link Integer }
     */
    @Override
    public Integer currentAssignStationId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (loginUser == null) {
            throw new BizException("获取用户信息异常!");
        }
        return loginUser.getStationId();
    }
}
