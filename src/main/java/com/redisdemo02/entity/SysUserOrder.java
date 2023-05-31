package com.redisdemo02.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author enluba
 * @since 2023-05-31
 */
@Getter
@Setter
@TableName("sys_user_order")
public class SysUserOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * user-order-id
     */
    private Integer id;

    /**
     * userid
     */
    private Integer userId;

    /**
     * orderid
     */
    private Integer orderId;
}
