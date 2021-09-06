package com.example.project09.service.auth;

import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.AccessTokenResponse;

public interface AuthService {
    String signup(SignupRequest request);
    AccessTokenResponse login(LoginRequest request);
}
