package com.itssky.system.mapper;

import com.itssky.common.core.domain.dto.TbMenuDto;
import com.itssky.common.core.domain.entity.TbMenu;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author ruoyi
 */
public interface TbMenuMapper {

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    public List<TbMenu> selectMenuTreeAll(@Param(value = "serviceId") Integer serviceId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    public List<TbMenu> selectMenuTreeByUserId(TbMenuDto dto);


    public List<TbMenu> selectMenuByIds(Collection<String> menuIds);
}
