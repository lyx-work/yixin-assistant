package com.lyx.process.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lyx.common.CommonResult;
import com.lyx.config.InvokeYixinConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("functionServiceImpl")
public class FunctionServiceImpl implements FunctionService
{
    @Autowired
    @Qualifier("invokeYixinConfig")
    private InvokeYixinConfig invokeYixin;

    @Override
    public ResponseEntity export(String tokenStr)
    {
        return null;
    }

    /**
     * 获得所有的需要导出的主单的id
     */
    public List<String> listNeedExportOrderMaster(String tokenStr)
    {
        String jsonData = "{\"applyNo\":\"\",\"customerName\":\"\",\"pickUpCarCompany\":\"\",\"pickUpCarManager\":\"\",\"applyBeginTime\":\"\",\"applyUser\":\"\",\"status\":\"wait_distribute\",\"index\":1,\"pageSize\":10}";
        CommonResult<JsonNode> yixinBody = invokeYixin.post("/visit/outsource/inside/pageQuery", tokenStr, jsonData);
        System.out.println(yixinBody);

        return null;
    }
}