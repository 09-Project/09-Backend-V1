package com.example.project09.service.image;

import com.example.project09.entity.post.Post;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ImageService {
    String uploadFile(MultipartFile image, Post post) throws IOException;
    void removeFile(Integer postId);
}
