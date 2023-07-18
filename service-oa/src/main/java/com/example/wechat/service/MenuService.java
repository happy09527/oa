package com.example.wechat.service;

import com.example.model.wechat.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-18
 */
public interface MenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();

}
