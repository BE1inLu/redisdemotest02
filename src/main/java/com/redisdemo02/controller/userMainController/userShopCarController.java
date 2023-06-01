package com.redisdemo02.controller.userMainController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.entity.SysGod;

import cn.dev33.satoken.annotation.SaCheckRole;

@RestController
public class userShopCarController {
    // 用户购物车实现


    // 1,添加商品

    @SaCheckRole("ROLE_USER")
    @GetMapping("/shopcaradd")
    public Result shopCarAdd(SysGod god){

        // 1,从redis查询
        


        return Result.succ("add succ");
    }


}
