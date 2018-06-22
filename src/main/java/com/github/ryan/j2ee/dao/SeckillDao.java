package com.github.ryan.j2ee.dao;

import com.github.ryan.j2ee.entity.Seckill;
import org.apache.ibatis.annotations.Param;


/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillDao
 * @date January 08,2017
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @return 如果影响行数>=1,表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId);

    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill querySeckillById(long seckillId);

}
