package com.itssky.framework.security.filter;

import com.itssky.client.ItsskySsoClientAbstract;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.common.utils.StringUtils;
import com.itssky.constant.Constants;
import com.itssky.framework.web.service.TokenService;
import com.itssky.properties.ScheduleProperties;
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
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ItsskySsoClientAbstract itsskySsoClientAbstract;

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private ScheduleProperties scheduleProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        //添加白名单，不需要进行token验证

        //校验是走自己的登陆系统还是使用sso服务器验证。
        Boolean useBoolean = (Boolean)Constants.cacheMap.get(Constants.REMOTE_SSO_SERVER_STATUS_FLAG);
        //        itsskySsoClientAbstract.
        if (useBoolean) {
            //代表远程sso服务器可用
            boolean over = itsskySsoClientAbstract.preHandle(request, response, redisTemplate,
                scheduleProperties.getRemoteSsoUrl() + "/sso/login", "Authorization", "itssky");
            if (over) {
                chain.doFilter(request, response);
                return;
            }
        }

        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
            tokenService.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request, response);
    }
}
