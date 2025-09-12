package com.itssky.system.domain.vo;

import com.itssky.common.annotation.Excel;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 未使用ETC Vo
 */
@Data
public class UnUseEtcVo {

    /**
     * 日期
     */
    private String staDate;

    /**
     * 收费站名称列表
     */
    private List<String> stations;


    /**
     * 各收费站数据
     */
    private Map<String, Integer> stationData;

    /**
     * 当日小计
     */
    private String dailyTotal;

    /**
     * 获取指定收费站的数据
     */
    public Integer getStationData(String stationCode) {
        return stationData.getOrDefault(stationCode, 0);
    }



}
