package com.example.project09.service.image;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ImageService {
    String imageUpload(MultipartFile image) throws IOException;
}
