package com.example.project09.payload.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberInfoResponse {
    private String name;
    private String profileUrl;
    private String introduction;
}
