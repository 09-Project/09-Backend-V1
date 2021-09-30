package com.example.project09.service.image;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import com.example.project09.entity.post.Post;
import com.example.project09.exception.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public String uploadFile(MultipartFile image, Post post) throws IOException {
        String imagePath = s3Service.upload(image, "static");

        imageRepository.save(Image.builder()
                .imagePath(imagePath)
                .imageUrl(s3Service.getFileUrl(imagePath))
                .post(post)
                .build());
        return imagePath;
    }

    @Override
    @Transactional
    public String updateFile(MultipartFile image, Post post) throws IOException {
        String imagePath = s3Service.upload(image, "static");
        Integer postId = post.getId();

        removeFile(postId);
        imageRepository.findByPostId(postId)
                .map(newImage -> newImage.updateImage(imagePath, s3Service.getFileUrl(imagePath)))
                .orElseThrow(ImageNotFoundException::new);

        return imagePath;
    }

    @Override
    public void removeFile(Integer postId) {
        s3Service.removeFile(imageRepository.findByPostId(postId)
                .map(image -> image.getImagePath())
                .orElseThrow(ImageNotFoundException::new));
    }

}
