package com.example.project09.payload.post.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class PostRequest {

    @NotBlank
    @Size(max = 40)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String content;
    private Integer price;

    @NotBlank
    private String transactionRegion;

    @NotBlank
    private String openChatLink;

    @NotNull
    private MultipartFile image;
}
