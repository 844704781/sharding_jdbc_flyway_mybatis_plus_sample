package com.watermelon.sharding_jdbc_sample.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author watermelon
 */
@TableName("commodity")
@Data
@Accessors(chain = true)
public class Commodity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String commodityId;
    private String nonce;
}
