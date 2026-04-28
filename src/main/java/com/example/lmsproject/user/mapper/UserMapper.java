package com.example.lmsproject.user.mapper;

import com.example.lmsproject.user.dto.request.CreateUserRequest;
import com.example.lmsproject.user.dto.response.UserResponse;
import com.example.lmsproject.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(CreateUserRequest request){
        User user=new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        return user;
    }
    public UserResponse ToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getStudentIdNumber(),
                user.getTeacherIdNumber()
        );
    }
}
