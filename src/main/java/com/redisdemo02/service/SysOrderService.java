package com.redisdemo02.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redisdemo02.entity.SysOrder;

/**
 * <p>
 * 服务类
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

    // 增删改查（map）

    /**
     * 通过orderid查询，返回map结构
     * redis data获取，查不到再通过sql查询
     * @param order
     * @return
     */
    Map<String, Object> getOrderMapByid(SysOrder order);

    /**
     * 获取所有order信息
     * @return
     */
    List<Map<String, Object>> getOrderMapAll();


    /**
     * 保存order信息，更新redis data
     * @param order
     */
    void saveOrderByMap(SysOrder order);


    /**
     * del orderItem 双更新
     * @param order
     */
    void removeOrderMap(SysOrder order);


    /**
     * 更新orderItem 双更新
     * @param order
     */
    void updateOrderMap(SysOrder order);


    /**
     * 更新orderItem redis更新
     * @param order
     */
    void updateOrderMapByRedis(SysOrder order);

}
