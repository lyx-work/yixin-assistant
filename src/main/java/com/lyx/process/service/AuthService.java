package com.lyx.process.service;

import com.lyx.common.CommonResult;

public interface AuthService
{
    /**
     * 设置 code
     */
    CommonResult getPic();

    /**
     * 设置 kaptcha、token
     */
    CommonResult setToken(String kaptcha, String userName);
}