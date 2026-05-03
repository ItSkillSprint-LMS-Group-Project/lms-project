package com.lmsproject;

import com.example.lmsproject.exception.AlreadyExistsException;
import com.example.lmsproject.exception.BadRequestException;
import com.example.lmsproject.exception.ForbiddenOperationException;
import com.example.lmsproject.security.service.JwtService;
import com.example.lmsproject.user.dto.request.CreateUserRequest;
import com.example.lmsproject.user.dto.request.LoginRequest;
import com.example.lmsproject.user.dto.response.AuthResponse;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.entity.UserRole;
import com.example.lmsproject.user.mapper.UserMapper;
import com.example.lmsproject.user.repository.UserRepository;
import com.example.lmsproject.user.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@gmail.com");

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> authService.register(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldThrowException_WhenAdminRole() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@gmail.com");
        request.setRole(UserRole.ADMIN);

        assertThrows(ForbiddenOperationException.class,
                () -> authService.register(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldThrowException_WhenStudentMissingId() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@gmail.com");
        request.setRole(UserRole.STUDENT);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        assertThrows(BadRequestException.class,
                () -> authService.register(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldSaveStudentSuccessfully() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@gmail.com");
        request.setRole(UserRole.STUDENT);
        request.setStudentIdNumber("ST123");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setRole(UserRole.STUDENT);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);

        authService.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123");

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class,
                () -> authService.login(request));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordWrong() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login(request));
    }

    @Test
    void login_ShouldReturnToken_WhenSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setPassword("encoded");
        user.setRole(UserRole.STUDENT);

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("mock-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock-token", response.getToken());
        assertEquals(1L, response.getId());
        assertEquals(UserRole.STUDENT, response.getRole());
    }
}
