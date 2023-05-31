package com.redisdemo02.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("sys_god")
public class SysGod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品名
     */
    @TableField("goodName")
    private String goodName;

    /**
     * 商品数量
     */
    @TableField("godNum")
    private Integer godNum;

    /**
     * 商品价格
     */
    @TableField("godCost")
    private Integer godCost;
}
