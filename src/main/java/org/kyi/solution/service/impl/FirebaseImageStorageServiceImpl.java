package org.kyi.solution.service.impl;

import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.kyi.solution.service.ImageStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseImageStorageServiceImpl implements ImageStorageService {

    @Override
    public String saveImageToFirebaseStorage(MultipartFile imageFile) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        String imageName = generateUniqueImageName(imageFile.getOriginalFilename());

        String contentType = getImageContentType(imageFile.getOriginalFilename());

        Blob blob = bucket.create(imageName, imageFile.getBytes(), contentType);

        return blob.getMediaLink();
    }

    private String generateUniqueImageName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return uuid + fileExtension;
    }

    private String getImageContentType(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();

        if (lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerCaseFileName.endsWith(".png")) {
            return "image/png";
        } else if (lowerCaseFileName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerCaseFileName.endsWith(".bmp")) {
            return "image/bmp";
        } else if (lowerCaseFileName.endsWith(".webp")) {
            return "image/webp";
        } else if (lowerCaseFileName.endsWith(".tiff") || lowerCaseFileName.endsWith(".tif")) {
            return "image/tiff";
        } else if (lowerCaseFileName.endsWith(".svg")) {
            return "image/svg+xml";
        } else {
            // If the file extension is not recognized, you can return a default content type
            return "application/octet-stream"; // Treat it as a binary file
        }
    }

}
