package com.example.project09.payload.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberLikePostsResponse {
    private Integer likePostsCount;
    private List<likePosts> likePosts;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class likePosts {
        private String title;
        private String image;
    }

}
