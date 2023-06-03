package com.redisdemo02.controller.ShopMainController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.controller.baseController;
import com.redisdemo02.entity.SysGod;
import com.redisdemo02.entity.SysOrder;
import com.redisdemo02.service.MessageService;
import com.redisdemo02.service.SysGodService;
import com.redisdemo02.service.SysOrderService;
import com.redisdemo02.service.SysUserService;
import com.redisdemo02.service.UserShopCarService;
import com.redisdemo02.util.castUtil;
import com.redisdemo02.util.redisUtil;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Console;

@RestController
@RequestMapping("/shop")
public class shopMainController extends baseController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserShopCarService userShopCarService;

    @Autowired
    SysGodService sysGodService;

    @Autowired
    SysOrderService sysOrderService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    redisUtil redisUtil;

    // 商户controller

    @SaCheckRole("ROLE_SHOP")
    @GetMapping("/getorder")
    public Result getOrderByShop() {

        SysOrder order;
        // 1,查看msglist状态
        try {
            order = (SysOrder) messageService.getMessage();
        } catch (Exception e) {
            return Result.succ("waitlist no order");
        }

        // 获取到订单后修改仓库信息
        // 获取用户购物车
        Map<String, Object> usercar = userShopCarService.readUserShopCar(order.getUserid());

        SysGod god = new SysGod();

        usercar.forEach((k, v) -> {
            god.setId(Integer.valueOf(k));
            god.setGodNum((Integer) v);

            // 从用户购物车查询godmap
            // --商品库存数量
            boolean bool = sysGodService.subGodNum(god);
            Console.log("sysGodService.subGodNum(god)" + bool);
        });

        // 切换定单状态
        order.setStatu(2);
        sysOrderService.updateOrderMapByRedis(order);

        // 返回操作信息
        return Result.succ("succ");
    }

    /**
     * 切换定单状态
     * 
     * @param orderid
     * @return
     */
    @SaCheckRole("ROLE_SHOP")
    @GetMapping("/switchorderstatu")
    public Result switchOrderstatu(int orderid) {

        int shopid = (Integer) StpUtil.getLoginId();

        Map<String, Object> localMap = castUtil.cast(redisUtil.hGetAll("OrderMapid:" + orderid));

        SysOrder order = BeanUtil.fillBeanWithMapIgnoreCase(localMap, new SysOrder(), false);

        order.setStatu(3);

        sysOrderService.updateOrderMapByRedis(order);

        sysUserService.addUserCount(order.getUserid(), shopid, order.getCost());

        return Result.succ("switch statu succ");
    }

}
