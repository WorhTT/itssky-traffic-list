package com.itssky.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itssky.system.domain.TbStationInfo;

import java.util.List;
import java.util.Map;

/**
 * tbstation SERVICE
 *
 * @author xiaoma
 * @date 2024/11/27
 */
public interface TbStationInfoService extends IService<TbStationInfo> {

    /**
     * 前端下拉选择项
     *
     * @return {@link Map }<{@link String }, {@link Object }>
     */
    public Map<String, Object> stationSelectList();

    /**
     * 列表下拉
     *
     * @return {@link Map }<{@link String }, {@link Object }>
     */
    public List<Map<String, Object>> listStationSelect();

    /**
     * 当前所属收费站的stationId
     *
     * @return {@link Integer }
     */
    public Integer currentAssignStationId();
}

