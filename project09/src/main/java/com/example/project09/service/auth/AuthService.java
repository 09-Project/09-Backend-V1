package com.example.project09.service.auth;

import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;

public interface AuthService {
    void signup(SignupRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse reissue(String token);
}
