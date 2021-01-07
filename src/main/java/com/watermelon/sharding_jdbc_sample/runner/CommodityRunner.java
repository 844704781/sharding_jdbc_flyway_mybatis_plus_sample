package com.watermelon.sharding_jdbc_sample.runner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelon.sharding_jdbc_sample.dao.CommodityDao;
import com.watermelon.sharding_jdbc_sample.entity.Commodity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.core.strategy.keygen.UUIDShardingKeyGenerator;
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
    public void run(ApplicationArguments args) {
        Integer count = commodityDao.selectCount(null);
        if (count == 0) {
            insertTestData();
        }
        IPage page = new Page();
        page.setCurrent(1).setSize(10);
        IPage<Commodity> commodityIPage=commodityDao.selectPage(page,new QueryWrapper<Commodity>().lambda().orderByAsc(Commodity::getCommodityId));
        System.out.println(commodityIPage.getRecords());;
        log.info("finish!!!!!!!!!!!!!");
    }

    private void insertTestData() {
        int count = 100;
        for (int i = 0; i < count; i++) {
            commodityDao.insert(new Commodity().setCommodityId("a" + i).setNonce("a" + i));
        }
    }
}
