package com.example.wechat.service.impl;

import com.example.model.wechat.Menu;
import com.example.vo.wechat.MenuVo;
import com.example.wechat.mapper.MenuMapper;
import com.example.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-18
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<MenuVo> findMenuInfo() {
        List<MenuVo> result = new ArrayList<>();
        List<Menu> menuList = baseMapper.selectList(null);

        List<Menu> oneMenuList = menuList.stream().filter(menu ->
                menu.getParentId() == 0).collect(Collectors.toList());

        for (Menu menu : oneMenuList) {
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            List<Menu> twoMenuList = menuList.stream().filter(menu1 ->
                            menu1.getParentId().longValue() == menu.getId().longValue())
                    .sorted(Comparator.comparing(Menu::getSort))
                    .collect(Collectors.toList());
            List<MenuVo> children = new ArrayList<>();
            for (Menu menu1 : twoMenuList) {
                MenuVo menuVoChild = new MenuVo();
                BeanUtils.copyProperties(menu1, menuVoChild);
                children.add(menuVoChild);
            }
            menuVo.setChildren(children);
            result.add(menuVo);
        }
        return result;
    }
}
