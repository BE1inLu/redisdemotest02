package com.redisdemo02.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redisdemo02.entity.SysGod;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author enluba
 * @since 2023-05-31
 */
public interface SysGodService extends IService<SysGod> {

    /**
     * 获取商品list
     * 
     * @return
     */
    List<SysGod> getGodList();

    /**
     * 获取商品 map
     * 
     * @return
     */
    List<Map<String, Object>> getGodMapAll();

    /**
     * 插入商品信息
     * 
     * @param god
     */
    void saveGodMap(SysGod god);

    /**
     * 移除商品信息
     * 
     * @param id
     */
    void removeGodMap(int id);

    /**
     * 更新商品信息
     * 
     * @param sysGod
     */
    void updateGodMap(SysGod sysGod);

    Map<String, Object> getGodMapById(int id);

    /**
     * 插入商品信息
     * 
     * @param god
     */
    void saveGod(SysGod god);

    /**
     * 移除商品信息
     * 
     * @param id
     */
    void removeGod(int id);

    /**
     * 更新商品信息
     * 
     * @param sysGod
     */
    void updateGod(SysGod sysGod);

    /**
     * 商品减少
     * @param sysGod
     * @return
     */
    boolean subGodNum(SysGod sysGod);

}
