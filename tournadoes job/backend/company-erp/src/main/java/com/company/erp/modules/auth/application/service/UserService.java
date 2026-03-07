package com.company.erp.modules.auth.application.service;

import com.company.erp.modules.auth.application.dto.response.UserResponse;
import com.company.erp.modules.auth.application.mapper.UserMapper;
import com.company.erp.modules.auth.domain.model.User;
import com.company.erp.modules.auth.domain.repository.UserRepository;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PreAuthorize("hasPermission(null, 'user:read')")
    public UserResponse findById(UUID id) {
        return userMapper.toResponse(findOrThrow(id));
    }

    @PreAuthorize("hasPermission(null, 'user:read')")
    public UserResponse findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.USER_NOT_FOUND, username));
    }

    @PreAuthorize("hasPermission(null, 'user:read')")
    public PageResponse<UserResponse> findAll(Pageable pageable) {
        // Use a simple projection via the JPA repository
        Page<User> page = userRepository.findAll(pageable);
        return PageResponse.from(page.map(userMapper::toResponse));
    }

    private User findOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, id));
    }
}
