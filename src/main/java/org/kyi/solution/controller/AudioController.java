package org.kyi.solution.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.kyi.solution.service.PodcastService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;

@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final PodcastService podcastService;

    public AudioController(PodcastService podcastService) {
        this.podcastService = podcastService;
    }

    @Value("${firebase.bucket-name}")
    private String bucketName;

    @Value("${firebase.service-account-path}")
    private String serviceAccount;

    @GetMapping("/stream")
    public ResponseEntity<InputStreamResource> streamAudio(@RequestParam("fileName") String fileName,
                                                           @RequestParam("podcastId")long podcastId,
                                                           @RequestParam("userId")long userId) throws IOException {
        Resource resource = new ClassPathResource(serviceAccount);
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                .build()
                .getService();

        Blob audioBlob = storage.get(bucketName, fileName);

        if (audioBlob !=    null && audioBlob.exists()) {
            ReadChannel audioChannel = audioBlob.reader();

            long start = 0;
            long end = audioBlob.getSize() - 1;

            long length = end - start + 1;

            InputStream audioStream = Channels.newInputStream(audioChannel);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(length);

            headers.set("Content-Range", String.format("bytes %d-%d/%d", start, end, audioBlob.getSize()));

            podcastService.viewPodcast(podcastId, userId);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(audioStream));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
