package com.lyx.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.lyx.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
     * @return 易鑫返回的body转换成的body，生成的body中的data是易鑫返回的body中的data
     */
    public CommonResult<JsonNode> post(String url, String tokenStr, String json)
    {
        // ①获取响应报文
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

        // ②处理响应报文 302
        if (rep.getStatusCode().is3xxRedirection())
        {
            return CommonResult.errorMsg("输入的tokenStr错误");
        }

        // ③处理响应报文 200
        if (rep.getStatusCode().is2xxSuccessful())
        {
            JsonNode body = rep.getBody();
            if (!body.get("success").asBoolean())
            {
                return CommonResult.errorMsg(body.get("message").asText());
            }

            return CommonResult.successData(body.get("data"));
        }

        return CommonResult.errorMsg(StrUtil.format("响应报文 {} ，没有针对这种情况进行处理", rep.getStatusCodeValue()));
    }
}