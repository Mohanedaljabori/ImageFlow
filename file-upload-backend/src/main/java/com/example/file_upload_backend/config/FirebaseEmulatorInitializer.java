package com.example.file_upload_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class FirebaseEmulatorInitializer {

    private final String storageBucket;
    private final String emulatorHost;

    public FirebaseEmulatorInitializer(
            @Value("${firebase.storage-bucket:}") String storageBucket
    ) {
        this.storageBucket = storageBucket;
        this.emulatorHost = System.getenv("FIREBASE_STORAGE_EMULATOR_HOST");
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (emulatorHost == null || emulatorHost.isBlank()) {
            System.out.println("[Firebase] No emulator host configured. Skipping bucket check.");
            return;
        }

        try {
            URL url = new URL("http://" + emulatorHost + "/storage/v1/b/" + storageBucket);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("[Firebase] Bucket exists: " + storageBucket);
            } else if (responseCode == 404) {
                System.out.println("[Firebase] Bucket does not exist. Emulator will create it on first upload.");
            } else {
                System.out.println("[Firebase] Unexpected response code: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("[Firebase] Could not contact emulator. Skipping initialization.");
            e.printStackTrace();
        }
    }
}
