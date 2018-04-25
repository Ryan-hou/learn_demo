package com.github.ryan.j2ee.service.impl;


import com.github.ryan.j2ee.dao.SeckillDao;
import com.github.ryan.j2ee.entity.Seckill;
import com.github.ryan.j2ee.service.SeckillService;
import com.github.ryan.j2ee.service.SeckillServiceTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillServiceImpl
 * @date January 14,2017
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Resource(name = "seckillServiceImpl")
    private SeckillService seckillService;

    @Autowired
    private SeckillServiceTemp seckillServiceTemp;

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public int executeSeckill(long seckillId) {
        // avoid this invocation
        seckillService.doSeckill(seckillId);
        // doSeckill(seckillId); // transaction don't work

        //seckillServiceTemp.doSeckill(seckillId);
        return 0;
    }

    @Transactional
    public void doSeckill(long seckillId) {

        logger.info("seckill number = {}", getById(seckillId).getNumber());
        seckillDao.reduceNumber(seckillId);
        logger.info("seckill number = {}", getById(seckillId).getNumber());

        // 测试回滚
        int i = 1 / 0;
    }
}
