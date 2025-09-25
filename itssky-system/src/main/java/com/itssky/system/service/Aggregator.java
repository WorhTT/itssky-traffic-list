package com.itssky.system.service;

import java.util.List;

public interface Aggregator<T,R> {

    /**
     * 对一组数据进行聚合操作
     * @param groupData 属于同一分组的数据列表
     * @return 聚合结果
     */
    R aggregate(List<T> groupData);
}
