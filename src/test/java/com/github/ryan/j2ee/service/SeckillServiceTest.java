package com.github.ryan.j2ee.service;

import com.github.ryan.j2ee.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: SeckillServiceTest
 * @date April 23,2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void executeSeckill() throws Exception {
        seckillService.executeSeckill(1000L);
    }

    @Test
    public void getSeckillById() throws Exception {
        Seckill seckill = seckillService.getSeckillById(1000L);
        logger.info("seckill = {}", seckill);
    }


    // test MySQL InnoDB transaction isolation level (RR)
    @Test
    public void testRepeatRead1() throws Exception {
        Seckill seckill = seckillService.getSeckillById(1000L);
    }

    @Test
    public void testRepeatRead2() throws Exception {
        seckillService.doSeckill(1000L);
    }

    @Test
    public void testNextKeyLock1() throws Exception {
        //seckillService.listAllSeckill();
        seckillService.updateSeckillByNumber(98);
    }

    @Test
    public void testNextKeyLock2() throws Exception {
        Seckill seckill = new Seckill();
        seckill.setName("秒杀测试");
        seckill.setNumber(96);
        seckill.setStartTime(new Date());
        seckill.setEndTime(new Date());
        seckill.setCreateTime(new Date());

        seckillService.insertSeckill(seckill);
    }



}