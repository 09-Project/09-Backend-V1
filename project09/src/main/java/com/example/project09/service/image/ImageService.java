package com.example.project09.service.image;

import com.example.project09.entity.post.Post;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadFile(MultipartFile image, Post post);
    String updateFile(MultipartFile image, Post post);
    void removeFile(Integer postId);
}
