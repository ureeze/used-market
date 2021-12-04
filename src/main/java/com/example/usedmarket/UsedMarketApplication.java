package com.example.usedmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
//@EnableConfigurationProperties(AppProperties.class)
public class UsedMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsedMarketApplication.class, args);
    }

}
