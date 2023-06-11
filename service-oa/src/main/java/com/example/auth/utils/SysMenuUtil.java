package com.example.auth.utils;

import com.example.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ZhangX
 * @createDate: 2023/6/11
 * @description:
 */
public class SysMenuUtil {

    public static List<SysMenu> buildTree(List<SysMenu> sysMenus){
        List<SysMenu> trees = new ArrayList<>();
        // 把所有的菜单数据进行遍历
        for (SysMenu sysMenu : sysMenus) {
            // 递归入口 parentId = 0
            if (sysMenu.getParentId().longValue()==0){
                trees.add(getChildren(sysMenu,sysMenus));
            }
        }
        return trees;
    }
    public static SysMenu getChildren(SysMenu sysMenu,List<SysMenu> sysMenus){
        sysMenu.setChildren(new ArrayList<SysMenu>());

        // 遍历所有的菜单数据，判断id和parent_id的对应关系
        for (SysMenu menu : sysMenus) {
            if (sysMenu.getId().longValue() == menu.getParentId().longValue()){
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(getChildren(menu,sysMenus));
            }
        }
        return sysMenu;
    }
}
