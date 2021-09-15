package com.example.project09.payload.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MemberLikePostsResponse {
    private String title;
    private List<String> images;
}
