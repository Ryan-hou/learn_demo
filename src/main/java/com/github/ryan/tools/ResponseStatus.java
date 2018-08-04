package com.github.ryan.tools;

/**
 * @author ryan.houyl@gmail.com
 * 网络请求状态码枚举
 * 内容来自从status.json
 * @className ResponseStatus
 * @date August 04,2018
 */
public enum ResponseStatus {
    // 如何快速把 status.json 转为枚举类？
    // control + command + G: Select All occurrences(进入列操作模式)

    // 1xxx informational
    CONTINUE(100),
    PROCESSING(102),

    // 2xx Success
    OK(200),
    CREATED(201),

    // 3xx Redirection
    FOUND(302),

    // 4xx Client Error
    UNAUTHORIZED(401);

    private final int code;


    ResponseStatus(int code) {
        this.code = code;
    }
}
