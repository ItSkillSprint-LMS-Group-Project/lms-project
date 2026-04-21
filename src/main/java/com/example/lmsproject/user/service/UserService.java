package com.example.lmsproject.user.service;

import com.example.lmsproject.exception.AlreadyExistsException;
import com.example.lmsproject.exception.BadRequestException;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.entity.UserRole;
import com.example.lmsproject.user.dto.request.CreateUserRequest;
import com.example.lmsproject.user.dto.request.UpdateUserRequest;
import com.example.lmsproject.user.dto.response.UserResponse;
import com.example.lmsproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {
        String email = normalizeEmail(request.getEmail());

        validateCreateRequest(request, email);

        User user = new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        if (request.getRole() == UserRole.STUDENT) {
            user.setStudentIdNumber(request.getStudentIdNumber().trim());
        }

        if (request.getRole() == UserRole.TEACHER) {
            user.setTeacherIdNumber(request.getTeacherIdNumber().trim());
        }

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return mapToResponse(user);
    }

    public List<UserResponse> searchUsersByName(String name) {
        String normalizedName = name.trim();

        return userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(normalizedName, normalizedName)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<UserResponse> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = findUserById(id);

        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName().trim());
        }

        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            user.setLastName(request.getLastName().trim());
        }

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String normalizedEmail = normalizeEmail(request.getEmail());

            if (!normalizedEmail.equals(user.getEmail()) && userRepository.existsByEmail(normalizedEmail)) {
                throw new AlreadyExistsException("Email already exists");
            }

            user.setEmail(normalizedEmail);
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getRole() == UserRole.STUDENT || user.getRole() == UserRole.STUDENT) {
            if (request.getStudentIdNumber() != null && !request.getStudentIdNumber().trim().isEmpty()) {
                String studentId = request.getStudentIdNumber().trim();

                if (!studentId.equals(user.getStudentIdNumber()) &&
                        userRepository.existsByStudentIdNumber(studentId)) {
                    throw new AlreadyExistsException("Student ID number already exists");
                }

                user.setStudentIdNumber(studentId);
                user.setTeacherIdNumber(null);
            }
        }

        if (request.getRole() == UserRole.TEACHER || user.getRole() == UserRole.TEACHER) {
            if (request.getTeacherIdNumber() != null && !request.getTeacherIdNumber().trim().isEmpty()) {
                String teacherId = request.getTeacherIdNumber().trim();

                if (!teacherId.equals(user.getTeacherIdNumber()) &&
                        userRepository.existsByTeacherIdNumber(teacherId)) {
                    throw new AlreadyExistsException("Teacher ID number already exists");
                }

                user.setTeacherIdNumber(teacherId);
                user.setStudentIdNumber(null);
            }
        }

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    private void validateCreateRequest(CreateUserRequest request, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Email already exists");
        }

        if (request.getRole() == UserRole.ADMIN) {
            return;
        }

        if (request.getRole() == UserRole.STUDENT) {
            if (isBlank(request.getStudentIdNumber())) {
                throw new BadRequestException("Student ID number is required for student users");
            }

            if (userRepository.existsByStudentIdNumber(request.getStudentIdNumber().trim())) {
                throw new AlreadyExistsException("Student ID number already exists");
            }

            if (!isBlank(request.getTeacherIdNumber())) {
                throw new BadRequestException("Teacher ID number must be empty for student users");
            }
        }

        if (request.getRole() == UserRole.TEACHER) {
            if (isBlank(request.getTeacherIdNumber())) {
                throw new BadRequestException("Teacher ID number is required for teacher users");
            }

            if (userRepository.existsByTeacherIdNumber(request.getTeacherIdNumber().trim())) {
                throw new AlreadyExistsException("Teacher ID number already exists");
            }

            if (!isBlank(request.getStudentIdNumber())) {
                throw new BadRequestException("Student ID number must be empty for teacher users");
            }
        }
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserResponse mapToResponse(User user) {
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

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isBlank();
    }
}