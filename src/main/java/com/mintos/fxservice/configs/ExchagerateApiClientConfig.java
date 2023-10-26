package com.mintos.fxservice.configs;

import com.mintos.fxservice.api_clients.ExchagerateApiClientInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class ExchagerateApiClientConfig {

    @Bean
    public RequestInterceptor ExchagerateApiClientInterceptor(){
        return new ExchagerateApiClientInterceptor();
    }
}
