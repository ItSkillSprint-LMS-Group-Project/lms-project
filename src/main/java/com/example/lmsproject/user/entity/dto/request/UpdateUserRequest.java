package com.example.lmsproject.user.entity.dto.request;

import com.example.lmsproject.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;

    @Email
    @Size(max = 100)
    private String email;

    private UserRole role;

    @Size(max = 30)
    private String studentIdNumber;

    @Size(max = 30)
    private String teacherIdNumber;
}