package com.devpulse.backend.infrastructure.adapter.in.web;

import com.devpulse.backend.infrastructure.adapter.in.web.dto.AuthRequest;
import com.devpulse.backend.infrastructure.adapter.in.web.dto.AuthResponse;
import com.devpulse.backend.infrastructure.adapter.out.persistence.UserEntity;
import com.devpulse.backend.infrastructure.adapter.out.persistence.UserJpaRepository;
import com.devpulse.backend.infrastructure.config.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserJpaRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return response;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getUsername());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return response;
    }
}
