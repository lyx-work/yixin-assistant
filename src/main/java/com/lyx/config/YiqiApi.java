package com.lyx.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.lyx.common.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class YiqiApi
{

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

//    /**
//     * 使用post调用一汽接口，并携带json数据
//     * @return 响应报文中的body
//     */
//    public CommonResult<JsonNode> post(String json, String url)
//    {
//        HttpHeaders mHeaders = new HttpHeaders();
//        mHeaders.setContentType(MediaType.APPLICATION_JSON);
//        mHeaders.add("Authorization", " Bearer "+loginEntity.getToken());
//        HttpEntity<String> entity = new HttpEntity<>(json, mHeaders);
//        JsonNode rep = restTemplate.postForObject(url, entity, JsonNode.class);
//
//        return CommonResult.successData(rep);
//    }
//
//    public CommonResult<JsonNode> get(String url)
//    {
//        HttpHeaders mHeaders = new HttpHeaders();
//        mHeaders.setContentType(MediaType.APPLICATION_JSON);
//        mHeaders.add("Authorization", " Bearer "+loginEntity.getToken());
//        HttpEntity<String> entity = new HttpEntity<>(mHeaders);
//
//        ResponseEntity<JsonNode> rep = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
//        if (rep.getStatusCode().toString().charAt(0) != '2')
//        {
//            System.out.println(rep);
//            return CommonResult.errorMsg("GET调用一汽接口出错");
//        }
//
//        return CommonResult.successData(rep.getBody());
//    }
}