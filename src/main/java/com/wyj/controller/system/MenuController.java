package com.wyj.controller.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyj.common.entity.Retval;
import com.wyj.entity.system.Menu;
import com.wyj.service.system.MenuService;

/**
 * 菜单Controller
 * 
 * 
 * @author：WangYuanJun
 * @date：2017年11月22日 下午8:25:35
 */
@RestController
@RequestMapping(value = "/remote/menu")
public class MenuController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public String query(@RequestParam(value = "offset", required = true, defaultValue = "1") Integer page, @RequestParam(value = "limit", required = false, defaultValue = "10") Integer pageSize, Long parentId) {
        PageHelper.startPage(page, pageSize);
        if(parentId == null){
            parentId = -1l;
        }
        List<Menu> menus = null;
        Menu menu = new Menu();
        menu.setParentId(parentId);
        menus = menuService.listNotButton(menu);
        PageInfo<Menu> pageInfo = new PageInfo<Menu>(menus);
        return JSON.toJSONString(pageInfo.getList());
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Retval save(Menu menu) {
        Retval retval = Retval.newInstance();
        try {
            if (menu.getMenuId() == null) {
                menuService.save(menu);
            } else {
                menuService.update(menu);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return retval;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Retval edit(@PathVariable String id) {
        Retval retval = Retval.newInstance();
        Menu menu = menuService.getObjectById(Long.valueOf(id));
        retval.put("obj", menu);
        return retval;
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public Retval remove(@RequestParam Long[] ids) {
        Retval retval = Retval.newInstance();
        try {
            retval = menuService.batchRemoveMenu(ids);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            retval.fail(e.getMessage());
        }
        return retval;
    }

    @ResponseBody
    @RequestMapping(value = "/renderTree", method = RequestMethod.GET)
    public List<Menu> renderTree() {
        return menuService.listNotButton(null);
    }

    
}
