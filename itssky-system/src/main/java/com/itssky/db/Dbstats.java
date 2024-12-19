package com.itssky.db;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS("dbstats")
public @interface Dbstats {
}
