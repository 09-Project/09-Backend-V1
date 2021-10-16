package com.example.project09.service.member;

import com.example.project09.payload.member.request.UpdateInformationRequest;
import com.example.project09.payload.member.request.UpdatePasswordRequest;
import com.example.project09.payload.member.response.MemberInfoResponse;
import com.example.project09.payload.member.response.MemberProfileResponse;
import com.example.project09.payload.post.response.PostResponse;
import java.util.List;

public interface MemberService {
    void updatePassword(UpdatePasswordRequest request);
    void updateInfo(UpdateInformationRequest request);
    MemberProfileResponse getMemberProfile(Integer id);
    List<PostResponse> getMemberLikePosts();
    List<PostResponse> getMemberInProgressPosts(Integer id);
    List<PostResponse> getMemberCompletedPosts(Integer id);
    MemberInfoResponse getMyInfo();
}
