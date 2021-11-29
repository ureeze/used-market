package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.config.auth.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final HttpSession httpSession;

    @GetMapping("/home")
    public String index(Model model) {
        SessionMember member = (SessionMember) httpSession.getAttribute("LoginUser");
        if (member != null) {
            model.addAttribute("memberName", member.getName());
            System.out.println("member is not null");
        }

        return "index";
    }

}
