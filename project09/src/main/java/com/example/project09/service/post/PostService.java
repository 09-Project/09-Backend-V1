package com.example.project09.service.post;

import com.example.project09.entity.member.Member;
import com.example.project09.payload.post.request.PostRequest;

public interface PostService {
    void createPost(PostRequest request, Member member);
    void modifyPost(PostRequest request, Integer id);
}
