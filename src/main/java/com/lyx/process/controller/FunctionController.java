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

    /**
     * 核后委托清收 导出文件
     * hw 代表 核后委托清收
     */
    @GetMapping("/hw-today-data")
    public ResponseEntity hwTodayData(String day)
    {
        return functionService.hwTodayData(day, false);
    }

    /**
     * 委托清收 导出文件
     * wq 代表 委托清收
     */
    @GetMapping("/wq-today-data")
    public ResponseEntity wqTodayData(String day)
    {
        return functionService.hwTodayData(day, true);
    }

    @GetMapping("/accNum")
    public CommonResult assNum()
    {
        return CommonResult.successData("功能接口调用次数：" + Constant.assNum);
    }
}