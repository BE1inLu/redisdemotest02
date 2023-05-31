package com.redisdemo02.controller.authController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.entity.SysUser;
import com.redisdemo02.service.impl.SysUserServiceImpl;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;

@RestController
public class authController {

    @Autowired
    SysUserServiceImpl sysUserService;

    /**
     * 登录接口
     * 
     * @param username
     * @param password
     * @return
     */
    @SaIgnore
    @PostMapping("/login")
    public Result login(String username, String password) {

        SysUser user = sysUserService.checkUser(username, password);

        if (user == null) {
            return Result.fail(401, "login fail!");
        }

        StpUtil.login((int) user.getId(), SaLoginConfig.setExtra("username", user.getUsername()));

        Console.log("toeknInfo: " + StpUtil.getTokenInfo());

        return Result.succ(200, "login succ!",
                MapUtil.builder()
                        .put("ROLE_LIST", StpUtil.getRoleList())
                        .put(StpUtil.getTokenInfo().getTokenName(), StpUtil.getTokenInfo().getTokenValue())
                        .build());
    }

    /**
     * 登出接口
     * 
     * @return
     */
    @SaIgnore
    @GetMapping("/logout")
    public Result logout() {

        StpUtil.logout();
        return Result.succ("logout success!");
    }

}
