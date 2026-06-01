package com.duoc.LearningSystem.aws.repository.interfaces;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.duoc.LearningSystem.aws.model.Asset;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface S3Repository {

    List<Asset> listObjectsInBucket (String bucketName);

    S3ObjectInputStream getObject(String bucketName, String key);

    byte[] downloadObject(String bucketName, String key) throws IOException;

    void moveObject(String sourceBucketName, String sourceKey, String destinationKey);

    void deleteObject(String bucketName, String key);

    String uploadObject(String bucketName, String key, File file) throws IOException;
}
