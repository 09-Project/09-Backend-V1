package com.example.project09.payload.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberLikePostsResponse {
    private String title;
    private String images;
}
