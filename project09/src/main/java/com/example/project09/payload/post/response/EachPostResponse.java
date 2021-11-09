package com.example.project09.payload.post.response;

import com.example.project09.entity.post.Completed;
import com.example.project09.entity.post.Purpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class EachPostResponse {
    private String title;
    private String content;
    private Integer price;
    private String transactionRegion;
    private String openChatLink;
    private Purpose purpose;
    private Completed completed;
    private boolean isLiked;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String image;
    private MemberInfo member;
    private Integer getLikes;
    private Integer postsCount;
    private Integer everyLikeCounts;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberInfo {
        private Integer memberId;
        private String memberName;
        private String memberIntroduction;
        private String memberProfile;
    }

}
