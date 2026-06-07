package com.ubs.ledger;

import org.springframework.boot
    .SpringApplication;
import org.springframework.boot.autoconfigure
    .SpringBootApplication;

@SpringBootApplication
public class TradeLedgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            TradeLedgerApplication.class,
            args
        );
    }
}
