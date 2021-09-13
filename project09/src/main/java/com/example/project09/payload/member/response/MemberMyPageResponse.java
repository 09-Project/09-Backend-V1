package com.example.project09.payload.member.response;

import com.example.project09.entity.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class MemberMyPageResponse {
    private MemberProfileResponse memberProfileResponse;
    private List<Set<Image>> likePosts;
}
