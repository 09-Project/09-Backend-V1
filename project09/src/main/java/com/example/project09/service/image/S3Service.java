package com.example.project09.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    private final AmazonS3Client amazonS3Client;

    public String upload(MultipartFile image, String dirName) throws IOException {
        String fileName = dirName + "/" + UUID.randomUUID() + image.getOriginalFilename();

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, image.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileName;
    }

    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public void removeFile(String imagePath) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, imagePath));
    }

}
