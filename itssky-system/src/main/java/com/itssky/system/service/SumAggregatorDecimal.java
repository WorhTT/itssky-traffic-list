package com.itssky.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 求和聚合器
 */
public class SumAggregatorDecimal<T> implements Aggregator<T, BigDecimal>{

    private final Function<T, BigDecimal> valueExtractor;

    public SumAggregatorDecimal(Function<T, BigDecimal> valueExtractor) {
        this.valueExtractor = valueExtractor;
    }

    @Override
    public BigDecimal aggregate(List<T> groupData) {
        return groupData.stream()
                .map(valueExtractor)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

