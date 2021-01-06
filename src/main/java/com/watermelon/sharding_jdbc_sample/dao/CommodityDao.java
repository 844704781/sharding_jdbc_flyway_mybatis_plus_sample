package com.watermelon.sharding_jdbc_sample.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.sharding_jdbc_sample.entity.Commodity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author watermelon
 */
@Mapper
public interface CommodityDao extends BaseMapper<Commodity> {
}
