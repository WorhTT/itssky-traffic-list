package com.itssky.system.domain.vo;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TableDataVo {

    private List<?> rows;

    private List<String> conditionList;
}
