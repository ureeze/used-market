package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.security.LoginUser;
import com.example.usedmarket.web.security.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "index";
    }
}