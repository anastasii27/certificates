package com.epam.esm.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com"})
@SpringBootApplication
public class GiftCertificateApplication {
    public static void main(String[] args) {
        SpringApplication.run(GiftCertificateApplication.class, args);
    }
}
