package com.redisdemo02.controller.userMainController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.controller.baseController;
import com.redisdemo02.service.UserShopCarService;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;

@RestController
@RequestMapping("/shopcar")
public class userShopCarController extends baseController {
    // 用户购物车实现

    @Autowired
    UserShopCarService userShopCarService;

    // 1,添加商品
    @SaCheckRole("ROLE_USER")
    @GetMapping("/add")
    public Result shopCarAdd(int godid) {

        int userid = (int) StpUtil.getLoginId();

        boolean bool = userShopCarService.addShopCarItem(userid, godid);

        Console.log("bool:" + bool);

        return Result.succ("add succ");
    }

    // 删除商品
    @SaCheckRole("ROLE_USER")
    @GetMapping("/delitem")
    public Result delShopCarItem(int godid) {

        int userid = (int) StpUtil.getLoginId();

        boolean bool = userShopCarService.delShopCarItem(userid, godid);

        Console.log("bool:" + bool);

        return Result.succ("del succ");
    }

    /**
     * 用户购物车查看
     * 
     * @return
     */
    @SaCheckRole("ROLE_USER")
    @GetMapping("/readlist")
    public Result readusershopcarlist() {

        int userid = (int) StpUtil.getLoginId();

        Map<String, Object> usercarmap = userShopCarService.readUserShopCar(userid);

        return Result.succ(MapUtil.builder()
                .put("usercar", usercarmap)
                .build());
    }

}
