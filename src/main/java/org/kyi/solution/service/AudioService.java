package org.kyi.solution.service;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public interface AudioService {
    String getAudioUrl(String name);
    String saveAudio(MultipartFile file) throws IOException;
    void deleteAudio(String name) throws IOException;

    default String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }
    default String generateAudioFileName(String originalFileName) {
        String extension = getExtension(originalFileName);
        String uniqueName = UUID.randomUUID().toString();
        return uniqueName + "." + extension;
    }
    default byte[] getAudioBytes(File audioFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(audioFile);
        byte[] audioBytes;
        try {
            audioBytes = IOUtils.toByteArray(fileInputStream);
        } finally {
            fileInputStream.close();
        }
        return audioBytes;
    }


}
