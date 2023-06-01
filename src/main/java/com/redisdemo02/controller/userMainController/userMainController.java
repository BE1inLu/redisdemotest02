package com.redisdemo02.controller.userMainController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.entity.SysOrder;

import cn.dev33.satoken.stp.StpUtil;

@RestController
public class userMainController {



    // TODO:实现用户创建定单接口
    @GetMapping("/userCreateOperater")
    public Result CoreateOperater(){

        // 1，创建订单id
        // 2，读取用户购物车
        // 3，订单信息返回
        // 4，定单信息存储redis

        SysOrder order=new SysOrder();

        order.setUserid((Integer)StpUtil.getTokenInfo().getLoginId());

        
        



        return Result.succ("create succ");
    }
    
}
