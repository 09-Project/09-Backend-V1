package com.example.project09.payload.post.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private Integer price;
    private String transactionRegion;
    private String openChatLink;
    private List<MultipartFile> multipartFiles;
}
