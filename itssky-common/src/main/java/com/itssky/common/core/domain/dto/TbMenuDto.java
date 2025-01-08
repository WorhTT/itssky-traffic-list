package com.itssky.common.core.domain.dto;


import lombok.*;

/**
 * @author ITSSKY
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbMenuDto {

    /**
     * 当前用户ID
     */
    private Long userId;

    /**
     * 当前系统的ServiceId
     */
    private Integer serviceId;

}
