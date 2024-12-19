package com.itssky.framework.aspectj;

import cn.hutool.core.date.DateUtil;
import com.itssky.common.annotation.DynamicTableName;
import com.itssky.common.utils.MybatisPlusTableNameHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class DynamicTableNameAspect {

    private final SpelExpressionParser parserSpel = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Pointcut("@annotation(com.itssky.common.annotation.DynamicTableName)")
    public void dynamicTableNameMethod() {

    }

    @SneakyThrows
    @Around(value = "dynamicTableNameMethod()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();
        if (log.isDebugEnabled()) {
            log.debug("拦截的方法:{}", method);
        }
        DynamicTableName dynamicTableName = method.getAnnotation(DynamicTableName.class);
        if (log.isDebugEnabled()) {
            log.debug("入参:{}", dynamicTableName.dateParam());
        }
        String value = generateKeyBySpEL(dynamicTableName.dateParam(), dynamicTableName.formatType(), joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("解析入参:{}", value);
        }
        Map<String, Object> map = new HashMap<>();
        map.put(MybatisPlusTableNameHelper.TABLE_TIME, value);
        MybatisPlusTableNameHelper.setRequestData(map);
        return joinPoint.proceed();
    }

    @After(value = "dynamicTableNameMethod()")
    public void after(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            log.debug("执行完毕清除数据 : {}", MybatisPlusTableNameHelper.getRequestData());
        }
        MybatisPlusTableNameHelper.clear();
        if (log.isDebugEnabled()) {
            log.debug("执行完毕清除数据 : {}", MybatisPlusTableNameHelper.getRequestData());
        }
    }

    /**
     * 通过SpEL表达式生成表时间后缀
     *
     * @param key        钥匙
     * @param formatType 格式类型
     * @param pjp        {@link ProceedingJoinPoint}
     * @return {@link String }
     */
    public String generateKeyBySpEL(String key, String formatType, ProceedingJoinPoint pjp) {
        Expression expression = parserSpel.parseExpression(key);
        EvaluationContext context = new StandardEvaluationContext();
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Object[] args = pjp.getArgs();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(methodSignature.getMethod());
        for (int i = 0; i < args.length; i++) {
            context.setVariable(Objects.requireNonNull(paramNames)[i], args[i]);
        }
        Object value = expression.getValue(context);
        if (value instanceof Date) {
            return DateUtil.format((Date)value, formatType);
        } else if (value instanceof String) {
            return value.toString();
        } else {
            return "";
        }
    }
}
