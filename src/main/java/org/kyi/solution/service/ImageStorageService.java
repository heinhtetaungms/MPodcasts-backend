package org.kyi.solution.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {
    String saveImageToFirebaseStorage(MultipartFile imageFile) throws IOException;
}
