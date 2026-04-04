package com.blog.application.service;

import com.blog.interfaces.dto.request.LoginRequest;
import com.blog.interfaces.dto.response.LoginResponse;

/**
 * 认证服务接口
 */
public interface AuthService {

    LoginResponse login(LoginRequest request);

    void logout();
}
