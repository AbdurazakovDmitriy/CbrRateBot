package com.currencyrate.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class BotApplication {

    public static void main(String[] args) {
//        System.getProperties().put("proxySet", "true");
//
//        System.getProperties().put("socksProxyHost", "127.0.0.1");
//
//        System.getProperties().put("socksProxyPort", "9150");
        SpringApplication.run(BotApplication.class, args);
    }

}
