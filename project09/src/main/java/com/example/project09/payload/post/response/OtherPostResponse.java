package com.example.project09.payload.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OtherPostResponse {
    private int id;
    private String title;
    private String image;
}
