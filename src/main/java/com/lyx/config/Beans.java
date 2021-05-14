package com.lyx.config;

import com.lyx.config.YiqiApi;
import com.lyx.entity.LoginEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Beans
{
    @Bean("restTemplate")
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }

    @Bean("loginEntity")
    public LoginEntity getLoginEntity()
    {
        return new LoginEntity();
    }

    @Bean("yiqiApi")
    public YiqiApi getYiqiApi()
    {
        return new YiqiApi();
    }
}