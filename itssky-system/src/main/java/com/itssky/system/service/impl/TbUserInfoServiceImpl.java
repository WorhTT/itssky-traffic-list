package com.itssky.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.system.domain.TbUserInfo;
import com.itssky.system.service.TbUserInfoService;
import com.itssky.system.mapper.TbUserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author Ma130
* @description 针对表【tbuserinfo】的数据库操作Service实现
* @createDate 2024-09-24 17:53:59
*/
@Service
public class TbUserInfoServiceImpl extends ServiceImpl<TbUserInfoMapper, TbUserInfo>
    implements TbUserInfoService{


    @Autowired
    private TbUserInfoMapper tbUserInfoMapper;

    @Override
    public TbUserInfo getLoginUserInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("当前获取的登录用户信息为空");
        }
        Long userId = loginUser.getUserId();
        LambdaQueryWrapper<TbUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbUserInfo::getUserid, userId);
        TbUserInfo tbUserInfo = tbUserInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(tbUserInfo)) {
            throw new RuntimeException("根据当前登录用户userId获取的用户信息为空");
        }
        return tbUserInfo;
    }
}




