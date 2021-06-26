### 这是一个shardingsphere-jdbc + mybatis plus + flyway + springboot +mysql +分布式主键ID + Nacos案例

1. pom.xml中加入sharding-jdbc-spring-boot-starter
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.watermelon.sharding_jdbc_sample</groupId>
    <artifactId>sharding_jdbc_sample</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>sharding_jdbc_sample</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.1</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
            <version>5.2.3</version>
        </dependency>

        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
            <version>4.1.1</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```
2. application.yml配置如下
```yaml
server:
  port: 8082

spring:
  shardingsphere:
    datasource:
      names: ds0
      ds0:
        type: com.zaxxer.hikari.HikariDataSource #必填
        jdbc-url: jdbc:mysql://localhost:3306/sharding_jdbc_sample?useSSL=false&&characterEncoding=UTF-8 #必填
        username: root #必填
        password: root #必填
    sharding:
      tables:
        commodity:
          actual-data-nodes: ds0.commodity_$->{0..3} # sql查询时，会使用commodity_{0..3}的格式去查实际表
          table-strategy:
            inline:
              sharding-column: commodityId # 分片ID
              algorithm-expression: commodity_$->{(commodityId.hashCode() & Integer.MAX_VALUE) % 4} # 通过此表达式来计算具体的commodity表
    props:
      sql:
        show: false #可打印实际sql，分表后的实际表名

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: false
#    log-impl:  org.apache.ibatis.logging.stdout.StdOutImpl #sql日志，会把查询结果也打印出来


logging:
  level:
    com:
      watermelon:
        sharding_jdbc_sample:
          dao: debug
```
3. mybatis plus使用分布式主键(sharding jdbc内置的雪花算法实现)
```java
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
```
4. entity中使用配置的雪花算法生成ID
```java
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
```
5. 增加对flyway的支持
因为数据源配置在sharding jdbc的属性里了，所以我们这里要编程式的配置flyway
```java
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
```
