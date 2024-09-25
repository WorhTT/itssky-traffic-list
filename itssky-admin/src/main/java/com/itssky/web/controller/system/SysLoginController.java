package com.itssky.web.controller.system;

import cn.hutool.core.util.StrUtil;
import com.itssky.common.constant.Constants;
import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.domain.model.LoginBody;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.framework.web.service.SysLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 登录验证
 *
 * @author itssky
 */
@RestController
public class SysLoginController {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(SysLoginController.class);

    @Autowired
    private SysLoginService loginService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(HttpServletRequest servletRequest, @RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        //请求头中包含captcha-enable:true 既可以避开验证码
        String captchaEnable = servletRequest.getHeader("captcha-enable");
        Boolean capEnable = true;
        try {
            if(StrUtil.isNotBlank(captchaEnable)){
                capEnable = Boolean.valueOf(captchaEnable);
            }
        } catch (Exception e) {
            log.error("转换部分请求头异常!",e);
        }
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
            loginBody.getUuid(), capEnable);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        AjaxResult ajax = AjaxResult.success();
        loginUser.setPermissions(new HashSet<>(Arrays.asList("*:*:*")));
        ajax.put("user", loginUser);
        ajax.put("roles", Arrays.asList("admin"));
        ajax.put("permissions", Arrays.asList("*:*:*"));
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        return AjaxResult.success(new ArrayList<>());
    }
}
