package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.config.auth.LoginUser;
import com.example.usedmarket.web.config.auth.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/index")
    public String index(Model model, @LoginUser SessionMember member) {
        if (member != null) {
            model.addAttribute("memberName", member.getName());
            System.out.println("member is not null");
        }
        return "index";
    }
}