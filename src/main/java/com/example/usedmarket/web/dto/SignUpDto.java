package com.example.usedmarket.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    private String name;
    private String email;
    private String password;

}
