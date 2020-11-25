package com.epam.esm.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com"})
@SpringBootApplication
public class GiftCertificateModule3Application {
    public static void main(String[] args) {
        SpringApplication.run(GiftCertificateModule3Application.class, args);
    }
}
