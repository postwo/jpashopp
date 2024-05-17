package com.example.jpashopp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class JpashoppApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpashoppApplication.class, args);
    }


}
