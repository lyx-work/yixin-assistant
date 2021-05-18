package com.lyx;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootTest
class AppTests
{
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper oMapper;

    /**
     * 正常的toekn
     * 返回200报文，返回数据
     */
	@Test
	public void test1() throws IOException
    {
        HttpHeaders mHeaders = new HttpHeaders();
        mHeaders.setContentType(MediaType.APPLICATION_JSON);
        mHeaders.add("Cookie", "token=ZGM5Y2I4OWQtOTE0Mi00NGZhLWI3M2ItNmNhMGQ4ZTgyNzVj");

        HttpEntity<String> entity = new HttpEntity<>(oMapper.readTree(FileUtil.file("/Users/lyx/my-dir/download/参数.json")).toString(), mHeaders);

        ResponseEntity<JsonNode> rep = restTemplate.postForEntity("https://ares.yxqiche.com/ares-web/visit/outsource/inside/pageQuery", entity, JsonNode.class);

        System.out.println(rep);
    }

    /**
     * 错误的token
     * 返回302报文，没有数据
     */
    @Test
    public void test2() throws IOException
    {
        HttpHeaders mHeaders = new HttpHeaders();
        mHeaders.setContentType(MediaType.APPLICATION_JSON);
        mHeaders.add("Cookie", "token=ZGM5YNGZhLWI3M2ItNmNhMGQ4ZTgyNzVj");

        HttpEntity<String> entity = new HttpEntity<>(oMapper.readTree(FileUtil.file("/Users/lyx/my-dir/download/参数.json")).toString(), mHeaders);

        ResponseEntity<JsonNode> rep = restTemplate.postForEntity("https://ares.yxqiche.com/ares-web/visit/outsource/inside/pageQuery", entity, JsonNode.class);

        System.out.println(rep);
    }

    /**
     * 没有token
     * 返回302报文，没有数据
     */
    @Test
    public void test3() throws IOException
    {
        HttpHeaders mHeaders = new HttpHeaders();
        mHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(oMapper.readTree(FileUtil.file("/Users/lyx/my-dir/download/参数.json")).toString(), mHeaders);

        ResponseEntity<JsonNode> rep = restTemplate.postForEntity("https://ares.yxqiche.com/ares-web/visit/outsource/inside/pageQuery", entity, JsonNode.class);

        System.out.println(rep);
    }
}