package com.cy.recruit.controller.admin;

import com.cy.recruit.base.modal.ResultInfo;
import com.cy.recruit.controller.BaseController;
import com.cy.recruit.model.backend.admin.BackendMenu;
import com.cy.recruit.service.jdbc.backend.BackendMenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("menu")
public class MenuController extends BaseController{

    @Resource
    private BackendMenuService backendMenuService;

    @RequestMapping("list")
    public ResultInfo getMenuList(){
        Map<Integer, BackendMenu> menus = backendMenuService.getMenuList();
        if(menus != null && menus.size() > 0){
            return success().setData(menus);
        }else{
            return fail();
        }
    }
}
