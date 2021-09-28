package com.example.project09.service.image;

import com.example.project09.entity.image.Image;
import com.example.project09.entity.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public String imageUpload(MultipartFile image) throws IOException {
        String imagePath = s3Service.upload(image, "static");

        imageRepository.save(Image.builder()
                .imagePath(imagePath)
                .build());
        return imagePath;
    }

}
