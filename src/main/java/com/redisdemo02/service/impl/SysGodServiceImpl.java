package com.redisdemo02.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.el.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redisdemo02.entity.SysGod;
import com.redisdemo02.mapper.SysGodMapper;
import com.redisdemo02.service.SysGodService;
import com.redisdemo02.util.redisUtil;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.json.JSON;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author enluba
 * @since 2023-05-31
 */
@Service
public class SysGodServiceImpl extends ServiceImpl<SysGodMapper, SysGod> implements SysGodService {

    @Autowired
    redisUtil redisUtil;

    @Override
    public List<SysGod> getGodList() {

        // 1,获取商品列表
        // 2，redis 同步添加 list

        List<SysGod> localGodList;

        try {
            if (redisUtil.get("godlist") != null) {
                localGodList = (List<SysGod>) redisUtil.get("godlist");
                Console.log("redis 读取 godlist");
            } else {
                localGodList = this.list();
                redisUtil.set("godlist", localGodList);
                redisUtil.hPutAll("godListtest", null);
                Console.log("redis 写入 godlist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            localGodList = this.list();
        }

        return localGodList;
    }

    @Override
    public void saveGod(SysGod god) {
        // 插入商品
        this.save(god);
        // 更新redis
        redisUtil.set("godlist", this.list());
    }

    @Override
    public void removeGod(int id) {
        this.removeById(id);
        redisUtil.set("godlist", this.list());
    }

    @Override
    public void updateGod(SysGod sysGod) {
        this.updateById(sysGod);
        redisUtil.set("godlist", this.list());

    }

}
