package com.itssky.system.service;

import com.itssky.common.core.domain.entity.TbMenu;
import com.itssky.system.domain.vo.RouterVo;

import java.util.List;

/**
 * 菜单 业务层
 *
 * @author ruoyi
 */
public interface ITbMenuService {


    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<TbMenu> selectMenuTreeByUserId(Long userId);


    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<TbMenu> menus);
}
