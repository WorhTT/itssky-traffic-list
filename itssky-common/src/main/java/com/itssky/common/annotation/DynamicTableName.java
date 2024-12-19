package com.itssky.common.annotation;

import cn.hutool.core.date.DatePattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DynamicTableName {

    String formatType() default DatePattern.SIMPLE_MONTH_PATTERN;

    String dateParam() default "";
}
