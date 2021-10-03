package com.example.project09.payload.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Schema(description = "회원 가입")
public class SignupRequest {

    @NotBlank
    @Size(max = 10)
    @Schema(description = "닉네임", maxLength = 10)
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
