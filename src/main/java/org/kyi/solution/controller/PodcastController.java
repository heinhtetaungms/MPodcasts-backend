package org.kyi.solution.controller;

import lombok.AllArgsConstructor;
import org.kyi.solution.dto.PodcastDTO;
import org.kyi.solution.dto.PodcastInteractionDTO;
import org.kyi.solution.dto.UserDTO;
import org.kyi.solution.factory.UserFactory;
import org.kyi.solution.model.Podcast;
import org.kyi.solution.model.User;
import org.kyi.solution.model.Writer;
import org.kyi.solution.response.HttpResponse;
import org.kyi.solution.factory.PodcastFactory;
import org.kyi.solution.service.*;
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
@RequestMapping("/podcast")
public class PodcastController {
    public static final String POST_DELETED_SUCCESSFULLY = "Podcast deleted successfully.";
    private final PodcastService podcastService;
    private final UserService userService;
    private final WriterService writerService;
    private final AudioService audioService;
    private final ImageStorageService imageStorageService;

    private <T> ResponseEntity<HttpResponse<T>> createResponse(T data, HttpStatus httpStatus) {
        HttpResponse<T> body = new HttpResponse<>(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), data);
        return new ResponseEntity<>(body, httpStatus);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());
        return new ResponseEntity<>(body, httpStatus);
    }

    @PostMapping("/add")
    public ResponseEntity<HttpResponse<Podcast>> register(@RequestParam("file") MultipartFile file,
                                                          @RequestParam("image") MultipartFile image,
                                                          @ModelAttribute PodcastDTO podcastDTO) throws IOException {
        // Process the audio file upload and the image file upload separately
        String fileName = audioService.saveAudio(file);
        String fileUrl = audioService.getAudioUrl(fileName);

        // Save the image file to Firebase Storage
        String imageFileUrl = imageStorageService.saveImageToFirebaseStorage(image);

        User user = userService.findById(podcastDTO.getUserId());
        Writer writer = writerService.findById(podcastDTO.getWriterId());

        Podcast podcast = PodcastFactory.convertToEntity(podcastDTO, writer, user);
        podcast.setFileUrl(fileUrl);
        podcast.setImageUrl(imageFileUrl);

        Podcast newPodcast = podcastService.save(podcast);

        return createResponse(newPodcast, OK);
    }

    @GetMapping("/favourite")
    public ResponseEntity<HttpResponse<List<Podcast>>> favourite(@RequestParam("userId") long userId) {

        List<Podcast> podcasts = podcastService.favouritePodcasts(userId);
        return createResponse(podcasts, OK);
    }

    @GetMapping("/playList")
    public ResponseEntity<HttpResponse<List<Podcast>>> playList(@RequestParam("userId") long userId) {

        List<Podcast> podcasts = podcastService.podcastPlayListByUser(userId);
        return createResponse(PodcastFactory.ago(podcasts), OK);
    }

    @GetMapping("/likeCount")
    public ResponseEntity<HttpResponse<List<Podcast>>> findPodcastsOrderByLikeCountDesc() {

        List<Podcast> podcasts = podcastService.findPodcastsOrderByLikeCountDesc();
        return createResponse(PodcastFactory.ago(podcasts), OK);
    }

    @GetMapping("/viewCount")
    public ResponseEntity<HttpResponse<List<Podcast>>> findPodcastsOrderByViewCountDesc() {

        List<Podcast> podcasts = podcastService.findPodcastsOrderByViewCountDesc();
        return createResponse(PodcastFactory.ago(podcasts), OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<HttpResponse<List<PodcastDTO>>> getLatestPodCasts() {

        List<Podcast> podcasts = podcastService.findAllPodcastsOrderedByCreatedAtDesc();
        List<PodcastDTO> results = PodcastFactory.convertListToDTOList(podcasts);
        return createResponse(PodcastFactory.concatAgo(results), OK);
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse<List<Podcast>>> getAllPosts() {

        List<Podcast> podcasts = podcastService.findAll();
        return createResponse(PodcastFactory.ago(podcasts), OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse<UserDTO>> myChannel(@PathVariable("id") long userId) {

        List<Podcast> podcasts = podcastService.findPodcastsByUserId(userId);
        User user = userService.findById(userId);
        UserDTO userDTO = UserFactory.convertToDTO(user, podcasts);
        return createResponse(userDTO, OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('creator:delete')")
    public ResponseEntity<HttpResponse> deletePost(@PathVariable("id") long id) {

        podcastService.delete(id);
        return response(NO_CONTENT, POST_DELETED_SUCCESSFULLY);
    }

    @PostMapping("/like")
    public ResponseEntity<HttpResponse<Podcast>> like(@RequestBody PodcastInteractionDTO podcastDTO) {

        Podcast podcast = podcastService.likePodcast(podcastDTO.getPodcastId(), podcastDTO.getUserId(), podcastDTO.isLiked());
        return createResponse(podcast, OK);
    }
}
