package com.example.lmsproject.enrollment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnrollByEmailRequest {
    @NotBlank
    @Email
    private String email;
}
