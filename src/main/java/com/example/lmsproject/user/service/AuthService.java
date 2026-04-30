package com.example.lmsproject.user.service;

import com.example.lmsproject.exception.AlreadyExistsException;
import com.example.lmsproject.exception.BadRequestException;
import com.example.lmsproject.exception.ForbiddenOperationException;
import com.example.lmsproject.security.service.JwtService;
import com.example.lmsproject.user.dto.request.CreateUserRequest;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.entity.UserRole;
import com.example.lmsproject.user.dto.response.AuthResponse;
import com.example.lmsproject.user.dto.request.LoginRequest;
import com.example.lmsproject.user.mapper.UserMapper;
import com.example.lmsproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void register(CreateUserRequest request) {
        String email = normalizeEmail(request.getEmail());

        validateEmailUniqueness(email);
        validateRole(request);
        validateRoleSpecificFields(request);

        User user = buildUser(request, email);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!matches) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(user.getId(),user.getRole(),token);
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Email already exists");
        }
    }

    private void validateRole(CreateUserRequest request) {
        if (request.getRole() == UserRole.ADMIN) {
            throw new ForbiddenOperationException("Admin registration is not allowed");
        }
    }

    private void validateRoleSpecificFields(CreateUserRequest request) {
        if (request.getRole() == UserRole.STUDENT) {
            validateStudentRegistration(request);
            return;
        }

        if (request.getRole() == UserRole.TEACHER) {
            validateTeacherRegistration(request);
            return;
        }

        throw new BadRequestException("Invalid role");
    }

    private void validateStudentRegistration(CreateUserRequest request) {
        if (isBlank(request.getStudentIdNumber())) {
            throw new BadRequestException("Student ID number is required for student registration");
        }

        if (userRepository.existsByStudentIdNumber(request.getStudentIdNumber().trim())) {
            throw new AlreadyExistsException("Student ID number already exists");
        }

        if (!isBlank(request.getTeacherIdNumber())) {
            throw new BadRequestException("Teacher ID number must be empty for student registration");
        }
    }

    private void validateTeacherRegistration(CreateUserRequest request) {
        if (isBlank(request.getTeacherIdNumber())) {
            throw new BadRequestException("Teacher ID number is required for teacher registration");
        }

        if (userRepository.existsByTeacherIdNumber(request.getTeacherIdNumber().trim())) {
            throw new AlreadyExistsException("Teacher ID number already exists");
        }

        if (!isBlank(request.getStudentIdNumber())) {
            throw new BadRequestException("Student ID number must be empty for teacher registration");
        }
    }

    private User buildUser(CreateUserRequest request, String email) {
        User user =userMapper.toEntity(request);
        if (request.getRole() == UserRole.STUDENT) {
            user.setStudentIdNumber(request.getStudentIdNumber().trim());
        }

        if (request.getRole() == UserRole.TEACHER) {
            user.setTeacherIdNumber(request.getTeacherIdNumber().trim());
        }

        return user;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isBlank();
    }
}