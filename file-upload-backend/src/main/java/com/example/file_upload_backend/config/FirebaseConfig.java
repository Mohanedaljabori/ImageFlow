package com.example.file_upload_backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account-path:}")
    private String serviceAccountPath;

    @Value("${firebase.storage-bucket:}")
    private String storageBucket;

    @Value("${firebase.project-id:}")
    private String projectId;

    @Value("${FIREBASE_STORAGE_EMULATOR_HOST:}")
    private String emulatorHost;

    @PostConstruct
    public void init() throws IOException {
        boolean usingEmulator = emulatorHost != null && !emulatorHost.isBlank();

        if ((storageBucket == null || storageBucket.isBlank())
                && projectId != null && !projectId.isBlank()) {
            storageBucket = projectId + ".appspot.com";
        }

        if (storageBucket == null || storageBucket.isBlank()) {
            System.out.println("[Firebase] storageBucket not set. Skipping Firebase initialization.");
            return;
        }

        GoogleCredentials credentials;
        if (usingEmulator) {
            credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream("{}".getBytes())
            );
            System.out.println("[Firebase] Using Storage emulator at " + emulatorHost);
        } else if (serviceAccountPath != null && !serviceAccountPath.isBlank()) {
            try (FileInputStream in = new FileInputStream(serviceAccountPath)) {
                credentials = GoogleCredentials.fromStream(in);
            }
        } else {
            throw new IllegalStateException(
                    "[Firebase] No emulator and no service account configured. " +
                            "Set FIREBASE_STORAGE_EMULATOR_HOST or firebase.service-account-path."
            );
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .setStorageBucket(storageBucket)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("[Firebase] Initialized. Storage emulator: " + (usingEmulator ? emulatorHost : "OFF"));
        }
    }
}
