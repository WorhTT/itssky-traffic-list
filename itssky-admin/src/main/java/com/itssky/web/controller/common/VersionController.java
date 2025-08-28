package com.itssky.web.controller.common;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.itssky.common.utils.VersionPropertiesUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * <p>系统信息获取</p>
 * TODO 待接入
 * @version 1.0.0
 * @author myc
 * @date 2025/4/16 9:56
 */
@RestController
@RequestMapping("/api")
public class VersionController {

    public static final String UNKONWN = "unknow";

    @GetMapping("/version")
    public String getVersion() {
        JSONObject tempJson = new JSONObject();
        Properties properties = null;
        try {
            properties = VersionPropertiesUtil.loadVersionProperties();
        } catch (RuntimeException | IOException e) {
            if (StrUtil.isNotBlank(e.getMessage()) && e.getMessage().contains("文件")) {
                tempJson.put("version", UNKONWN);
                tempJson.put("sso_version", UNKONWN);
            }
            System.err.println(e);
        }
        if (properties != null) {
            tempJson.put("version", properties.getProperty("version"));
            tempJson.put("sso_version", properties.getProperty("sso_version"));
        } else {
            tempJson.put("version", UNKONWN);
            tempJson.put("sso_version", UNKONWN);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("data", tempJson);
        return jsonObject.toString();
    }

    @GetMapping("/update-version")
    public String updateVersion(@RequestParam(value = "version", required = false) String version,
        @RequestParam(value = "sso_version", required = false) String ssoVersion) {
        if (StrUtil.isNotBlank(version)) {
            try {
                VersionPropertiesUtil.updateVersionProperty("version", version);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        if (StrUtil.isNotBlank(ssoVersion)) {
            try {
                VersionPropertiesUtil.updateVersionProperty("sso_version", ssoVersion);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return "update ok!";
    }

}
