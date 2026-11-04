package com.example.ecommerce.user;

import com.example.ecommerce.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ){
        UserResponse response = userService.register(request);

        return ApiResponse.success(
                201,
                "User added successfully",
                response
        );
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(
            org.springframework.security.core.Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();



        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );

        return ApiResponse.success(
                200,
                "Get me successfully",
                response
        );
    }
}
