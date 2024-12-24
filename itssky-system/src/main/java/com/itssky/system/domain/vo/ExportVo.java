package com.itssky.system.domain.vo;

import lombok.*;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ExportVo {

    /**
     * 统计条件
     */
    private List<String> conditionList;

    private List<?> result;
}
