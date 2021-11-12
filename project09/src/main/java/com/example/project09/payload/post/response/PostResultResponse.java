package com.example.project09.payload.post.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostResultResponse {
    private final long count;
    private final Integer memberId;
    private final List<PostResponse> posts;
}
