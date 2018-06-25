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
import java.util.List;

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

    //@Resource or @Autowired failed!(need new spring version)
    @Resource(name = "seckillServiceImpl")
    private SeckillService seckillService;

    @Autowired
    private SeckillServiceTemp seckillServiceTemp;

    @Transactional // test Isolation Level: RR
    public Seckill getSeckillById(long seckillId) {
        Seckill seckill = seckillDao.querySeckillById(seckillId);
        logger.info("seckill = {}", seckill);
        seckill = seckillDao.querySeckillById(seckillId);
        logger.info("seckill = {}", seckill);
        return seckill;
    }

    public int executeSeckill(long seckillId) {
        // avoid "this" invocation
        seckillService.doSeckill(seckillId);
        // doSeckill(seckillId); // transaction don't work

        //seckillServiceTemp.doSeckill(seckillId);
        return 0;
    }

    @Transactional
    public void doSeckill(long seckillId) {

        logger.info("seckill number = {}", getSeckillById(seckillId).getNumber());
        seckillDao.reduceNumber(seckillId);
        logger.info("seckill number = {}", getSeckillById(seckillId).getNumber());

        // 测试回滚
        //int i = 1 / 0;
    }

    public Integer insertSeckill(Seckill seckill) {
        return seckillDao.insertSeckill(seckill);
    }

    public List<Seckill> listAllSeckill() {
        List<Seckill> seckills = seckillDao.listAllSeckill();
        logger.error("seckills = {}", seckills);
        seckills = seckillDao.listAllSeckill();
        logger.error("seckills = {}", seckills);
        return seckills;
    }

    @Transactional
    public Integer updateSeckillByNumber(Integer number) {
        Integer res = seckillDao.updateSeckillByNumber(number);
        logger.error("res = {}", res);
        return res;
    }
}
