package com.ceremonie.demo.services.interfaces;

import java.util.List;

import com.ceremonie.demo.dto.request.ChangePasswordRequest;
import com.ceremonie.demo.dto.request.CreateUserRequest;
import com.ceremonie.demo.dto.request.UpdateUserRequest;
import com.ceremonie.demo.dto.response.UserResponse;
import com.ceremonie.demo.enums.Role;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    void changePassword(String username, ChangePasswordRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
    List<UserResponse> getAllUsers();
    List<UserResponse> getActiveUsers();
    List<UserResponse> getUsersByRole(Role role);
    void deleteUser(Long id);
    void activateUser(Long id);
    void deactivateUser(Long id);
}
