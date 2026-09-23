package com.example.ecommerce.auth;

import com.example.ecommerce.common.AppException;
import com.example.ecommerce.common.JwtService;
import com.example.ecommerce.user.User;
import com.example.ecommerce.user.UserRepository;
import com.example.ecommerce.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsernameAndDeletedAtIsNull(request.username())
                .orElseThrow(() ->
                        new AppException(401, "Invalid username or password")
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new AppException(401, "Invalid username or password");
        }

        if (user.getStatus() != com.example.ecommerce.user.UserStatus.ACTIVE) {
            throw new AppException(403, "User is inactive");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(
                token,
                "Bearer"
        );
    }
}
