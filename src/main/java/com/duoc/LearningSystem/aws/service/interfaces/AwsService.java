package com.duoc.LearningSystem.aws.service.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.duoc.LearningSystem.aws.model.Asset;

public interface AwsService {

    String getS3FileContent(String bucketName, String key) throws IOException;

    List<Asset> getS3Files(String bucketName) throws IOException;

    byte[] downloadS3File(String bucketName, String key) throws IOException;

    void moveObject(String sourceBucketName, String sourceKey, String destinationKey);

    void deleteObject(String bucketName, String key);

    String uploadObject(String bucketName, String key, MultipartFile file) throws IOException;

}
