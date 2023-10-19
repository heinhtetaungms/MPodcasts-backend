package org.kyi.solution.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.kyi.solution.service.AudioService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FirebaseAudioService implements AudioService {

    @Value("${firebase.bucket-name}")
    private String bucketName;

    @Value("${firebase.audio-url}")
    private String audioUrl;

    @Value("${firebase.service-account-path}")
    private String serviceAccount;

    @EventListener
    public void initialize(ApplicationReadyEvent event) throws IOException {
        try {
            Resource resource = new ClassPathResource(serviceAccount);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .setStorageBucket(bucketName)
                    .build();

            FirebaseApp.initializeApp(options);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public String getAudioUrl(String name) {
        return String.format(audioUrl, name);
    }

    @Override
    public String saveAudio(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = generateAudioFileName(file.getOriginalFilename());
        bucket.create(name, file.getBytes(), file.getContentType());
        return name;
    }

    @Override
    public void deleteAudio(String name) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        if (StringUtils.isEmpty(name)) {
            throw new IOException("Invalid file name");
        }
        Blob blob = bucket.get(name);
        if (blob == null) {
            throw new IOException("File not found");
        }
        blob.delete();
    }
}
