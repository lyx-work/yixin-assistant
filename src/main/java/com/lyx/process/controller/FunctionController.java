package com.lyx.process.controller;

import com.lyx.common.CommonResult;
import com.lyx.common.Constant;
import com.lyx.process.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunctionController
{
    @Autowired
    @Qualifier("functionServiceImpl")
    private FunctionService functionService;
}