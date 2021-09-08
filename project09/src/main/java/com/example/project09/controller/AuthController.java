package com.example.project09.controller;

import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;
import com.example.project09.service.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody SignupRequest request) {
        authService.signup(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PutMapping("/reissue")
    public TokenResponse reissue(@RequestHeader(name = "x-refresh-token") String token) {
        return authService.reissue(token);
    }

}
