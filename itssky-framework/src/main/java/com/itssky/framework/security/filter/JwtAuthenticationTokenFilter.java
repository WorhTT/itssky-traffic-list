package com.itssky.framework.security.filter;

import cn.hutool.extra.servlet.ServletUtil;
import com.itssky.client.ItsskySsoClientAbstract;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.core.redis.RedisCache;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.common.utils.StringUtils;
import com.itssky.constant.Constants;
import com.itssky.exception.SsoUncheckedException;
import com.itssky.exception.TokenExpiredException;
import com.itssky.framework.config.IgnoreUrlsConfig;
import com.itssky.framework.web.service.TokenService;
import com.itssky.properties.ScheduleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 * @author itssky
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ItsskySsoClientAbstract itsskySsoClientAbstract;

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private ScheduleProperties scheduleProperties;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        //添加白名单，不需要进行token验证
        if (itsskySsoClientAbstract.permitPass(request, ignoreUrlsConfig.getUrls())) {
            log.info("当前路径:{}放行",request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        //校验是走自己的登陆系统还是使用sso服务器验证。
        if ((Boolean)Constants.cacheMap.get(Constants.REMOTE_SSO_SERVER_STATUS_FLAG)) {
            //处理并转换sso跳转token为本系统原先验证方式
            handleAndTransferSsoJumpToken(request, response, chain);
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
     * 处理并转换sso跳转token为本系统原先验证方式
     *
     * @param request  请求
     * @param response 响应
     * @param chain    链
     */
    private void handleAndTransferSsoJumpToken(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) {
        try {
            //代表远程sso服务器可用
            //并校验自己本地是否有该用户token，没有代表该用户未在本地登陆过，需要走sso登陆流程

            //此方法会校验请求头中是否有规定格式的token，并通过公钥验证token有效性，并校验token是否过期
            //如果token有效，会将用户信息放入SecurityContextHolder中
            //否则会抛出对应的异常
            itsskySsoClientAbstract.preHandle(request, response, redisTemplate,
                scheduleProperties.getRemoteSsoUrl() + "/sso/login", "Authorization", Constants.rsaPublicKey);
            //到此处没有抛出异常，即表示验证通过了
            LoginUser loginUser = getLoginUser(request);
            if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
                //校验时间，如果过期，直接抛出异常
                setAuthenticationToken(request, loginUser, tokenService.getToken(request));
            }
            chain.doFilter(request, response);
        } catch (SsoUncheckedException | TokenExpiredException e) {
            ServletUtil.write(response, e.getMessage(), "application/json;charset=UTF-8");
        } catch (Exception e) {
            log.error("处理sso登陆流程异常!", e);
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
        String totalTokenKey = com.itssky.utils.SecurityUtils.getTotalTokenKey(token);
        return (LoginUser)redisCache.getCacheObject(totalTokenKey);
    }
}
