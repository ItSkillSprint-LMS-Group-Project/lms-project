
package com.lmsproject;

import com.example.lmsproject.exception.AlreadyExistsException;
import com.example.lmsproject.exception.BadRequestException;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.user.dto.request.CreateUserRequest;
import com.example.lmsproject.user.dto.request.UpdateUserRequest;
import com.example.lmsproject.user.dto.response.UserResponse;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.entity.UserRole;
import com.example.lmsproject.user.mapper.UserMapper;
import com.example.lmsproject.user.repository.UserRepository;
import com.example.lmsproject.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Mikayil");
        user.setLastName("Quliyev");
        user.setEmail("test@gmail.com");
        user.setRole(UserRole.STUDENT);
        user.setStudentIdNumber("ST123");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFirstName("Mikayil");
        userResponse.setLastName("Quliyev");
        userResponse.setEmail("test@gmail.com");
        userResponse.setRole(UserRole.STUDENT);
    }

    @Test
    void createUser_ShouldCreateStudentSuccessfully() {

        CreateUserRequest request = new CreateUserRequest();
        request.setFirstName("Mikayil");
        request.setLastName("Quliyev");
        request.setEmail("test@gmail.com");
        request.setPassword("12345");
        request.setRole(UserRole.STUDENT);
        request.setStudentIdNumber("ST123");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByStudentIdNumber(anyString())).thenReturn(false);

        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.ToResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("test@gmail.com", result.getEmail());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {

        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@gmail.com");
        request.setRole(UserRole.STUDENT);
        request.setStudentIdNumber("ST123");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(
                AlreadyExistsException.class,
                () -> userService.createUser(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_ShouldThrowException_WhenStudentIdMissing() {

        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@gmail.com");
        request.setRole(UserRole.STUDENT);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> userService.createUser(request)
        );
    }

    @Test
    void getUserById_ShouldReturnUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userMapper.ToResponse(user))
                .thenReturn(userResponse);

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(1L)
        );
    }

    @Test
    void getAllUsers_ShouldReturnUsers() {

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        when(userMapper.ToResponse(user))
                .thenReturn(userResponse);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
    }

    @Test
    void searchUsersByName_ShouldReturnMatchingUsers() {

        when(userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        anyString(),
                        anyString()
                ))
                .thenReturn(List.of(user));

        when(userMapper.ToResponse(user))
                .thenReturn(userResponse);

        List<UserResponse> result =
                userService.searchUsersByName("Mik");

        assertEquals(1, result.size());
    }

    @Test
    void getUsersByRole_ShouldReturnUsers() {

        when(userRepository.findByRole(UserRole.STUDENT))
                .thenReturn(List.of(user));

        when(userMapper.ToResponse(user))
                .thenReturn(userResponse);

        List<UserResponse> result =
                userService.getUsersByRole(UserRole.STUDENT);

        assertEquals(1, result.size());
    }

    @Test
    void updateUser_ShouldUpdateSuccessfully() {

        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("Updated");
        request.setLastName("User");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        when(userMapper.ToResponse(any(User.class)))
                .thenReturn(userResponse);

        UserResponse result =
                userService.updateUser(1L, request);

        assertNotNull(result);

        verify(userRepository).save(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenEmailExists() {

        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("new@gmail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThrows(
                AlreadyExistsException.class,
                () -> userService.updateUser(1L, request)
        );
    }

    @Test
    void deleteUser_ShouldDeleteSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(1L)
        );
    }
}

