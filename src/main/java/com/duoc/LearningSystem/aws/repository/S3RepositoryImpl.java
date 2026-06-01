package com.duoc.LearningSystem.aws.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.duoc.LearningSystem.aws.model.Asset;
import com.duoc.LearningSystem.aws.repository.interfaces.S3Repository;

import java.util.stream.Collectors;

@Repository
public class S3RepositoryImpl implements S3Repository {

    private AmazonS3 amazonS3;

    public S3RepositoryImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(S3RepositoryImpl.class);

    @Override
    public List<Asset> listObjectsInBucket(String bucketName) {
        List<Asset> items =
                amazonS3.listObjects(bucketName).getObjectSummaries().stream()
                        .parallel()
                        .map(S3ObjectSummary::getKey)
                        .map(key -> mapS3ToObject(bucketName, key))
                        .collect(Collectors.toList());
                    
        LOGGER.info("Objects in bucket {}: {}", bucketName, items);

        return items;
    }

    public Asset mapS3ToObject(String bucketName, String key) {

        return Asset.builder()
                .name(amazonS3.getObjectMetadata(bucketName, key).getUserMetadata().get("name"))
                .key(key)
                .url(amazonS3.getUrl(bucketName, key))
                .build();
    }

    @Override
    public S3ObjectInputStream getObject(String bucketName, String key) {
        if (!amazonS3.doesObjectExist(bucketName, key)) {
            LOGGER.warn("Object with key {} does not exist in bucket {}", key, bucketName);
            return null;
        }
        S3Object s3object = amazonS3.getObject(bucketName, key);
        return s3object.getObjectContent();
    }

    @Override
    public byte[] downloadObject(String bucketName, String key) throws IOException {
        S3Object s3object = amazonS3.getObject(bucketName, key);
        try (S3ObjectInputStream inputStream = s3object.getObjectContent()) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            LOGGER.error("Error downloading object with key {} from bucket {}: {}", key, bucketName, e.getMessage());
            throw e;
        }
    }

    @Override
    public void moveObject(String sourceBucketName, String sourceKey, String destinationKey) {
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(sourceBucketName, sourceKey, sourceBucketName, destinationKey);
        amazonS3.copyObject(copyObjRequest);
        deleteObject(sourceBucketName, sourceKey);
    }

    @Override
    public void deleteObject(String bucketName, String key) {
        amazonS3.deleteObject(bucketName, key);
    }

    @Override
    public String uploadObject(String bucketName, String key, File file) throws IOException {
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
        file.delete();
        LOGGER.info("File {} uploaded to bucket {} with key {}", file.getName(), bucketName, key);
        return key;
    }

    
}
