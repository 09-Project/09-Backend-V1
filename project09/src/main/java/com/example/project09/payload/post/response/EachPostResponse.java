package com.example.project09.payload.post.response;

import com.example.project09.entity.member.Member;
import com.example.project09.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EachPostResponse {
    private Post post;
    private Member member;
}
