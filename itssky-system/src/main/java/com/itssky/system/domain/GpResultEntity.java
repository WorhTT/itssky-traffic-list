package com.itssky.system.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GpResultEntity {

    @JSONField(name = "stationHex")
    private String stationHex;

    @JSONField(name = "mvLicense")
    private String mvLicense;

    @JSONField(name = "flag")
    private String flag;

    @JSONField(name = "inputDate")
    private String inputDate;
}
