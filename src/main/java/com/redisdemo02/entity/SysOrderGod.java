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
@TableName("sys_order_god")
public class SysOrderGod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * order-god-id
     */
    private Integer id;

    /**
     * orderid
     */
    private Integer orderId;

    /**
     * godid
     */
    private Integer godId;
}
