package com.example.project09.payload.post.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class PostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
    private Integer price;

    @NotBlank
    private String transactionRegion;

    @NotBlank
    private String openChatLink;
    private MultipartFile image;
}
