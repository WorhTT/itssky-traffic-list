package com.itssky.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.enums.SpecialStationType;
import com.itssky.common.exception.biz.BizException;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.system.domain.TbCorpInfo;
import com.itssky.system.domain.TbCorpStationInfoVo;
import com.itssky.system.domain.TbStationInfo;
import com.itssky.system.mapper.TbCorpInfoMapper;
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
    private TbCorpInfoMapper tbCorpInfoMapper;

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
        List<TbCorpInfo> tbcorpInfoList = tbCorpInfoMapper.selectList(tbcorpInfoLambdaQueryWrapper);
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
    public List<Map<String, Object>> listStationSelect(boolean needCenter) {
        LoginUser loginUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof LoginUser) {
            loginUser = (LoginUser) authentication.getPrincipal();
        }
        if (Objects.isNull(loginUser)) {
            log.error("获取当前登录用户为空");
            throw new RuntimeException("获取当前登录用户为空");
        }
        String corpNo = loginUser.getCorpNo();
        LambdaQueryWrapper<TbStationInfo> tbStationInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tbStationInfoLambdaQueryWrapper.select(TbStationInfo::getStationname, TbStationInfo::getStationhex,
                TbStationInfo::getStationid);
        tbStationInfoLambdaQueryWrapper.likeRight(TbStationInfo::getCorpno, corpNo)
                .apply(" length(corpno)=6 ");
        if (corpNo.length() == 6) {
            tbStationInfoLambdaQueryWrapper.eq(TbStationInfo::getStationid, loginUser.getStationId());
        }
        List<TbStationInfo> tbStationInfoList = baseMapper.selectList(tbStationInfoLambdaQueryWrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        if (needCenter && corpNo.length() != 6) {
            Map<String, Object> center = new HashMap<>();
            center.put("value", -1);
            center.put("label", "中心");
            result.add(center);
        }
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
     * 获取权限涉及范围内的StationIdList
     */
    @Override
    public List<Integer> getAuthRangeStationIdList(Integer stationId, LoginUser loginUser) {
        //-1是中心
        //三位数的是分中心需要补零，四位数的也是分中心
        //其余的都考虑是站ID
        boolean isRoot = loginUser.getCorpNo().length() == 2;
        if (stationId == -1) {
            String corpNo = loginUser.getCorpNo();
            LambdaQueryWrapper<TbStationInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.likeRight(TbStationInfo::getCorpno, corpNo);
            List<TbStationInfo> tbStationInfos = baseMapper.selectList(lambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(tbStationInfos)) {
                return tbStationInfos.stream().map(TbStationInfo::getStationid).collect(Collectors.toList());
            }

        }
        //需要判断是否是中心用户 中心用户则取这个传参(如果位数不对需补0) ,分中心用户则直接获取corpNo下的所有tbStationInfo
        else if (stationId <= 9999) {
            List<Integer> allIds = SpecialStationType.getAllIds();
            if (allIds.contains(stationId)) {
                return SpecialStationType.getStationIdsById(stationId);
            } else if (isRoot) {
                //中心用户
                String corpNo;
                if (stationId < 1000) {
                    corpNo = "0" + stationId;
                } else {
                    corpNo = String.valueOf(stationId);
                }
                LambdaQueryWrapper<TbStationInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.likeRight(TbStationInfo::getCorpno, corpNo);
                List<TbStationInfo> tbStationInfos = baseMapper.selectList(lambdaQueryWrapper);
                if (!CollectionUtils.isEmpty(tbStationInfos)) {
                    return tbStationInfos.stream().map(TbStationInfo::getStationid).collect(Collectors.toList());
                }
            }
            //非顶层中心用户
            else {
                LambdaQueryWrapper<TbStationInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.likeRight(TbStationInfo::getCorpno, loginUser.getCorpNo());
                List<TbStationInfo> tbStationInfos = baseMapper.selectList(lambdaQueryWrapper);
                if (!CollectionUtils.isEmpty(tbStationInfos)) {
                    return tbStationInfos.stream().map(TbStationInfo::getStationid).collect(Collectors.toList());
                }
            }
        } else {
            return Collections.singletonList(stationId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> listStationSelectV2(boolean needCenter) {
        List<Map<String, Object>> result = new ArrayList<>();
        LoginUser loginUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof LoginUser) {
            loginUser = (LoginUser) authentication.getPrincipal();
        }
        if (Objects.isNull(loginUser)) {
            log.error("获取当前登录用户为空");
            throw new RuntimeException("获取当前登录用户为空");
        }
        if (Objects.isNull(loginUser.getCorpNo())) {
            log.error("当前登录用户的所属路公司CorpNo为空，请联系运维人员");
            throw new RuntimeException("当前登录用户的所属路公司CorpNo为空，请联系运维人员");
        }
        String corpNo = loginUser.getCorpNo();
        if (corpNo.length() % 2 != 0) {
            log.error("当前登录用户的所属路公司CorpNo位数有误，请联系运维人员");
            throw new RuntimeException("当前登录用户的所属路公司CorpNo位数有误，请联系运维人员");
        }
        //顶层路公司用户
        if (needCenter && (corpNo.length() == 2 || corpNo.length() == 4)) {
            LambdaQueryWrapper<TbCorpInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.likeRight(TbCorpInfo::getCorpno, corpNo)
                    .apply(" LENGTH(CorpNo) <= 4 ");
            List<TbCorpInfo> tbCorpInfoList = tbCorpInfoMapper.selectList(lambdaQueryWrapper);
//            //特殊的聚合路公司设定
//            List<TbCorpInfo> specialCorpList = new ArrayList<>();
//            Arrays.stream(SpecialStationType.values()).forEach(obj -> {
//                if (obj.getCorpNo().startsWith(corpNo)) {
//                    TbCorpInfo tbCorpInfo = new TbCorpInfo();
//                    tbCorpInfo.setCorpname(obj.getName());
//                    tbCorpInfo.setCorpno(String.valueOf(obj.getId()));
//                    specialCorpList.add(tbCorpInfo);
//                }
//            });
//            tbCorpInfoList.addAll(specialCorpList);
            if (!CollectionUtils.isEmpty(tbCorpInfoList)) {
                tbCorpInfoList.forEach(obj -> {
                    Map<String, Object> map = new HashMap<>();
                    if (obj.getCorpno().length() == 2) {
                        map.put("value", -1);
                        map.put("label", obj.getCorpname());
                    } else if (obj.getCorpno().length() == 4) {
                        map.put("value", Integer.parseInt(obj.getCorpno()));
                        map.put("label", obj.getCorpname());
                    }
                    result.add(map);
                });
            }
        }
        //获取站选项
        LambdaQueryWrapper<TbStationInfo> stationInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stationInfoLambdaQueryWrapper.likeRight(TbStationInfo::getCorpno, corpNo)
                .apply(" LENGTH(CorpNo) = 6 ");
        //站级别的用户，EQ本站StationID
        if (corpNo.length() == 6) {
            stationInfoLambdaQueryWrapper.eq(TbStationInfo::getStationid, loginUser.getStationId());
        }
        List<TbStationInfo> tbStationInfoList = baseMapper.selectList(stationInfoLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(tbStationInfoList)) {
            tbStationInfoList.forEach(obj -> {
                Map<String, Object> map = new HashMap<>();
                map.put("value", obj.getStationid());
                map.put("label", obj.getStationname());
                result.add(map);
            });
        }
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

    @Override
    public List<Map<String, Object>> centerOptions() {
        List<Map<String, Object>> tempList = new ArrayList<>();
        List<TbCorpStationInfoVo> list = new ArrayList<>();
        LoginUser loginUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof LoginUser) {
            loginUser = (LoginUser) authentication.getPrincipal();
        }
        if (Objects.isNull(loginUser)) {
            log.error("获取当前用户为空");
            return tempList;
        } else {
            if (SecurityUtils.isAdmin(loginUser.getUserId())) {
                List<TbCorpInfo> corpInfoList = tbCorpInfoMapper.selectTbCorpInfoList(null);
                if (!CollectionUtils.isEmpty(corpInfoList)) {
                    corpInfoList.forEach(i -> {
                        TbCorpStationInfoVo build = TbCorpStationInfoVo.builder()
                                .corpName(i.getCorpname())
                                .corpNo(i.getCorpno())
                                .stationId(Integer.parseInt(i.getCorpno()))
                                .stationHex(i.getLevel() == 1 ? "中心" : "分中心")
                                .build();
                        list.add(build);
                    });
                } else {
                    log.error("获取路公司信息列表为空");
                    return tempList;
                }
            }
            //非管理员
            else {
                TbCorpInfo param = new TbCorpInfo();
                if (Objects.isNull(loginUser.getCorpNo())) {
                    throw new RuntimeException("当前用户获取到的CorpNo为空，请联系维护人员");
                }
                if (loginUser.getCorpNo().length() % 2 != 0) {
                    throw new RuntimeException("当前用户所属路公司编号位数有误，请联系维护人员");
                }
                //获取当前用户顶层路公司corpno
                if (loginUser.getCorpNo().length() <= 4) {
                    List<String> corpNoList = generateChildCorpPaths(loginUser.getCorpNo());
                    param.setCorpNoList(corpNoList);
                } else if (loginUser.getCorpNo().length() == 6) {
                    param.setCorpNoList(Collections.singletonList(loginUser.getCorpNo().substring(0, 4)));
                }

                List<TbCorpInfo> corpInfoList = tbCorpInfoMapper.selectTbCorpInfoListForTree(param);
                if (!CollectionUtils.isEmpty(corpInfoList)) {
                    corpInfoList.forEach(i -> {
                        TbCorpStationInfoVo build = TbCorpStationInfoVo.builder()
                                .corpName(i.getCorpname())
                                .corpNo(i.getCorpno())
                                .stationId(Integer.parseInt(i.getCorpno()))
                                .stationHex(i.getLevel() == 1 ? "中心" : "分中心")
                                .build();
                        list.add(build);
                    });
                }
            }
            for (TbCorpStationInfoVo r : list) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("stationId", r.getStationId());
                tempMap.put("corpNo", r.getCorpNo());
                tempMap.put("label", r.getCorpName());
                tempMap.put("stationHex", r.getStationHex());
                tempList.add(tempMap);
            }
        }
        return tempList;
    }

    /**
     * 根据中心或分中心的CorpNo获取stationIds
     */
    @Override
    public List<Integer> getStationIdsByCorpNo(Integer corpNo) {
        String corpNoStr = "";
        if (corpNo <= 99) {
            if (corpNo == -1) {
                corpNoStr = SecurityUtils.getLoginUser().getCorpNo();
            } else if (corpNo < 10) {
                corpNoStr = "0" + corpNo;
            } else {
                corpNoStr = corpNo + "";
            }
        } else if (corpNo <= 9999) {
            if (corpNo < 1000) {
                corpNoStr = "0" + corpNo;
            } else {
                corpNoStr = corpNo + "";
            }
        }
        LambdaQueryWrapper<TbStationInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(TbStationInfo::getCorpno, corpNoStr);
        List<TbStationInfo> stationInfoList = baseMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(stationInfoList)) {
            return stationInfoList.stream().map(TbStationInfo::getStationid).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 这个是只从当前的CorpNo往下迭代
     *
     * @param corpNo
     * @return
     */
    public List<String> generateChildCorpPaths(String corpNo) {
        List<String> paths = new ArrayList<>();
        paths.add(corpNo); // 包含当前节点

        // 如果当前CorpNo长度小于6，生成直接子节点
        if (corpNo.length() < 6) {
            // 生成所有可能的直接子节点（00-99）
            for (int i = 0; i <= 99; i++) {
                String child = corpNo + String.format("%02d", i);
                paths.add(child);
            }
        }

        return paths;
    }
}
