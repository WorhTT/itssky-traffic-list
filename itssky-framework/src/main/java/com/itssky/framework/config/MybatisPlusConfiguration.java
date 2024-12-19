package com.itssky.framework.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.itssky.common.utils.MybatisPlusTableNameHelper;
import org.mybatis.spring.annotation.MapperScan;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * mybatis-plus配置
 *
 * @author myc
 * @version 1.0.0
 * @date 2024/7/2 10:24
 */
@Configuration
@MapperScan("com.itssky.system.mapper")
public class MybatisPlusConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisPlusConfiguration.class);

    private static final List<String> dynamicTableNames = Lists.newArrayList("tbstatexit", "tbstatentry", "tbsh", "tbstc");

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("当前执行线程 : {} ; sql: {} ; tableName : {}", Thread.currentThread().getName(), sql,
                        tableName);
            }
            if (StrUtil.isNotBlank(tableName) && tableName.contains("`")) {
                tableName = tableName.replaceAll("`", "");
            }
            if (StrUtil.isNotBlank(tableName) && dynamicTableNames.contains(tableName.toLowerCase())) {
                Map<String, Object> requestData = MybatisPlusTableNameHelper.getRequestData();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("当前threadlocl参数 : {}", requestData);
                }
                if (requestData != null) {
                    return tableName + (String)requestData.get(MybatisPlusTableNameHelper.TABLE_TIME);
                }
            }
            return tableName;
        });
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        // 3.4.3.2 作废该方式
        // dynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
        return interceptor;
    }
}
