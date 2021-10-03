package com.example.project09.service.member;

import com.example.project09.payload.auth.request.LoginRequest;
import com.example.project09.payload.auth.request.SignupRequest;
import com.example.project09.payload.auth.response.TokenResponse;
import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberInfoResponse;
import com.example.project09.payload.member.response.MemberMyPageResponse;
import com.example.project09.payload.member.response.MemberProfileResponse;

public interface MemberService {
    void signup(SignupRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse reissue(String token);
    void updatePassword(UpdatePasswordRequest request);
    void updateInfo(UpdateInformationRequest request);
    MemberProfileResponse getMemberProfile(Integer id);
    MemberMyPageResponse getMyPage();
    MemberInfoResponse getMyInfo();
}
