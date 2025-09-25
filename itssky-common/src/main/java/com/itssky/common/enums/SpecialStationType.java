package com.itssky.common.enums;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 特殊的一些站点聚合
 * 非中心及分中心
 * 例如 沿江的 张家港疏港
 */
public enum SpecialStationType {

    
    ZJGSG(9901, "张家港疏港", "0402", Arrays.asList(70001,70002,70003,70004)),
    TCGSG(9902, "太仓港疏港", "0403", Arrays.asList(20001,20002,20003)),
    CZD(9903, "常州段", "0402", Arrays.asList(100012,100013)),
    WXD(9904, "无锡段", "0402", Arrays.asList(100008,100009,100010,100011)),
    SZZXZJ(9905, "苏州中心值机", "0403", Arrays.asList(100002,100004)),
    ZJGLHZJ(9906, "张家港联合值机", "0403", Arrays.asList(100005,100007)),
    TCLHZJ(9907, "太仓联合值机", "0403", Arrays.asList(100014,100015,100016)),
    ;

    private final int id;
    private final String name;
    private final String corpNo;
    private final List<Integer> stationIds;

    SpecialStationType(int id, String name, String corpNo, List<Integer> stationIds) {
        this.id = id;
        this.name = name;
        this.corpNo = corpNo;
        this.stationIds = stationIds;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCorpNo() {
        return corpNo;
    }

    public List<Integer> getStationIds() {
        return stationIds;
    }

    public static List<Integer> getAllIds() {
        return Arrays.stream(SpecialStationType.values())
                .map(SpecialStationType::getId)
                .collect(Collectors.toList());
    }

    public static List<Integer> getStationIdsById(int id) {
        return Arrays.stream(SpecialStationType.values())
                .filter(specialStationType -> specialStationType.getId() == id)
                .map(SpecialStationType::getStationIds)
                .findFirst()
                .orElse(null);
    }

    public static String getNameById(int id) {
        return Arrays.stream(SpecialStationType.values())
                .filter(specialStationType -> specialStationType.getId() == id)
                .map(SpecialStationType::getName)
                .findFirst()
                .orElse(null);
    }
}