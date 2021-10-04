package com.example.project09.payload.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UpdateInformationRequest {

    @NotBlank
    @Size(max = 10)
    @Schema(description = "닉네임", maxLength = 10)
    private String name;

    @Size(max = 200)
    private String introduction;

    @NotNull
    private MultipartFile profile;
}
