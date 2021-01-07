package com.watermelon.sharding_jdbc_sample.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 *
 * @author watermelon
 */
@Component
public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Number nextId(Object entity) {
        SnowflakeShardingKeyGenerator snowflakeShardingKeyGenerator=new SnowflakeShardingKeyGenerator();
        /**
         * 分布式系统中，每个服务的worker.id都需要改成不一样的值,worker.id的范围是从0到1024
         */
        Properties properties=new Properties();
        properties.setProperty("worker.id","1000");
        snowflakeShardingKeyGenerator.setProperties(properties);
        return (Number) snowflakeShardingKeyGenerator.generateKey();
    }
}
