package com.itssky.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

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
}
