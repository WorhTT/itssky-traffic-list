package com.itssky.framework.sso.client;

import cn.hutool.jwt.JWT;
import com.itssky.common.core.redis.RedisCache;
import com.itssky.constant.CacheConstants;
import com.itssky.constant.Constants;
import com.itssky.logout.ItsskySsoClientLogoutAbstract;
import com.itssky.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;

/**
 * 自定义客户端登出逻辑
 *
 * @author myc
 * @version 1.0.0
 * @date 2024/10/16 17:06
 */
@Component
public class CustomItsskySsoClientLogoutImpl extends ItsskySsoClientLogoutAbstract {

    @Autowired
    private RedisCache redisCache;

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    @Override
    public void handleLogout(HttpServletRequest httpServletRequest) {
        //获取token
        String logoutToken =
            StringUtils.isNotBlank(getLogoutToken(httpServletRequest)) ? getLogoutToken(httpServletRequest)
                : getToken(httpServletRequest);

        if (StringUtils.isNotBlank(logoutToken)) {
            JWT jwt = SecurityUtils.parseTokenAndVerify(logoutToken, (RSAPublicKey)Constants.rsaPublicKey);
            //获取token中的用户名
            String userId = (String)jwt.getPayload("userId");
            Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + userId + ":*");
            keys.forEach(redisCache::deleteObject);
        }

    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (com.itssky.common.utils.StringUtils.isNotEmpty(token) && token.startsWith(
            com.itssky.common.constant.Constants.TOKEN_PREFIX)) {
            token = token.replace(com.itssky.common.constant.Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 获取注销令牌
     *
     * @param request 请求
     * @return {@link String }
     */
    public String getLogoutToken(HttpServletRequest request) {
        return request.getHeader(com.itssky.constant.Constants.LOGOUT_REQUEST_HEADER);
    }

}
