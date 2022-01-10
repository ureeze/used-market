package com.example.usedmarket.web.dto;

import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto implements Serializable {

    String token;
}
