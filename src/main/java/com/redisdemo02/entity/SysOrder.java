package com.redisdemo02.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("sys_order")
public class SysOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联的用户id
     */
    private Integer userid;

    /**
     * 商品所需的总价
     */
    private Integer cost;

    /**
     * 订单状态 0为取消，1为创建，2为执行，3为完成
     */
    private Integer statu;
}
