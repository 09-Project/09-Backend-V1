package com.example.project09.payload.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileResponse {
    private Integer memberId;
    private String name;
    private String profileUrl;
    private String introduction;
    private Integer allPostsCount;
    private Integer getLikesCount;
    private Integer inProgressPostsCount;
    private Integer completedPostsCount;
    private Integer likePostsCount;
}
