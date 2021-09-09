package com.example.project09.service.post;

import com.example.project09.entity.member.Member;
import com.example.project09.payload.post.request.CreatePostRequest;

public interface PostService {
    void create(CreatePostRequest request, Member member);
}
