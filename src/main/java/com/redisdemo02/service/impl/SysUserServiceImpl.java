package com.redisdemo02.service.impl;

import com.redisdemo02.entity.SysUser;
import com.redisdemo02.mapper.SysUserMapper;
import com.redisdemo02.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser checkUser(String username, String password) {

        SysUser checkUser = getOne(new QueryWrapper<SysUser>().eq("username", username));

        if (checkUser == null) {
            return null;
        }

        boolean check = (password.equals(checkUser.getPassword())) ? true : false;

        if (check) {
            checkUser.setPassword(null);
        } else {
            return null;
        }

        return checkUser;

    }

    @Override
    public boolean addUserCount(int shopid,int userid, Integer cost) {

        SysUser shop=this.getById(shopid);

        SysUser user=this.getById(userid);

        if(user.getCost()-shop.getCost()<0){
            return false;
        }

        shop.setCost(shop.getCost()+cost);

        user.setCost(user.getCost()-cost);

        this.updateById(shop);
        this.updateById(user);

        return true;
    }


    

}
