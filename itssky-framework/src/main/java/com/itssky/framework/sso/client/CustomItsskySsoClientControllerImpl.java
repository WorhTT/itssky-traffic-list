package com.itssky.framework.sso.client;

import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itssky.common.constant.CacheConstants;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.core.redis.RedisCache;
import com.itssky.controller.ItsskySsoClientControllerAbstract;
import com.itssky.system.domain.TbUserInfo;
import com.itssky.system.service.TbUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 自定义token接口行为
 *
 * @author myc
 * @version 1.0.0
 * @date 2024/10/12 15:03
 */
@Component
public class CustomItsskySsoClientControllerImpl extends ItsskySsoClientControllerAbstract {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(CustomItsskySsoClientControllerImpl.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TbUserInfoService tbUserInfoService;

    @Override
    public void tokenHandle(JWT jwt) {
        String userId = (String)jwt.getPayload("userId");
        String uuid = (String)jwt.getPayload("uuid");
        int exp = Integer.parseInt(String.valueOf(jwt.getPayload("exp")));

        //获取当前事件时间戳(秒)
        long seconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        int expiresIn = (int)(exp - seconds);
        LambdaQueryWrapper<TbUserInfo> tbUserInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tbUserInfoLambdaQueryWrapper.eq(TbUserInfo::getUserid, userId);
        tbUserInfoLambdaQueryWrapper.last(" limit 1 ");
        TbUserInfo tbUserInfo = tbUserInfoService.getOne(tbUserInfoLambdaQueryWrapper);
        if (tbUserInfo == null) {
            throw new RuntimeException("用户不存在");
        }
        try {
            LoginUser loginUser =
                new LoginUser((long)tbUserInfo.getUserid(), tbUserInfo.getCorpno(), tbUserInfo.getUsername(),
                    tbUserInfo.getPassword(), tbUserInfo.getLevel(), tbUserInfo.getStationid(), Collections.emptySet());
            loginUser.setLoginTime(System.currentTimeMillis());
//            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(exp), ZoneId.systemDefault());
            loginUser.setExpireTime((long)exp);
            redisCache.setCacheObject(CacheConstants.LOGIN_TOKEN_KEY + userId + ":" + uuid, loginUser, expiresIn,
                TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("处理token-----存放redis异常!", e);
            throw new RuntimeException("存放redis异常!");
        }
    }
}
