package com.itssky.common.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * mybatis-plus 分表 动态表名
 * <p>用于mybatis-plus 动态表名，传递参数</p>
 *
 * @author myc
 * @version 1.0.0
 * @date 2024/11/7 14:46
 */
@Slf4j
public class MybatisPlusTableNameHelper {

    public static final String TABLE_TIME = "tableTime";

    /**
     * 请求参数存取
     */
    private static final ThreadLocal<Map<String, Object>> REQUEST_DATA = new ThreadLocal<>();

    /**
     * 设置请求参数
     *
     * @param requestData 请求参数 MAP 对象
     */
    public static void setRequestData(Map<String, Object> requestData) {
        REQUEST_DATA.set(requestData);
        log.info("REQUEST_DATA SET MAP");
    }

    /**
     * 获取请求参数
     *
     * @param param 请求参数
     * @return 请求参数 MAP 对象
     */
    public static <T> T getRequestData(String param) {
        Map<String, Object> dataMap = getRequestData();
        if (CollectionUtils.isNotEmpty(dataMap)) {
            return (T)dataMap.get(param);
        }
        return null;
    }

    /**
     * 获取请求参数
     *
     * @return 请求参数 MAP 对象
     */
    public static Map<String, Object> getRequestData() {
        return REQUEST_DATA.get();
    }

    public static void clear() {
        REQUEST_DATA.remove();
    }
}
