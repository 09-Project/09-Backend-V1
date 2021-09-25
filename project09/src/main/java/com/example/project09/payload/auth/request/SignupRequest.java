package com.example.project09.payload.auth.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(max = 10)
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
