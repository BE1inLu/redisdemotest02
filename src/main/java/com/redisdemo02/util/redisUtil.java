package com.redisdemo02.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class redisUtil {

    @Autowired
    RedisTemplate<String, Object> temp;

    /**
     * <p>
     * 给一个指定的 key 值附加时间
     * </p>
     * 
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        return temp.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * <p>
     * 缓存获取
     * </p>
     * 
     * @param key
     * @return
     */
    public Object get(String key) {
        return (key == null) ? null : temp.opsForValue().get(key);
    }

    /**
     * <p>
     * 缓存写入
     * </p>
     * 
     * @param key
     * @param obj
     * @return
     */
    public boolean set(String key, Object obj) {
        try {
            temp.opsForValue().set(key, obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                temp.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return temp.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p>
     * hashmapset 设置时间
     * </p>
     * 
     * @param key
     * @param item
     * @param obj
     * @param time
     * @return
     */
    public boolean hashmapset(String key, String item, Object obj, long time) {
        try {
            temp.opsForHash().put(key, item, obj);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p>
     * hashmapset 设置
     * </p>
     * 
     * @param key
     * @param item
     * @param obj
     * @return
     */
    public boolean hashmapset(String key, String item, Object obj) {
        try {
            temp.opsForHash().put(key, item, obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p>
     * 批量设置 key 的 faild 值
     * </p>
     * 
     * @param key
     * @param maps
     */
    public void hPutAll(String key, Map<String, Object> maps) {
        temp.opsForHash().putAll(key, maps);
    }

    public Map<Object, Object> hGetAll(String key) {
        Map<Object, Object> local = temp.opsForHash().entries(key);
        return local.isEmpty() ? null : local;
    }

    public Cursor<Entry<Object, Object>> hScan(String key, ScanOptions options) {
        return temp.opsForHash().scan(key, options);
    }

    public Long hSize(String key) {
        return temp.opsForHash().size(key);
    }

    public Boolean hPutIfAbsent(String key, String hashKey, String value) {
        return temp.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * 删除hash表中的值
     *
     * @param key
     * @param item
     */
    public void hashmapdel(String key, Object... item) {
        temp.opsForHash().delete(key, item);
    }

    /**
     * HashMapGet 获取 hash 表键对应的值
     *
     * @param key
     * @param item
     * @return
     */
    public Object hashmapget(String key, String item) {
        return temp.opsForHash().get(key, item);
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                temp.delete(key[0]);
            } else {
                temp.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    public Long listLeftPush(String key, Object item) {
        return temp.opsForList().leftPush(key, item);
    }

    public Object lRightPop(String Key) {
        return temp.opsForList().rightPop(Key);
    }
}
