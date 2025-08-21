package com.example.file_upload_backend.controller;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/files")
public class FileController {

    @GetMapping("/generate-upload-url")
    public Map<String, String> generateUploadUrl(@RequestParam String fileName) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        String emulatorHost = System.getenv("FIREBASE_STORAGE_EMULATOR_HOST");
        if (emulatorHost != null && !emulatorHost.isBlank()) {
            // Emulator does not support signed URLs
            String url = "http://" + emulatorHost
                    + "/v0/b/" + bucket.getName()
                    + "/o?name=uploads/" + fileName;
            return Map.of("url", url, "emulator", "true");
        }

        // Production: use signed URL
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), "uploads/" + fileName)
                .setContentType("image/jpeg")
                .build();

        URL signedUrl = bucket.getStorage().signUrl(
                blobInfo,
                15,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withV4Signature()
        );

        return Map.of("url", signedUrl.toString(), "emulator", "false");
    }
}
