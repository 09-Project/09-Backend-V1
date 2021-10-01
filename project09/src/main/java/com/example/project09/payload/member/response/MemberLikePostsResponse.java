package com.example.project09.payload.member.response;

import com.example.project09.payload.post.response.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberLikePostsResponse {
    private Integer likePostsCount;
    private List<PostResponse> likePosts;
}
