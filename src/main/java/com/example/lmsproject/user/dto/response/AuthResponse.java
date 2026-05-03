package com.example.lmsproject.user.dto.response;

import com.example.lmsproject.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private UserRole role;
    private String token;
}