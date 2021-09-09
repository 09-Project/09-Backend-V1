package com.example.project09.payload.post.response;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.post.Purpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private String title;
    private String content;
    private Integer price;
    private String transactionRegion;
    private String openChatLink;
    private Purpose purpose;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<String> images;
}