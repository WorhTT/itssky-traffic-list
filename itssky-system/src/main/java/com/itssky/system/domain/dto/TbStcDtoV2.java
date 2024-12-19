package com.itssky.system.domain.dto;

import lombok.*;

import java.util.List;

/**
 * @author ITSSKY
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbStcDtoV2 {

    private Integer intBeginTime;

    private Integer intEndTime;

    private List<Integer> stationIdList;

    private String statisticsType;

    private List<String> tableNameList;
}
