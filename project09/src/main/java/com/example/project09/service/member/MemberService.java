package com.example.project09.service.member;

import com.example.project09.entity.member.Member;
import com.example.project09.payload.member.request.InformationRequest;
import com.example.project09.payload.member.request.PasswordRequest;

public interface MemberService {
    void updatePassword(PasswordRequest request, Member member);
    void updateInfo(InformationRequest request, Member member);
}
