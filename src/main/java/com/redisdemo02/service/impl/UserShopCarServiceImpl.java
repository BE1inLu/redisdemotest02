package com.redisdemo02.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redisdemo02.common.dto.userShopCarDto;
import com.redisdemo02.service.SysGodService;
import com.redisdemo02.service.UserShopCarService;
import com.redisdemo02.util.castUtil;
import com.redisdemo02.util.redisUtil;

import cn.hutool.core.bean.BeanUtil;

@Service
public class UserShopCarServiceImpl implements UserShopCarService {

    @Autowired
    SysGodService sysGodService;

    @Autowired
    redisUtil redisUtil;

    @Override
    public boolean addShopCarItem(int userid, int godid) {

        if (userid <= 0 && godid <= 0)
            return false;

        try { // 1,查询 redis god map
            userShopCarDto usercar = new userShopCarDto();
            usercar.setGodid(godid);
            usercar.setNum(1);

            redisUtil.hashmapset("userCar:" + userid, "" + usercar.getGodid(), usercar.getNum());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delShopCarItem(int userid, int shopCarId) {

        if (userid <= 0 && shopCarId <= 0)
            return false;

        try {
            redisUtil.hashmapdel("userCar:" + userid, "" + shopCarId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateShopCarItem(int userid, userShopCarDto userShopCar) {
        Map<String, Object> saveShopCarMap = BeanUtil.beanToMap(userShopCar);
        redisUtil.hPutAll("userCar:" + userid, saveShopCarMap);
        return true;
    }

    @Override
    public Map<String, Object> readUserShopCar(int userid) {
        Map<String, Object> localMap = castUtil.cast(redisUtil.hGetAll("userCar:" + userid));
        return localMap;
    }

}
