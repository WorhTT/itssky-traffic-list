package com.itssky.system.service.impl;

import com.itssky.common.constant.Constants;
import com.itssky.common.constant.UserConstants;
import com.itssky.common.core.domain.dto.TbMenuDto;
import com.itssky.common.core.domain.entity.TbMenu;
import com.itssky.common.utils.SecurityUtils;
import com.itssky.common.utils.StringUtils;
import com.itssky.system.domain.vo.MetaVo;
import com.itssky.system.domain.vo.RouterVo;
import com.itssky.system.mapper.TbMenuMapper;
import com.itssky.system.service.ITbMenuService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 *
 * @author ruoyi
 */
@Service
public class TbMenuServiceImpl implements ITbMenuService {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private TbMenuMapper menuMapper;


    @Value("${tbsysmodule.id}")
    private Integer serviceId;


    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<TbMenu> selectMenuTreeByUserId(Long userId) {
        List<TbMenu> menus = null;
        if (SecurityUtils.isAdmin(userId)) {
            menus = menuMapper.selectMenuTreeAll(serviceId);
        } else {
            TbMenuDto dto = new TbMenuDto(userId, serviceId);
            menus = menuMapper.selectMenuTreeByUserId(dto);
        }
        return getChildPerms(menus, 0);
    }


    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<TbMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (TbMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), false, menu.getPath()));
            List<TbMenu> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && (UserConstants.TYPE_DIR.equals(menu.getMenuType()) || UserConstants.TYPE_MENU.equals(menu.getMenuType()))) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName("", menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), false, menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(getRouteName("", routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(TbMenu menu) {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            return StringUtils.EMPTY;
        }
        return getRouteName("", menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     * <p>
     * routerName 路由名称
     *
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path) {
        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(TbMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
//        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
//                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
//            routerPath = "/" + menu.getPath();
//        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(TbMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(TbMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType());
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(TbMenu menu) {
        return StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(TbMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<TbMenu> getChildPerms(List<TbMenu> list, int parentId) {
        //这里稍微改动一下 如果父节点是模块类型的 那么设置当前的parentId为0
        Set<Long> parentMenuIdSet = list.stream().map(TbMenu::getParentId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(parentMenuIdSet)) {
            List<TbMenu> tbMenus = menuMapper.selectMenuByIds(parentMenuIdSet);
            Map<Long, TbMenu> parentMenuMap = tbMenus.stream().collect(Collectors.toMap(TbMenu::getMenuId, i -> i));
            list.forEach(i -> {
                if (i.getParentId() != 0) {
                    if (Objects.nonNull(parentMenuMap.get(i.getParentId()))) {
                        TbMenu parentMenu = parentMenuMap.get(i.getParentId());
                        if ("M".equals(parentMenu.getMenuType())) {
                            i.setParentId(0L);
                        }
                    }
                }
            });
        }
        List<TbMenu> returnList = new ArrayList<TbMenu>();
        for (Iterator<TbMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            TbMenu t = (TbMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<TbMenu> list, TbMenu t) {
        // 得到子节点列表
        List<TbMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TbMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<TbMenu> getChildList(List<TbMenu> list, TbMenu t) {
        List<TbMenu> tlist = new ArrayList<TbMenu>();
        Iterator<TbMenu> it = list.iterator();
        while (it.hasNext()) {
            TbMenu n = (TbMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<TbMenu> list, TbMenu t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }
}
