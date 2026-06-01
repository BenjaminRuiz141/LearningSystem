package com.duoc.LearningSystem.aws.service;

import com.duoc.LearningSystem.aws.service.interfaces.AwsService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.duoc.LearningSystem.aws.model.Asset;
import com.duoc.LearningSystem.aws.repository.interfaces.S3Repository;

@Service
public class AwsServiceImpl implements AwsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsServiceImpl.class);

    private S3Repository s3Repository;

    public AwsServiceImpl(S3Repository s3Repository) {
        this.s3Repository = s3Repository;
    }

    @Override
    public String getS3FileContent(String bucketName, String key) throws IOException {
        return new String(s3Repository.downloadObject(bucketName, key), StandardCharsets.UTF_8);
    }

/*     private static String getAsString(InputStream is) throws IOException {
        if (is == null) 
            return "";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StringUtils.UTF8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            is.close();
        }
        return sb.toString();
    } */

    @Override
    public List<Asset> getS3Files(String bucketName) throws IOException {
        return s3Repository.listObjectsInBucket(bucketName);
    }

    @Override
    public byte[] downloadS3File(String bucketName, String key) throws IOException {
        return s3Repository.downloadObject(bucketName, key);
    }

    @Override
    public void moveObject(String sourceBucketName, String sourceKey, String destinationKey) {
        s3Repository.moveObject(sourceBucketName, sourceKey, destinationKey);
    }

    @Override
    public void deleteObject(String bucketName, String key) {
        s3Repository.deleteObject(bucketName, key);
    }

    @Override
    public String uploadObject(String bucketName, String key, MultipartFile file) throws IOException {
        return s3Repository.uploadObject(bucketName, key, convertMultipartFileToFile(file));
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            LOGGER.error("Error converting MultipartFile to File: {}", e.getMessage());
            throw e;
        }
        return convertedFile;
    }

}
