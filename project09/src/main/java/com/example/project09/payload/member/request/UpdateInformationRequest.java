package com.example.project09.payload.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UpdateInformationRequest {

    @Size(max = 10)
    @Schema(description = "닉네임", maxLength = 10)
    private String name;

    @Size(max = 200)
    private String introduction;
    private MultipartFile profile;
}
