package com.example.file_upload_backend.controller;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> upload(@RequestParam("image") MultipartFile file) throws Exception {
        Bucket bucket = StorageClient.getInstance().bucket();
        String objectName = "uploads/" + file.getOriginalFilename();

        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), objectName)
                .setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                .build();

        bucket.getStorage().create(blobInfo, file.getBytes());

        return Map.of(
                "bucket", bucket.getName(),
                "object", objectName,
                "message", "Uploaded to emulator"
        );
    }

    @GetMapping("/raw")
    public byte[] getRaw(@RequestParam("object") String objectName) {
        Bucket bucket = StorageClient.getInstance().bucket();
        return bucket.get(objectName).getContent();
    }
}
