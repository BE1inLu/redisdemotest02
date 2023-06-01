package com.redisdemo02.service;

import java.util.Map;

import com.redisdemo02.common.dto.userShopCarDto;

public interface UserShopCarService {

    // 购物车增删改查

    /**
     * 购物车item增加
     * 
     * @param userShopCar
     * @return
     */
    boolean addShopCarItem(int userid, int godid);

    /**
     * 购物车item删除
     * 
     * @param id
     * @return
     */
    boolean delShopCarItem(int userid, int shopCarId);

    /**
     * 购物车Item修改
     * 
     * @param userShopCar
     * @return
     */
    boolean updateShopCarItem(int userid, userShopCarDto userShopCar);

    /**
     * 查看/读取用户购物车
     * 
     * @param userid
     * @return
     */
    Map<String, Object> readUserShopCar(int userid);

}
