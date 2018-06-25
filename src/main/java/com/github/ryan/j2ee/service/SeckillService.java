package com.github.ryan.j2ee.service;

/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillService
 * @date January 13,2017
 */
import com.github.ryan.j2ee.entity.Seckill;

import java.util.List;

/**
 * 业务接口:站在"使用者"角度设计接口
 * 三个方面:方法定义粒度,参数,返回类型(return 类型/异常)
 */
public interface SeckillService {


    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(long seckillId);


    /**
     * 执行秒杀操作
     * @param seckillId
     */
    int executeSeckill(long seckillId);

    void doSeckill(long seckillId);

    Integer insertSeckill(Seckill seckill);

    List<Seckill> listAllSeckill();

    Integer updateSeckillByNumber(Integer number);
}
