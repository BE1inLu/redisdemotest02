package com.redisdemo02.controller.userMainController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.controller.baseController;
import com.redisdemo02.entity.SysGod;
import com.redisdemo02.entity.SysOrder;
import com.redisdemo02.service.MessageService;
import com.redisdemo02.service.SysGodService;
import com.redisdemo02.service.SysOrderService;
import com.redisdemo02.service.UserShopCarService;
import com.redisdemo02.util.castUtil;
import com.redisdemo02.util.redisUtil;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class userMainController extends baseController {

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
     * 
     * @return
     */
    @SaCheckRole("ROLE_USER")
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

            Map<String, Object> localGodMap = sysGodService.getGodMapById(Integer.valueOf(k).intValue());

            localgod.setId((int) localGodMap.get("id"));
            localgod.setGoodName((String) localGodMap.get("goodName"));
            localgod.setGodNum((int) v);
            localgod.setGodCost((int) v * (int) localGodMap.get("godCost"));

            i = (int) v * (int) localGodMap.get("godCost");

            order.setCost(order.getCost() + i);

            userShopCarGodList.add(localgod);
        });

        // 构建用户商品map
        Map<String, Object> userShopCarMap = BeanUtil.beanToMap(userShopCarGodList);

        order.setStatu(1);

        sysOrderService.saveOrderByMap(order);

        // 写入队列
        Long listnum = messageService.sendMessage(order);
        log.info("listnum:" + listnum);

        return Result.succ(MapUtil.builder()
                .put("order", BeanUtil.beanToMap(order))
                .put("userShopCarMap", userShopCarMap)
                .build());
    }

    /**
     * 查看订单状态
     * 
     * @param orderid
     * @return
     */
    @SaCheckRole(value = { "ROLE_ADMIN", "ROLE_USER", "ROLE_SHOP" }, mode = SaMode.OR)
    @GetMapping("/checkorder")
    public Result checkorder(int orderid) {
        Map<String, SysOrder> localMap;
        SysOrder localorder = new SysOrder();
        localorder.setId(orderid);
        localMap = sysOrderService.getOrderMapByid(localorder);
        log.info(localMap.toString());
        return Result.succ(MapUtil.builder()
                .put("map", localMap)
                .build());
    }

    /**
     * 结束定单
     * 
     * @param orderid
     * @param statu
     * @return
     */
    @SaCheckRole("ROLE_USER")
    @GetMapping("/endorder")
    public Result endOrder(int orderid) {

        Map<String, Object> localMap = castUtil.cast(redisUtil.hGetAll("OrderMapid:" + orderid));

        SysOrder order = BeanUtil.fillBeanWithMapIgnoreCase(localMap, new SysOrder(), false);

        order.setStatu(4);

        sysOrderService.save(order);

        // 删除redis数据库用户信息
        redisUtil.del("OrderMapid:" + orderid);
        redisUtil.del("userCar:" + order.getUserid());

        return Result.succ("succ endorder");
    }

}
