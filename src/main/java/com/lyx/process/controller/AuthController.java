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
}