package com.example.project09.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.project09.config.S3Config;
import com.example.project09.exception.S3ConnectionFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Config s3Config;
    private final AmazonS3Client amazonS3Client;

    public String upload(MultipartFile image, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + image.getOriginalFilename();

        try {
            amazonS3Client.putObject(new PutObjectRequest(s3Config.getBucket(), fileName, image.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new S3ConnectionFailedException();
        }

        return fileName;
    }

    public ResponseEntity<byte[]> download(String storedFileName) {
        S3Object object = amazonS3Client.getObject(new GetObjectRequest(s3Config.getBucket(), storedFileName));
        S3ObjectInputStream oi = object.getObjectContent();

        try {
            byte[] bytes = IOUtils.toByteArray(oi);

            String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
        } catch (IOException e) {
            throw new S3ConnectionFailedException();
        }
    }

    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(s3Config.getBucket(), fileName).toString();
    }

    public void removeFile(String imagePath) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(s3Config.getBucket(), imagePath));
    }

}
