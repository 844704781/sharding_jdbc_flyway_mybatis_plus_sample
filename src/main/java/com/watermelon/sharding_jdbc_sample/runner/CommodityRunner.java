package com.watermelon.sharding_jdbc_sample.runner;

import com.watermelon.sharding_jdbc_sample.dao.CommodityDao;
import com.watermelon.sharding_jdbc_sample.entity.Commodity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author watermelon
 */
@Component
@Order(1)
@Slf4j
public class CommodityRunner implements ApplicationRunner {

    @Resource
    private CommodityDao commodityDao;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Integer count =commodityDao.selectCount(null);
        if(count == 0){
            insertTestData();
        }
        log.info("finish!!!!!!!!!!!!!");
    }

    private void insertTestData(){
        commodityDao.insert(new Commodity().setCommodityId("a1").setNonce("a1"));
        commodityDao.insert(new Commodity().setCommodityId("b1").setNonce("b1"));
        commodityDao.insert(new Commodity().setCommodityId("c1").setNonce("c1"));
    }
}
