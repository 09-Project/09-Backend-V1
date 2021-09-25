package com.example.project09.payload.post.response;

import com.example.project09.entity.post.Purpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {
    private Integer id;
    private String title;
    private Integer price;
    private String transactionRegion;
    private Purpose purpose;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String image;
}
