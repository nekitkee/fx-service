package com.mintos.fxservice.api_clients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class ExchagerateApiClientInterceptor implements RequestInterceptor {
    @Value("${exchange-rate.api.access-key}")
    private String accessKey;

    @Override
    public void apply(RequestTemplate template) {
        template.query( "access_key", accessKey);
    }
}
