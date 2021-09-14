package com.example.project09.payload.member.response;

import com.example.project09.entity.like.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberLikePostsResponse {
    private Like likePosts;
}
