package com.example.usedmarket.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String base() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "base";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }
}
