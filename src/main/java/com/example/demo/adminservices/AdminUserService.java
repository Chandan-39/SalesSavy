package com.example.demo.adminservices;

import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.repository.JWTTokenRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {

    private final UserRepository userRepo;
    private final JWTTokenRepository jwtRepo;

    public AdminUserService(UserRepository userRepo, JWTTokenRepository jwtRepo) {
        this.userRepo = userRepo;
        this.jwtRepo = jwtRepo;
    }

    @Transactional
    public User modifyUser(Integer userId, String username, String email, String role) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        if (role != null && !role.isBlank()) {
            try {
                user.setRole(Role.valueOf(role.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
        }

        // Invalidate all JWT tokens for the user after modification
        jwtRepo.deleteByUserId(userId);

        return userRepo.save(user);
    }

    public User getUserById(Integer userId) {
        return userRepo.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
