package com.lyx.process.service;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.lyx.common.CommonResult;
import com.lyx.entity.LoginEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service("myServiceImpl")
public class AuthServiceImpl implements AuthService
{
    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("loginEntity")
    private LoginEntity loginEntity;

    @Override
    public CommonResult getPic()
    {
        // 获得code
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonNode jsonNode = restTemplate.getForObject("https://unisrvs.17ebank.com:9117/mid-user-core/auth/dspcode", JsonNode.class);
        String code = jsonNode.at("/data").asText(StrUtil.EMPTY);
        if (StrUtil.isBlank(code))
            return CommonResult.errorMsg("没有获得code");
        loginEntity.setCode(code);

        // 获得图片验证码
        HttpHeaders mHeaders = new HttpHeaders();
        mHeaders.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String,String> jsonData = new LinkedMultiValueMap<>();
        jsonData.add("imitate", "1");
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(jsonData, mHeaders);
        JsonNode picBase64 = restTemplate.postForObject("https://unisrvs.17ebank.com:9117/mid-user-core/kaptcha/image?code={code}", entity, JsonNode.class, code);
        String base64Suffix = picBase64.at("/data/img").asText(StrUtil.EMPTY);
        if (StrUtil.isBlank(base64Suffix))
            return CommonResult.errorMsg("没有获得验证码的base64编码");

        return CommonResult.successData("data:image/png;base64," + base64Suffix);
    }

    @Override
    public CommonResult setToken(String kaptcha, String loginName)
    {
        loginEntity.setKaptcha(kaptcha);
        loginEntity.setLoginName(loginName);

        // 调用登录接口获得token
        HttpHeaders mHeaders = new HttpHeaders();
        mHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
        formData.add("loginName", loginEntity.getLoginName());
        formData.add("password", "TZV3bjaoT4b0DmtHxUtrBg==");
        formData.add("kaptcha", loginEntity.getKaptcha());
        formData.add("code", loginEntity.getCode());
        HttpEntity httpEntity = new HttpEntity(formData, mHeaders);
        JsonNode rep = restTemplate.postForObject("https://unisrvs.17ebank.com:9117/mid-user-core/auth/dsplogin", httpEntity, JsonNode.class);

        // 处理返回的数据
        if (StrUtil.equals(rep.get("code").asText(StrUtil.EMPTY), "500"))
            return CommonResult.errorMsg(rep.get("data").asText());
        loginEntity.setToken(rep.at("/data/tokenMap/system77/accessToken").asText());
        return CommonResult.successMsg("认证成功");
    }
}