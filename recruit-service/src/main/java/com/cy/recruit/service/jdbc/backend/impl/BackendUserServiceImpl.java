package com.cy.recruit.service.jdbc.backend.impl;

import com.cy.recruit.ServiceResult;
import com.cy.recruit.enums.EnableEnum;
import com.cy.recruit.enums.backend.RegisterEnum;
import com.cy.recruit.mapper.backend.admin.BackendUserMapper;
import com.cy.recruit.model.backend.BackendUser;
import com.cy.recruit.service.jdbc.backend.BackendUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class BackendUserServiceImpl implements BackendUserService{

    @Resource
    private BackendUserMapper backendUserMapper;

    @Override
    public BackendUser getUserInfo(String email) {
        if(StringUtils.isBlank(email)){
            return null;
        }
        return backendUserMapper.getUserInfo(email);
    }

    @Override
    public ServiceResult registerUser(BackendUser backendUser) {
        ServiceResult result = new ServiceResult();
        if(backendUser == null || StringUtils.isBlank(backendUser.getEmail()) || StringUtils.isBlank(backendUser.getPassword())){
            result.setData(RegisterEnum.PARAMS_ERROR);
            return result;
        }
        try{
            BackendUser userInfo = getUserInfo(backendUser.getEmail());
            if(userInfo != null){//邮箱已注册
                result.setData(RegisterEnum.EMAIL_HAD_REGISTERED);
                return result;
            }
            if(StringUtils.isBlank(backendUser.getName())){
                backendUser.setName(backendUser.getEmail());
            }
            if(backendUser.getEnable() == null){//默认有效
                backendUser.setEnable(EnableEnum.ENABLE.getCode());
            }
            backendUser.setToken(UUID.randomUUID().toString().replaceAll("-",""));
            if(backendUserMapper.registerUser(backendUser) < 0){
                result.setData(RegisterEnum.INNER_ERROR);
                return result;
            }
            result.setData(RegisterEnum.REGISTER_SUCCESS);
            result.setSuccess(true);
            return result;
        }catch (Exception e){
            result.setData(RegisterEnum.INNER_ERROR);
            return result;
        }
    }
}
