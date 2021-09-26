package com.example.project09.payload.member.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class UpdateInformationRequest {

    @Size(max = 10)
    private String name;

    @Size(max = 200)
    private String introduction;
    private MultipartFile profile;
}
