package com.itssky.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 表工具类
 *
 * @author myc
 * @version 1.0.0
 * @date 2024/2/28 15:51
 */
@Slf4j
public class TableUtil {

    private TableUtil() {
    }

    public static List<String> generateTableNamesList(Date startDate, Date endDate, String tableNamePrefix,
        String pattern) {
        List<String> tableNamesList = new ArrayList<>();
        if (startDate.after(endDate)) {
            log.error("开始时间不能大于结束时间");
            return tableNamesList;
        }
        //获取开始时间00：00：00
        DateTime startDateBegin = DateUtil.beginOfMonth(startDate);
        DateTime endDateBegin = DateUtil.beginOfMonth(endDate);
        DateTime endDateBeginNext = DateUtil.offsetDay(endDateBegin, 1);
        int index = 1;
        while (startDateBegin.before(endDateBeginNext) && index <= 2000) {
            tableNamesList.add(tableNamePrefix + DateUtil.format(startDateBegin, pattern));
            //暂时先添加年的判断
            if (pattern.endsWith("yyyy")) {
                startDateBegin = DateUtil.offsetMonth(startDateBegin, 12);
            } else {
                startDateBegin = DateUtil.offsetMonth(startDateBegin, 1);
            }
            index++;
        }
        return tableNamesList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
}
