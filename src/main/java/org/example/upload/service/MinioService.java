package org.example.upload.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String defaultBucket;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * 上传文件到MinIO（使用默认桶）
     */
    public void uploadFile(String fileName, MultipartFile file) throws Exception {
        uploadFile(defaultBucket, fileName, file);
    }

    /**
     * 上传文件到MinIO（指定桶）
     */
    public void uploadFile(String bucketName, String fileName, MultipartFile file) throws Exception {
        ensureBucket(bucketName);

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        }
    }

    private void ensureBucket(String bucketName) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
}