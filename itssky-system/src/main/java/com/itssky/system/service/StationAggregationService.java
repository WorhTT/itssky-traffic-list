package com.itssky.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 分组聚合工具类
 */
public class StationAggregationService<T, R> {

    /**
     * 执行分组聚合操作
     */
    public List<R> aggregateData(List<T> dataList, GroupAggDefinition<T, R> definition) {
        // 1. 条件过滤
        Stream<T> filteredStream = dataList.stream();
        if (definition.getCondition() != null) {
            filteredStream = filteredStream.filter(definition.getCondition());
        }

        // 2. 分组：使用分组键计算函数
        Map<String, List<T>> groupedData = filteredStream
                .collect(Collectors.groupingBy(
                        definition.getGroupKeyCalculator(),
                        Collectors.toList()
                ));

        // 3. 对每个分组执行聚合操作并构建结果
        return groupedData.entrySet().stream()
                .map(entry -> {
                    String groupKey = entry.getKey();
                    List<T> groupData = entry.getValue();

                    // 应用所有聚合器
                    Map<String, Object> aggregationResults = new HashMap<>();
                    if (definition.getAggregatorMap() != null) {
                        definition.getAggregatorMap().forEach((fieldName, aggregator) -> {
                            aggregationResults.put(fieldName, aggregator.aggregate(groupData));
                        });
                    }

                    // 构建最终结果对象
                    return definition.getResultBuilder().apply(groupKey, aggregationResults);
                })
                .collect(Collectors.toList());
    }
}
