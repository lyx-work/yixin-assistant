package com.lyx.process.controller;

import com.lyx.common.CommonResult;
import com.lyx.common.ConstantAndVar;
import com.lyx.process.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunctionController
{
    @Autowired
    @Qualifier("functionServiceImpl")
    private FunctionService functionService;

    @GetMapping("/export")
    public ResponseEntity export(@RequestParam String tokenStr)
    {
        return functionService.export(tokenStr);
    }

    @GetMapping("/getCount")
    public CommonResult getCount()
    {
        return CommonResult.successData(ConstantAndVar.assCount);
    }
}
