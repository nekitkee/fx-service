package com.mintos.fxservice.api_clients;

import com.mintos.fxservice.configs.ExchagerateApiClientConfig;
import com.mintos.fxservice.dtos.exchangerate.ExchangerateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ExchagerateApiClient", url = "${exchange-rate.api.url}",  configuration = {ExchagerateApiClientConfig.class})
public interface ExchagerateApiClient {
    @GetMapping("live")
    ExchangerateResponse getExchangeRates(@RequestParam("source") String sourceCcy);
}
