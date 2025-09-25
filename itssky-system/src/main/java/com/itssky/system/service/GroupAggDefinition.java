package com.itssky.system.service;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分组聚合配置定义
 */
@Getter
public class GroupAggDefinition<T, R> {

    // Getter 方法
    /**
     * 条件表达式（可选），过滤不需要参与聚合的数据
     */
    private Predicate<T> condition;

    /**
     * 分组键计算函数：决定数据如何分组
     * 这是实现"特殊站聚合"的关键
     */
    private Function<T, String> groupKeyCalculator;

    /**
     * 聚合操作映射：Key-字段名，Value-该字段使用的聚合器
     */
    private Map<String, Aggregator<T, ?>> aggregatorMap;

    /**
     * 结果对象构建器：将分组键和聚合结果构建成最终结果对象
     */
    private BiFunction<String, Map<String, Object>, R> resultBuilder;

    // 构造函数、Getter和Setter
    public GroupAggDefinition() {}

    // 流畅接口（Fluent Interface）方法，方便链式调用
    public GroupAggDefinition<T, R> condition(Predicate<T> condition) {
        this.condition = condition;
        return this;
    }

    public GroupAggDefinition<T, R> groupKeyCalculator(Function<T, String> groupKeyCalculator) {
        this.groupKeyCalculator = groupKeyCalculator;
        return this;
    }

    public GroupAggDefinition<T, R> aggregator(String fieldName, Aggregator<T, ?> aggregator) {
        if (this.aggregatorMap == null) {
            this.aggregatorMap = new HashMap<>();
        }
        this.aggregatorMap.put(fieldName, aggregator);
        return this;
    }

    public GroupAggDefinition<T, R> resultBuilder(BiFunction<String, Map<String, Object>, R> resultBuilder) {
        this.resultBuilder = resultBuilder;
        return this;
    }

}
