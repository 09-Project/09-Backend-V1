package com.example.project09.payload.member.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberMyPageResponse {
    private MemberProfileResponse memberProfileResponse;
    private List<MemberLikePostsResponse> memberLikePostsResponse;
}
