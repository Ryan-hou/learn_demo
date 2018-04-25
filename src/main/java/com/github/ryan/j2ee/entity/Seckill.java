package com.github.ryan.j2ee.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Ryan-hou
 * @description:
 * @className: Seckill
 * @date January 08,2017
 */
@Data
public class Seckill {

    private long seckillId;

    private String name;

    private int number;

    private Date startTime;

    private Date endTime;

    private Date createTime;

}