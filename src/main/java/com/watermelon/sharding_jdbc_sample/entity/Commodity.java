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
    /**
     * 一定要用ASSIGN_ID,这样使用的就是配置的雪花算法了
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String commodityId;
    private String nonce;
}
