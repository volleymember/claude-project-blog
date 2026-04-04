package com.blog.application.service.impl;

import com.blog.application.service.AuthService;
import com.blog.domain.common.BusinessException;
import com.blog.domain.entity.User;
import com.blog.domain.repository.UserRepository;
import com.blog.infrastructure.security.JwtUtils;
import com.blog.interfaces.dto.request.LoginRequest;
import com.blog.interfaces.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        String roleKey = userRepository.findRoleKeyByUserId(user.getId());

        return new LoginResponse(token, user.getUsername(), user.getNickname(), roleKey);
    }

    @Override
    public void logout() {
        // JWT 无状态，服务端无需处理
    }
}
