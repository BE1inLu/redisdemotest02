package com.redisdemo02.controller.userMainController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.entity.SysGod;
import com.redisdemo02.entity.SysOrder;
import com.redisdemo02.service.SysGodService;
import com.redisdemo02.service.UserShopCarService;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;

@RestController
public class userMainController {

    @Autowired
    UserShopCarService userShopCarService;

    @Autowired
    SysGodService sysGodService;

    // TODO:实现用户创建定单接口
    @GetMapping("/userCreateOperater")
    public Result CoreateOperater() {

        // 1，创建订单id
        // 2，读取用户购物车
        // 3，订单信息返回
        // 4，定单信息存储redis

        SysOrder order = new SysOrder();

        order.setUserid((Integer) StpUtil.getTokenInfo().getLoginId());

        Map<String, Object> usercar = userShopCarService.readUserShopCar(order.getUserid());

        // 通过usercar来获取对应的商品键值对
        // 通过key来查询goditemmap（key）
        // 创建List<sysgod> 保存用户要购买的商品
        // 创建sysgod，查询goditemmap的值写入，addcost，putlist
        // list转map
        // ...

        List<SysGod> userShopCarGodList = new ArrayList<>();

        order.setCost(0);

        usercar.forEach((k, v) -> {

            int i = 0;

            SysGod localgod = new SysGod();

            Map<String, Object> localGodMap = sysGodService.getGodItemByMap(Integer.valueOf(k).intValue());

            localgod.setId((int) localGodMap.get("id"));
            localgod.setGoodName((String) localGodMap.get("goodName"));
            localgod.setGodNum((int) v);
            localgod.setGodCost((int) v * (int) localGodMap.get("godCost"));

            i = (int) v * (int) localGodMap.get("godCost");

            order.setCost(order.getCost() + i);

            userShopCarGodList.add(localgod);
        });

        // 构建用户商品map
        Map<String, Object> testMap = BeanUtil.beanToMap(userShopCarGodList);

        order.setStatu(1);

        // TODO:将订单信息推送到list
        


        return Result.succ(MapUtil.builder().put("order", BeanUtil.beanToMap(order)).put("userShopMap",testMap).build());


    }

}
