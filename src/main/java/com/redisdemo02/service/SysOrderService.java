package com.redisdemo02.service;

import com.redisdemo02.entity.SysOrder;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author enluba
 * @since 2023-05-31
 */
public interface SysOrderService extends IService<SysOrder> {

    List<SysOrder> getOrderList();

    void saveOrder(SysOrder order);

    void removeOrder(int id);

    void updateOrder(SysOrder order);

}
