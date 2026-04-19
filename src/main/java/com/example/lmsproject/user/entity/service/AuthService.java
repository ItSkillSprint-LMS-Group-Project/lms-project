package com.example.lmsproject.user.entity.service;

import com.example.lmsproject.exception.AlreadyExistsException;
import com.example.lmsproject.exception.BadRequestException;
import com.example.lmsproject.exception.ForbiddenOperationException;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.entity.UserRole;
import com.example.lmsproject.user.entity.dto.AuthResponse;
import com.example.lmsproject.user.entity.dto.LoginRequest;
import com.example.lmsproject.user.entity.dto.RegisterRequest;
import com.example.lmsproject.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        if (request.getRole() == UserRole.ADMIN) {
            throw new ForbiddenOperationException("Admin registration is not allowed");
        }

        if (request.getRole() == UserRole.STUDENT) {
            if (request.getStudentIdNumber() == null || request.getStudentIdNumber().isBlank()) {
                throw new BadRequestException("Student ID number is required for student registration");
            }

            if (userRepository.existsByStudentIdNumber(request.getStudentIdNumber())) {
                throw new AlreadyExistsException("Student ID number already exists");
            }

            if (request.getTeacherIdNumber() != null && !request.getTeacherIdNumber().isBlank()) {
                throw new BadRequestException("Teacher ID number must be empty for student registration");
            }
        }

        if (request.getRole() == UserRole.TEACHER) {
            if (request.getTeacherIdNumber() == null || request.getTeacherIdNumber().isBlank()) {
                throw new BadRequestException("Teacher ID number is required for teacher registration");
            }

            if (userRepository.existsByTeacherIdNumber(request.getTeacherIdNumber())) {
                throw new AlreadyExistsException("Teacher ID number already exists");
            }

            if (request.getStudentIdNumber() != null && !request.getStudentIdNumber().isBlank()) {
                throw new BadRequestException("Student ID number must be empty for teacher registration");
            }
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        if (request.getRole() == UserRole.STUDENT) {
            user.setStudentIdNumber(request.getStudentIdNumber());
        }

        if (request.getRole() == UserRole.TEACHER) {
            user.setTeacherIdNumber(request.getTeacherIdNumber());
        }

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!matches) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}