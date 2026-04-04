package com.blog.application.service.impl;

import com.blog.application.service.UserService;
import com.blog.domain.common.BusinessException;
import com.blog.domain.entity.User;
import com.blog.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUserList(String keyword) {
        return userRepository.findAll(keyword);
    }

    @Override
    public void createUser(User user) {
        User existing = userRepository.findByUsername(user.getUsername());
        if (existing != null) {
            throw new BusinessException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        User existing = userRepository.findById(user.getId());
        if (existing == null) {
            throw new BusinessException("用户不存在");
        }
        // 如果修改了密码，需要加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existing.getPassword());
        }
        userRepository.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        userRepository.deleteById(id);
    }
}
