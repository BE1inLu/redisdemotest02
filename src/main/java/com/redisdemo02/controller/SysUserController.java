package com.redisdemo02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.service.SysUserService;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author enluba
 * @since 2023-05-31
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;

    /**
     * 获取用户列表
     * @return
     */
    @SaCheckRole(value = {"ROLE_ADMIN","ROLE_USER","ROLE_SHOP"},mode = SaMode.OR)
    @GetMapping("/getlist")
    public Result getlist(){
        
        return Result.succ(sysUserService.list());
    }

}
