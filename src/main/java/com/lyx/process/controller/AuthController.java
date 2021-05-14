package com.lyx.process.controller;

import com.lyx.common.CommonResult;
import com.lyx.process.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController
{
	@Autowired
	@Qualifier("myServiceImpl")
	private AuthService authService;

    /**
     * 进入首页会调用这个接口
     * 设置 code
     */
    @GetMapping("/get-pic")
	public CommonResult getPic()
    {
        return authService.getPic();
    }

    /**
     * 点击认证调用这个接口
     * 设置 kaptcha、token
     * 执行完这个接口就算认证成功了
     */
    @PostMapping("/set-token")
    public CommonResult setToken(String kaptcha, String userName)
    {
        return authService.setToken(kaptcha, userName);
    }
}