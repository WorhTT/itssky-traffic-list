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
public class TbStcDto {

    private Integer staDate;

    private Integer shiftId;

    private List<Integer> stationIdList;
}
