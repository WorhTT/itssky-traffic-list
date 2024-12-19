package com.itssky.framework.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.core.redis.RedisCache;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.common.utils.StringUtils;
import com.itssky.constant.LoginStatusConstants;
import com.itssky.framework.config.IgnoreUrlsConfig;
import com.itssky.framework.web.service.TokenService;
import com.itssky.properties.ClientPorperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * token过滤器 验证token有效性
 *
 * @author itssky
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    /**
     * Ant 路径匹配器
     */
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ClientPorperties clientPorperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        //添加白名单，不需要进行token验证
        if (permitPass(request, ignoreUrlsConfig.getUrls())) {
            if (log.isDebugEnabled()) {
                log.debug("当前路径:{}放行", request.getRequestURI());
            }
            chain.doFilter(request, response);
            return;
        }

        //校验是走自己的登陆系统还是使用sso服务器验证。
        if (LoginStatusConstants.getSsoServerHealthy()) {
            //处理并转换sso跳转token为本系统原先验证方式
            LoginUser loginUser = getLoginUser(request);
            log.info("当前请求路径:{}，当前用户信息:{}", request.getRequestURI(), loginUser.toString());
            if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
                //校验时间，如果过期，直接抛出异常
                setAuthenticationToken(request, loginUser, tokenService.getToken(request));
            }
            chain.doFilter(request, response);
        } else {
            //否则走自己的登陆系统
            LoginUser loginUser = tokenService.getLoginUser(request);
            if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
                tokenService.verifyToken(loginUser);
                setAuthenticationToken(request, loginUser, tokenService.getToken(request));
            }
            chain.doFilter(request, response);
        }
    }

    /**
     * 设置鉴权 AuthenticationToken
     *
     * @param request   请求
     * @param loginUser 登录用户
     * @param token     token
     */
    private static void setAuthenticationToken(HttpServletRequest request, LoginUser loginUser, String token) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginUser, token, loginUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    /**
     * 获取登录用户信息(兼容若依原先登陆系统)
     *
     * @param request 请求
     * @return {@link LoginUser }
     */
    private LoginUser getLoginUser(HttpServletRequest request) {
        String token = tokenService.getToken(request);
        String totalTokenKey = com.itssky.utils.SecurityUtils.getTotalTokenKey(clientPorperties.getCacheKeyPrefix() + ":", token);
        return (LoginUser)redisCache.getCacheObject(totalTokenKey);
    }

    /**
     * <p>路径判断是否存在于白名单中</p>
     * <p>白名单路径直接放行</p>
     *
     * @param request 请求
     * @param urlList URL 列表
     * @return boolean
     */
    private boolean permitPass(HttpServletRequest request, List<String> urlList) {
        if (CollectionUtil.isEmpty(urlList)) {
            return false;
        }
        //使用AntPathMatcher进行路径匹配
        String requestURI = request.getRequestURI();
        for (String pattern : urlList) {
            if (antPathMatcher.match(pattern, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
