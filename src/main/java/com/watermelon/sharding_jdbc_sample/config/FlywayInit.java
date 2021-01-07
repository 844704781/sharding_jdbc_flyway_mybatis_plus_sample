package com.watermelon.sharding_jdbc_sample.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author watermelon
 */
@Component
@Slf4j
public class FlywayInit {

    @Resource
    private ShardingDataSource shardingDataSource;

    /**
     * 因为数据源配置在sharding jdbc的属性里了，所以我们这里要编程式的配置flyway
     */
    @Bean
    public void flyway2(){
        Map<String, DataSource> dataSourceMap = shardingDataSource.getDataSourceMap();
        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
            ClassicConfiguration configuration = new ClassicConfiguration();
            configuration.setDataSource(entry.getValue());
            Flyway flyway = new Flyway(configuration);
            flyway.migrate();
        }
    }
}
