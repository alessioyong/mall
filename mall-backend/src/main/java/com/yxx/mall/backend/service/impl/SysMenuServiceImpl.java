package com.yxx.mall.backend.service.impl;

import com.yxx.mall.backend.mapper.SysMenuMapper;
import com.yxx.mall.backend.model.MetaVo;
import com.yxx.mall.backend.model.RouterVo;
import com.yxx.mall.backend.service.SysMenuService;
import com.yxx.mall.common.entity.backend.SysMenuEntity;
import com.yxx.mall.common.utils.RRException;
import com.yxx.mall.common.utils.StringUtils;
import com.yxx.mall.common.utils.constant.Constants;
import com.yxx.mall.common.utils.constant.UserConstants;
import com.yxx.mall.common.utils.security.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xyong
 * date 2021-05-10
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Resource
    SysMenuMapper menuMapper;

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<SysMenuEntity> selectMenuTreeByUserId(Long userId) {
        List<SysMenuEntity> menus=null;
        if(SecurityUtils.isAdmin(userId)){
            menus=menuMapper.selectMenuTreeAll();
        }else {
            menus=menuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus,0);
    }

    @Override
    public List<RouterVo> buildMenus(List<SysMenuEntity> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenuEntity menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
            List<SysMenuEntity> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            else if (isMenuFrame(menu))
            {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * ??????????????????????????????
     * @param menu
     * @param userId
     * @return
     */
    @Override
    public List<SysMenuEntity> selectMenuList(SysMenuEntity menu, Long userId) {
        List<SysMenuEntity> menuList = null;
        // ?????????????????????????????????
        if (SecurityUtils.isAdmin(userId))
        {
            menuList = menuMapper.selectMenuList(menu);
        }
        else
        {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * ????????????????????????????????????
     * @param menuId
     * @return
     */
    @Override
    public SysMenuEntity selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * ????????????
     * @param menu
     * @return
     */
    @Override
    public int insertMenu(SysMenuEntity menu) {
        if(UserConstants.NOT_UNIQUE.equals(checkMenuNameUnique(menu))){
            throw new RRException("????????????'" + menu.getMenuName() + "'??????????????????????????????");
        }else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS))
        {
            throw new RRException("????????????'" + menu.getMenuName() + "'????????????????????????http(s)://??????");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return menuMapper.insertMenu(menu);
    }

    /**
     * ??????????????????????????????
     *
     * @param menu ????????????
     * @return ??????
     */
    public String checkMenuNameUnique(SysMenuEntity menu)
    {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenuEntity info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }



    /**
     * ??????????????????
     *
     * @param menu ????????????
     * @return ????????????
     */
    public String getComponent(SysMenuEntity menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
        {
            component = menu.getComponent();
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
        {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }
    /**
     * ??????????????????
     *
     * @param menu ????????????
     * @return ????????????
     */
    public String getRouteName(SysMenuEntity menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        // ???????????????????????????????????????????????????
        if (isMenuFrame(menu))
        {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * ??????????????????
     *
     * @param menu ????????????
     * @return ????????????
     */
    public String getRouterPath(SysMenuEntity menu)
    {
        String routerPath = menu.getPath();
        // ???????????????????????????????????????????????????
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
        {
            routerPath = "/" + menu.getPath();
        }
        // ???????????????????????????????????????????????????
        else if (isMenuFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }
    /**
     * ???????????????????????????
     *
     * @param menu ????????????
     * @return ??????
     */
    public boolean isMenuFrame(SysMenuEntity menu)
    {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * ?????????parent_view??????
     *
     * @param menu ????????????
     * @return ??????
     */
    public boolean isParentView(SysMenuEntity menu)
    {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }
    /**
     * ??????????????????ID?????????????????????
     *
     * @param list ?????????
     * @param parentId ??????????????????ID
     * @return String
     */
    public List<SysMenuEntity> getChildPerms(List<SysMenuEntity> list, int parentId)
    {
        List<SysMenuEntity> returnList = new ArrayList<SysMenuEntity>();
        for (Iterator<SysMenuEntity> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenuEntity t = (SysMenuEntity) iterator.next();
            // ????????????????????????????????????ID,????????????????????????????????????
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }
    /**
     * ????????????
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenuEntity> list, SysMenuEntity t)
    {
        // ?????????????????????
        List<SysMenuEntity> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenuEntity tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }
    /**
     * ?????????????????????
     */
    private List<SysMenuEntity> getChildList(List<SysMenuEntity> list, SysMenuEntity t)
    {
        List<SysMenuEntity> tlist = new ArrayList<SysMenuEntity>();
        Iterator<SysMenuEntity> it = list.iterator();
        while (it.hasNext())
        {
            SysMenuEntity n = (SysMenuEntity) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * ????????????????????????
     */
    private boolean hasChild(List<SysMenuEntity> list, SysMenuEntity t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
