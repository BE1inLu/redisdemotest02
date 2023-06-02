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
import com.redisdemo02.service.MessageService;
import com.redisdemo02.service.SysGodService;
import com.redisdemo02.service.SysOrderService;
import com.redisdemo02.service.UserShopCarService;
import com.redisdemo02.util.castUtil;
import com.redisdemo02.util.redisUtil;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class userMainController {

    @Autowired
    UserShopCarService userShopCarService;

    @Autowired
    SysGodService sysGodService;

    @Autowired
    SysOrderService sysOrderService;

    @Autowired
    MessageService messageService;

    @Autowired
    redisUtil redisUtil;

    /**
     * 创建订单
     * 
     * TODO：增加鉴权
     * 
     * @return
     */
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

        Map<String, Object> orderMap = BeanUtil.beanToMap(order);

        // 写入redis hashmap
        redisUtil.hPutAll("OrderMapid:" + order.getId(), orderMap);

        // 写入队列
        Long listnum = messageService.sendMessage(order);
        log.info("listnum:" + listnum);

        // 写入持久层
        sysOrderService.save(order);

        return Result
                .succ(MapUtil.builder()
                        .put("order", BeanUtil.beanToMap(order))
                        .put("userShopMap", testMap)
                        .build());
    }

    @GetMapping("/checkorder")
    public Result checkorder(int orderid) {
        // Map<String, Object> localMap = (Map<String, Object>) (Object)
        // redisUtil.hGetAll("OrderMapid:" + orderid);
        Map<String, Object> localMap = castUtil.cast(redisUtil.hGetAll("OrderMapid:" + orderid));
        log.info(localMap.toString());
        return Result.succ(MapUtil.builder()
                .put("map", localMap)
                .build());
    }

    @GetMapping("/endorder")
    public Result endorder(int orderid, int statu) {

        Map<String, Object> localMap = castUtil.cast(redisUtil.hGetAll("OrderMapid:" + orderid));

        SysOrder order = BeanUtil.fillBeanWithMapIgnoreCase(localMap, new SysOrder(), false);

        order.setStatu(3);

        sysOrderService.save(order);

        redisUtil.del("OrderMapid:" + orderid);

        return Result.succ("succ endorder");
    }

}
