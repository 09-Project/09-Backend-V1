package com.example.project09.service.member;

import com.example.project09.entity.member.Member;
import com.example.project09.payload.member.request.InformationRequest;
import com.example.project09.payload.member.request.PasswordRequest;
import com.example.project09.payload.post.response.PostResponse;
import java.util.List;

public interface MemberService {
    void updatePassword(PasswordRequest request, Member member);
    void updateInfo(InformationRequest request, Member member);
    List<PostResponse> getMemberPosts(Member member);
}
