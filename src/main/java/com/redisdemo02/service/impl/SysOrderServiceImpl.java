package com.redisdemo02.service.impl;

import com.redisdemo02.entity.SysOrder;
import com.redisdemo02.mapper.SysOrderMapper;
import com.redisdemo02.service.SysOrderService;
import com.redisdemo02.util.castUtil;
import com.redisdemo02.util.redisUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

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

}
