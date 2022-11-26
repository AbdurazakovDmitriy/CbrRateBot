package com.currencyrate.bot.feign;

import com.currencyrate.bot.feign.config.FeignClientConfig;
import com.currencyrate.bot.feign.dto.CurrencyRate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(url = "${urls.cbrServicePath}", name = "CbrServiceFeign", configuration = FeignClientConfig.class)
public interface CbrServiceFeign {

    @GetMapping("/currencies")
    Flux<CurrencyRate> getRates(
            @RequestParam("date") String date
    );

    @GetMapping(value = "/currency/{currency}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    Mono<CurrencyRate> getRate(
            @PathVariable(value = "currency", required = false) String currency,
            @RequestParam("date") String date
    );

}
