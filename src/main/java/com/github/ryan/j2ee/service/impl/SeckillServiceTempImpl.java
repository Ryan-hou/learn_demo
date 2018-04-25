package com.github.ryan.j2ee.service.impl;

import com.github.ryan.j2ee.dao.SeckillDao;
import com.github.ryan.j2ee.service.SeckillServiceTemp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: SeckillServiceTempImpl
 * @date April 25,2018
 */
@Service
@Slf4j
public class SeckillServiceTempImpl implements SeckillServiceTemp {

    @Autowired
    private SeckillDao seckillDao;

    @Transactional
    public void doSeckill(long seckillId) {
        log.info("seckill number = {}", seckillDao.queryById(seckillId).getNumber());
        seckillDao.reduceNumber(seckillId);
        log.info("seckill number = {}", seckillDao.queryById(seckillId).getNumber());

        int i = 1 / 0;
    }
}
