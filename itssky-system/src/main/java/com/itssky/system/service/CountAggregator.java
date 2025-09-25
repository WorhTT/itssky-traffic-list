package com.itssky.system.service;

import java.util.List;

/**
 * 计数聚合器
 */
public class CountAggregator<T> implements Aggregator<T, Integer> {

    @Override
    public Integer aggregate(List<T> groupData) {
        return (int) groupData.size();
    }
}
