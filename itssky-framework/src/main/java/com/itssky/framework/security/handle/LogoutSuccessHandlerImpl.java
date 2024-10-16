package com.itssky.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.itssky.common.constant.Constants;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.utils.MessageUtils;
import com.itssky.common.utils.ServletUtils;
import com.itssky.common.utils.StringUtils;
import com.itssky.framework.manager.AsyncManager;
import com.itssky.framework.manager.factory.AsyncFactory;
import com.itssky.framework.web.service.TokenService;
import com.itssky.logout.ItsskySsoClientLogoutAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author itssky
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ItsskySsoClientLogoutAbstract itsskySsoClientLogoutAbstract;


    private static final Logger log = LoggerFactory.getLogger(LogoutSuccessHandlerImpl.class);

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {

        //接收单点登陆平台的退出请求
        try {
            itsskySsoClientLogoutAbstract.handleLogout(request);
        } catch (Exception e) {
            ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error("退出登陆异常!")));
        }

        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(
                AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response,
            JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}
