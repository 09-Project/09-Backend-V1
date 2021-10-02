package com.example.project09.payload.member.response;

import com.example.project09.payload.post.response.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileResponse {
    private String name;
    private String profileUrl;
    private String introduction;
    private List<PostResponse> posts;
    private Integer postsCount;
    private Integer getLikesCount;
    private Integer inProgressPostsCount;
    private Integer completedPostsCount;
}
