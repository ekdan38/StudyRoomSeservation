package com.example.reservation.web.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountSignupResponseDto {
    private Long id;
    private String name;
    private String loginId;
    private String email;
}
