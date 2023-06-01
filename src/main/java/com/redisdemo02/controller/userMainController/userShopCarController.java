package com.redisdemo02.controller.userMainController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.entity.SysGod;
import com.redisdemo02.service.UserShopCarService;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Console;

@RestController
public class userShopCarController {
    // 用户购物车实现

    @Autowired
    UserShopCarService userShopCarService;

    // 1,添加商品

    @SaCheckRole("ROLE_USER")
    @GetMapping("/shopcaradd")
    public Result shopCarAdd(SysGod god) {

        int id = (int) StpUtil.getLoginId();

        boolean bool = userShopCarService.addShopCarItem(id, god.getId());

        Console.log("bool:" + bool);

        return Result.succ("add succ");
    }

    @SaCheckRole("ROLE_USER")
    @GetMapping("/delshopcaritem")
    public Result delShopCarItem(int carid) {

        int userid = (int) StpUtil.getLoginId();

        boolean bool = userShopCarService.delShopCarItem(userid, carid);

        Console.log("bool:" + bool);

        return Result.succ("del succ");
    }

    

}
