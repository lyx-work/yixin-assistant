package com.lyx.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.lyx.common.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 易鑫平台调用工具
 */
@Component("invokeYixinConfig")
public class InvokeYixinConfig
{
    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    private String host = "https://ares.yxqiche.com/ares-web";

    /**
     * 使用post调用易鑫接口，并携带json数据
     * @return 易鑫返回的body转换成的
     */
    public CommonResult<JsonNode> post(String url, String tokenStr, String json)
    {
        HttpHeaders mHeaders = new HttpHeaders();
        mHeaders.setContentType(MediaType.APPLICATION_JSON);
        mHeaders.add("Cookie", tokenStr);
        HttpEntity<String> entity = new HttpEntity<>(json, mHeaders);
        JsonNode rep;
        try
        {
            rep = restTemplate.postForObject(url, entity, JsonNode.class);
        }
        catch (Exception e)
        {
            return CommonResult.successMsg("出现异常：" + e.getMessage());
        }

        if (!rep.get("success").asBoolean())
        {
            return CommonResult.errorMsg(rep.get("message").asText());
        }

        return CommonResult.successData(rep.get("data"));
    }
}