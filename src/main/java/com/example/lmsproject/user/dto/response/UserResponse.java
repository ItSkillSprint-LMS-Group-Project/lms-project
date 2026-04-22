package com.example.lmsproject.user.dto.response;

import com.example.lmsproject.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private String studentIdNumber;
    private String teacherIdNumber;
}
