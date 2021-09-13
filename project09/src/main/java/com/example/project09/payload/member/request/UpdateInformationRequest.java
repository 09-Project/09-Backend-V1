package com.example.project09.payload.member.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UpdateInformationRequest {
    private String name;
    private String introduction;
    private MultipartFile profile;
}
