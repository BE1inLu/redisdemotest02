package com.redisdemo02.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.redisdemo02.entity.SysUser;
import com.redisdemo02.service.SysUserService;

import cn.dev33.satoken.stp.StpInterface;

@Component
public class stpInterfaceImpl implements StpInterface {

    @Autowired
    SysUserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        System.out.println("loginid:"+loginId);

        // 1,获取该用户id
        SysUser user = userService.getById((Serializable)loginId);

        List<String> list = new ArrayList<String>();

        switch (user.getAuth()) {
            case 0:
                list.add("ROLE_ADMIN");
                list.add("ROLE_SHOP");
                list.add("ROLE_USER");
                break;
            case 1:
                list.add("ROLE_SHOP");
                list.add("ROLE_USER");
                break;
            default:
                list.add("ROLE_USER");
                break;
        }
        return list;
    }

}
