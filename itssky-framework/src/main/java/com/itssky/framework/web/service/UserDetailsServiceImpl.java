package com.itssky.framework.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.exception.ServiceException;
import com.itssky.common.utils.MessageUtils;
import com.itssky.common.utils.StringUtils;
import com.itssky.system.domain.TbUserInfo;
import com.itssky.system.service.TbUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * 用户验证处理
 *
 * @author itssky
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private TbUserInfoService tbUserInfoService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<TbUserInfo> tbUserInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tbUserInfoLambdaQueryWrapper.eq(TbUserInfo::getUserid, username);
        tbUserInfoLambdaQueryWrapper.last("limit 1");
        TbUserInfo tbUserInfo = tbUserInfoService.getOne(tbUserInfoLambdaQueryWrapper);

        if (StringUtils.isNull(tbUserInfo)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }
        passwordService.validate(tbUserInfo);
        return createLoginUser(tbUserInfo);
    }

    public UserDetails createLoginUser(TbUserInfo tbUserInfo) {
        return new LoginUser(Long.valueOf(tbUserInfo.getUserid()), tbUserInfo.getCorpno(), tbUserInfo.getUsername(),
            tbUserInfo.getPassword(), tbUserInfo.getLevel(), tbUserInfo.getStationid(), new HashSet<String>());
    }
}
