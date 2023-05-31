package com.redisdemo02.service;

import com.redisdemo02.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author enluba
 * @since 2023-05-31
 */
public interface SysUserService extends IService<SysUser> {

    SysUser checkUser(String username,String password);

}
