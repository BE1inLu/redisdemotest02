package com.redisdemo02.service.impl;

import com.redisdemo02.entity.SysOrder;
import com.redisdemo02.mapper.SysOrderMapper;
import com.redisdemo02.service.SysOrderService;
import com.redisdemo02.util.castUtil;
import com.redisdemo02.util.redisUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Console;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author enluba
 * @since 2023-05-31
 */
@Service
public class SysOrderServiceImpl extends ServiceImpl<SysOrderMapper, SysOrder> implements SysOrderService {

    @Autowired
    redisUtil redisUtil;

    @Override
    public List<SysOrder> getOrderList() {
        List<SysOrder> orderList;
        try {
            if (redisUtil.get("orderlist") != null) {
                orderList = castUtil.cast(redisUtil.get("orderList"));
            } else {
                orderList = this.list();
                redisUtil.set("orderList", orderList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            orderList = this.list();
        }
        return orderList;

    }

    @Override
    public void saveOrder(SysOrder order) {

        this.save(order);
        redisUtil.set("orderList", this.list());
    }

    @Override
    public void removeOrder(int id) {
        this.removeById(id);
        redisUtil.set("orderList", this.list());
    }

    @Override
    public void updateOrder(SysOrder order) {
        this.updateById(order);
        redisUtil.set("orderList", this.list());
    }

    @Override
    public Map<String, Object> getOrderMapByid(SysOrder order) {

        int orderid = order.getId();

        Map<String, SysOrder> listmap;

        Map<String, Object> localMap;

        // 查询 redis 库
        if (redisUtil.hGetAll("orderMapId:" + order.getId()) != null) {
            localMap = castUtil.cast(redisUtil.hGetAll("orderMapId:" + order.getId()));
        } else {
            // 查询sql库并写入 redis
            order = this.getOne(new QueryWrapper<SysOrder>().eq("id", orderid));
            listmap = castUtil.cast(BeanUtil.beanToMap(order));
            localMap = castUtil.cast(listmap);
            order = BeanUtil.fillBeanWithMap(listmap, new SysOrder(), false);
            redisUtil.hPutAll("orderMapId:" + order.getId(), localMap);
        }
        return localMap;
    }

    @Override
    public List<Map<String, Object>> getOrderMapAll() {

        List<Map<String, Object>> listOrderMap = new ArrayList<>();

        List<Map<String, Object>> localOrderMaplist = this.listMaps();

        Console.log(localOrderMaplist);

        if (redisUtil.hasKey("orderMapIndex")) {
            long i = redisUtil.hSize("orderMapIndex");
            for (Long j = (long) 1; j <= i; j++)
                listOrderMap.add(castUtil.cast(redisUtil.hGetAll("orderMapId" + j.toString())));
        } else {
            localOrderMaplist.forEach((k) -> {
                redisUtil.hashmapset("orderMapIndex", "" + k.get("id"), "orderMapId" +
                        k.get("id"));
                redisUtil.hPutAll("orderMapId:" + k.get("id"), k);
                listOrderMap.add(k);
            });
        }
        return listOrderMap;
    }

    @Override
    public void saveOrderByMap(SysOrder order) {

        // 订单创建

        if (redisUtil.hGetAll("orderMapId:" + order.getId()) != null) {
            Console.log("order is created");
            return;
        }

        this.save(order);

        if (order.getId() == null) {
            SysOrder localorder = this.getOne(new QueryWrapper<SysOrder>().eq("userId", order.getUserid()));
            order.setId(localorder.getId());
        }

        Map<String, Object> orderMap = BeanUtil.beanToMap(order);
        redisUtil.hashmapset("orderMapIndex", order.getId().toString(), "orderMapId" + order.getId());
        redisUtil.hPutAll("orderMapId:" + order.getId(), orderMap);
    }

    @Override
    public void removeOrderMap(SysOrder order) {
        this.removeById(order.getId());
        redisUtil.hashmapdel("orderMapId:" + order.getId(), "userid","id", "statu", "userid", "cost");
        redisUtil.hashmapdel("orderMapIndex", order.getId());
        redisUtil.del("orderMapId:" + order.getId());
    }

    @Override
    public void updateOrderMap(SysOrder order) {
        this.updateById(order);
        Map<String, Object> orderMap = BeanUtil.beanToMap(order);
        redisUtil.hPutAll("orderMapId:" + order.getId(), orderMap);
    }

    @Override
    public void updateOrderMapByRedis(SysOrder order) {
        Map<String, Object> orderMap = BeanUtil.beanToMap(order);
        redisUtil.hPutAll("orderMapId:" + order.getId(), orderMap);
    }

}
