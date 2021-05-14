package com.lyx.process.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lyx.common.CommonResult;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface FunctionService
{
    /**
     * 导出核后委托清收的数据到excel文件
     * 这些数据的[委托开始日期]等于某一天
     * @param isWq ture-委托清收导出 false-核后委托清收导出
     */
    ResponseEntity hwTodayData(String day, boolean isWq);
}