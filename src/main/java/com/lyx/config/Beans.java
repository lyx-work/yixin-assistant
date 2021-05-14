package com.lyx.config;

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
}