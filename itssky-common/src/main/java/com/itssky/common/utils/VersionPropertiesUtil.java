package com.itssky.common.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * <p>版本信息文件管理工具</p>
 *
 * @version 1.0.0
 * @author myc
 * @date 2025/4/16 9:58
 */
public class VersionPropertiesUtil {

    // 读取properties文件
    public static Properties loadVersionProperties() throws IOException {
        Resource resource = new ClassPathResource("version.properties");
        if (resource.exists()) {
            return PropertiesLoaderUtils.loadProperties(resource);
        } else {
            throw new RuntimeException("文件不存在");
        }
    }

    // 修改properties文件
    public static void updateVersionProperty(String key, String value) throws IOException {
        Resource resource = new ClassPathResource("version.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);

        // 修改属性
        props.setProperty(key, value);

        // 保存修改
        try (FileOutputStream fos = new FileOutputStream(resource.getFile())) {
            props.store(fos, "Updated version information");
        }
    }

    // 获取特定版本信息
    public static String getVersion(String key) throws IOException {
        Properties props = loadVersionProperties();
        return props.getProperty(key);
    }
}
