package org.kyi.solution.controller;

import lombok.AllArgsConstructor;
import org.kyi.solution.model.Writer;
import org.kyi.solution.response.HttpResponse;
import org.kyi.solution.service.ImageStorageService;
import org.kyi.solution.service.WriterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/api/writer")
public class WriterController {
    private final WriterService writerService;
    private final ImageStorageService imageStorageService;
    public static final String WRITER_DELETED_SUCCESSFULLY = "Writer deleted successfully.";

    private <T> ResponseEntity<HttpResponse<T>> createResponse(T data, HttpStatus httpStatus) {
        HttpResponse<T> body = new HttpResponse<>(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), data);
        return new ResponseEntity<>(body, httpStatus);
    }
    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());
        return new ResponseEntity<>(body, httpStatus);
    }

    @PostMapping("/add")
    public ResponseEntity<HttpResponse<Writer>> register(@RequestParam("image") MultipartFile image,
                                                         @ModelAttribute Writer writer) throws IOException {
        String imageFileUrl = imageStorageService.saveImageToFirebaseStorage(image);
        writer.setImageUrl(imageFileUrl);
        Writer newWriter = writerService.save(writer);
        return createResponse(newWriter, OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<HttpResponse<Writer>> getPost(@PathVariable("id")long id) {
        Writer writer = writerService.findById(id);
        return createResponse(writer, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse<List<Writer>>> getAllWriters() {
        List<Writer> writers = writerService.findAll();
        return createResponse(writers, OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete')")
    public ResponseEntity<HttpResponse> deleteWriter(@PathVariable("id")long id) {
        writerService.delete(id);
        return response(NO_CONTENT, WRITER_DELETED_SUCCESSFULLY);
    }
}
