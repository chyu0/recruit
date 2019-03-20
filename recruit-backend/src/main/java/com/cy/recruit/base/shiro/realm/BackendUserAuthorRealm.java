package com.cy.recruit.base.shiro.realm;

import com.cy.recruit.enums.EnableEnum;
import com.cy.recruit.model.backend.BackendUser;
import com.cy.recruit.service.jdbc.backend.BackendUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class BackendUserAuthorRealm extends AuthorizingRealm {

    @Resource
    private BackendUserService backendUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /*Map<String, Object> params = new HashMap<>();
        params.put("userCode", (String) super.getAvailablePrincipal(principalCollection));
        List<UserRoleInfo> userRoleInfos = userService.getUserRoleInfos(params);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if(!userRoleInfos.isEmpty()) {
            for(UserRoleInfo role : userRoleInfos) {
                info.addRole(role.getRoleCode());
            }
        }
        return info;*/
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String email = (String)authenticationToken.getPrincipal();
        BackendUser userInfo = backendUserService.getUserInfo(email);
        if (userInfo == null) {//账号或者密码错误
            throw new UnknownAccountException();
        } else if(userInfo.getEnable() == null || userInfo.getEnable().equals(EnableEnum.DISABLE.getCode())) {//无效账号
            throw new DisabledAccountException();
        } else {
            ByteSource salt = ByteSource.Util.bytes(email);
            return new SimpleAuthenticationInfo(userInfo, userInfo.getPassword(),salt , getName());
        }
    }
}
