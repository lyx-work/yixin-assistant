package com.lyx.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.lyx.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 易鑫平台调用工具
 */
@Slf4j
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

        ResponseEntity<JsonNode> rep;
        try
        {
            rep = restTemplate.postForEntity(host + url, entity, JsonNode.class);
        }
        catch (Exception e)
        {
            log.error("出现异常：{}", e.getMessage());
            return CommonResult.successMsg("出现异常：" + e.getMessage());
        }
        if (!rep.getStatusCode().is2xxSuccessful())
        {
            log.error("请求易鑫接口返回不成功报文，状态码：{}", rep.getStatusCodeValue());
            return CommonResult.errorMsg(StrUtil.format("请求易鑫接口返回不成功报文，状态码：{}", rep.getStatusCodeValue()));
        }

        JsonNode body = rep.getBody();
        if (!body.get("success").asBoolean())
        {
            return CommonResult.errorMsg(body.get("message").asText());
        }

        return CommonResult.successData(body.get("data"));
    }
}