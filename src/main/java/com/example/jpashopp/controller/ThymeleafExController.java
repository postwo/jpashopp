package com.example.jpashopp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping( "thymeleaf")
@Controller
public class ThymeleafExController {

    @GetMapping( "ex")
    public String thymeleafExample() {
        return "thymeleafEx";
    }

}

