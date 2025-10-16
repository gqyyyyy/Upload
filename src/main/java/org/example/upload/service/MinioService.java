package org.example.upload.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {

    // MinIO服务器配置
    private final String MINIO_URL = "http://124.70.108.214:9000";
    private final String ACCESS_KEY = "admin";
    private final String SECRET_KEY = "JavaEE20251014";

    /**
     * 上传文件到MinIO
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @param file 要上传的文件
     * @throws Exception 可能抛出的异常
     */
    public void uploadFile(String bucketName, String fileName, MultipartFile file) throws Exception {
        // 创建MinIO客户端
        MinioClient minioClient = MinioClient.builder()
                .endpoint(MINIO_URL)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();

        // 检查存储桶是否存在，如果不存在则创建
        boolean isBucketExist = minioClient.bucketExists(io.minio.BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isBucketExist) {
            minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // 获取文件输入流
        try (InputStream inputStream = file.getInputStream()) {
            // 上传文件
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
}