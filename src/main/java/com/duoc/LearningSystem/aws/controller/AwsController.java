package com.duoc.LearningSystem.aws.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.LearningSystem.aws.service.interfaces.AwsService;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;




@RestController
@RequestMapping("/s3")
public class AwsController {

    private AwsService awsService;

    public AwsController(AwsService awsService) {
        this.awsService = awsService;
    }
    
    @GetMapping("/getS3FileContent")
    public ResponseEntity<String> getS3FileContent(@RequestParam String bucketName, @RequestParam String key) throws IOException{
        String content = awsService.getS3FileContent(bucketName, key);
        return ResponseEntity.ok(content);
    }
    
    @GetMapping("/downloadS3File")
    public ResponseEntity<byte[]> downloadS3File(@RequestParam String bucketName, @RequestParam String key) throws IOException {
        byte[] fileContent = awsService.downloadS3File(bucketName, key);
        return ResponseEntity.ok().body(fileContent);
    }

    @DeleteMapping("/deleteObject")
    public ResponseEntity<Void> deleteObject(@RequestParam String bucketName, @RequestParam String key) throws IOException {
        awsService.deleteObject(bucketName, key);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/moveObject")
    public ResponseEntity<Void> moveObject(@RequestParam String sourceBucketName, @RequestParam String sourceKey, @RequestParam String destinationKey) {
        awsService.moveObject(sourceBucketName, sourceKey, destinationKey);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/uploadObject")
    public ResponseEntity<String> uploadObject(@RequestParam String bucketName, @RequestParam String key, @RequestParam MultipartFile fileContent) throws IOException {
        String result = awsService.uploadObject(bucketName, key, fileContent);
        return ResponseEntity.ok(result);
    }
    
}
