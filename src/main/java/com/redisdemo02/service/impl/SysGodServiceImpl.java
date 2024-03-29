package com.redisdemo02.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redisdemo02.entity.SysGod;
import com.redisdemo02.mapper.SysGodMapper;
import com.redisdemo02.service.SysGodService;
import com.redisdemo02.util.castUtil;
import com.redisdemo02.util.redisUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Console;

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
                localGodList = castUtil.cast(redisUtil.get("godlist"));
                Console.log("redis 读取 godlist");
            } else {
                localGodList = this.list();
                redisUtil.set("godlist", localGodList);
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

    // ====map操作====

    @Override
    public List<Map<String, Object>> getGodMapAll() {
        Map<String, Object> testmap = new HashMap<>();
        List<Map<String, Object>> listtestmap = new ArrayList<>();

        Console.log("redisUtil.hasKey(GodMapIndex)" + redisUtil.hasKey("GodMapIndex"));

        if (redisUtil.hasKey("GodMapIndex")) {

            // 1,获取index num
            Console.log("hsize:" + redisUtil.hSize("GodMapIndex"));
            Long i = redisUtil.hSize("GodMapIndex");
            for (Long j = (long) 1; j <= i; j++) {
                listtestmap.add(castUtil.cast(redisUtil.hGetAll("GodMapId" + j.toString())));

            }
        } else {
            List<Map<String, Object>> localGodMap = this.listMaps();
            localGodMap.forEach((k) -> {
                k.forEach((n, z) -> {
                    testmap.put(n, z);
                });
                redisUtil.hashmapset("GodMapIndex", testmap.get("id").toString(), "GodMapId" + testmap.get("id"));
                redisUtil.hPutAll("GodMapId" + testmap.get("id"), testmap);
            });
            return localGodMap;
        }
        return listtestmap;
    }

    @Override
    public void saveGodMap(SysGod god) {
        this.save(god);

        if (god.getId() == null) {
            SysGod localgod = this.getOne(new QueryWrapper<SysGod>().eq("goodName", god.getGoodName()));
            god.setId(localgod.getId());
        }

        Map<String, Object> godmap = BeanUtil.beanToMap(god);
        redisUtil.hashmapset("GodMapIndex", god.getId().toString(), "GodMapId" + god.getId());
        redisUtil.hPutAll("GodMapId" + god.getId(), godmap);

    }

    @Override
    public void removeGodMap(int id) {
        this.removeById(id);
        redisUtil.hashmapdel("GodMapId" + id, "goodName", "godNum", "godCost", "id");
        redisUtil.hashmapdel("GodMapIndex", "" + id);
        redisUtil.del("GodMapId" + id);
    }

    @Override
    public void updateGodMap(SysGod sysGod) {
        this.updateById(sysGod);
        Map<String, Object> godmap = BeanUtil.beanToMap(sysGod);
        redisUtil.hPutAll("GodMapId" + sysGod.getId(), godmap);
    }

    @Override
    public Map<String, Object> getGodMapById(int id) {
        Map<String, Object> localMap = castUtil.cast(redisUtil.hGetAll("GodMapId" + id));
        return localMap;
    }

    @Override
    public boolean subGodNum(SysGod sysGod) {

        Map<String, Object> localgodmap = this.getGodMapById(sysGod.getId());

        Console.log(localgodmap);

        SysGod god = BeanUtil.fillBeanWithMap(localgodmap, new SysGod(), false);

        Console.log("godnum:" + god.getGodNum());

        int localgodnum = god.getGodNum();

        if (localgodnum - sysGod.getGodNum() < 0) {
            throw new Error("库存为空");
        }

        localgodmap.replace("godNum", localgodnum - sysGod.getGodNum());

        sysGod = BeanUtil.fillBeanWithMap(localgodmap, new SysGod(), false);

        try {
            this.updateGodMap(sysGod);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
