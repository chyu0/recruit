package com.cy.recruit.service.jdbc.backend.impl;

import com.cy.recruit.mapper.backend.admin.BackendMenuMapper;
import com.cy.recruit.model.backend.admin.BackendMenu;
import com.cy.recruit.service.jdbc.backend.BackendMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BackendMenuServiceImpl implements BackendMenuService{

    @Resource
    private BackendMenuMapper backendMenuMapper;

    @Override
    public Map<Integer, BackendMenu> getMenuList() {

        List<BackendMenu> list = backendMenuMapper.getAllMenuList();
        if(list == null  || list.size() <= 0){
            return null;
        }
        List<BackendMenu> lastMenu = new ArrayList<>();
        Map<Integer, BackendMenu> currentMap = new HashMap<>();
        for(BackendMenu menu : list){
            if(!menu.isChild()){
                lastMenu.add(menu);//取末级菜单
            }
            currentMap.put(menu.getId(), menu);
        }
        Map<Integer, BackendMenu> result = new HashMap<>();
        for(BackendMenu m : lastMenu){
            initMenu(m, currentMap, result);
        }
        return result;
    }

    /**
     * 递归讲菜单进行封装成一个可用的对象
     * @param curr
     * @param currentMap
     * @param result
     */
    private void initMenu(BackendMenu curr, Map<Integer, BackendMenu> currentMap, Map<Integer, BackendMenu> result){
        if(result == null){
            result = new HashMap<>();
        }
        if(curr.getParentId() <= 0){//父菜单式顶级菜单直接返回
            result.put(curr.getId(), curr);
            return ;
        }

        BackendMenu inResult = result.get(curr.getParentId());
        if(inResult != null){//result里面包含了菜单对象，在原有上面进行封装
            List<BackendMenu> children = inResult.getChildMenus();
            if(children == null){
                children = new ArrayList<>();
            }
            for(BackendMenu menu : children){//排除已经存在的子节点，去重复
                if(menu.getId().equals(curr.getId())){
                    return ;
                }
            }
            children.add(curr);
            return ;
        }

        BackendMenu parentMenu = currentMap.get(curr.getParentId());
        List<BackendMenu> childMenus = parentMenu.getChildMenus();
        if(childMenus == null){
            childMenus = new ArrayList<>();
        }
        childMenus.add(curr);
        parentMenu.setChildMenus(childMenus);

        initMenu(parentMenu, currentMap, result);
    }
}
