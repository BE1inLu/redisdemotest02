package com.redisdemo02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redisdemo02.service.SysGodService;
import com.redisdemo02.service.SysOrderService;
import com.redisdemo02.service.SysUserService;
import com.redisdemo02.service.UserShopCarService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class baseController {

    @Autowired
    SysGodService sysGodService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysOrderService sysOrderService;

    @Autowired
    UserShopCarService userShopCarService;

    @Autowired
    HttpServletRequest req;

    public Page<? extends Object> getPage() {
        int current = ServletRequestUtils.getIntParameter(req, "current", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 10);
        return new Page<>(current, size);
    }
}
