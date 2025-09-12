package com.itssky.system.domain;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbCorpStationInfoVo {

    private String corpNo;

    private String corpName;

    private Integer level;

    private Integer stationId;

    private Integer uniqueId;

    private String stationHex;

}
