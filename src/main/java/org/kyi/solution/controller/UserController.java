package org.kyi.solution.controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.kyi.solution.configuration.UserPrincipal;
import org.kyi.solution.model.User;
import org.kyi.solution.response.HttpResponse;
import org.kyi.solution.service.SubscriptionService;
import org.kyi.solution.service.UserService;
import org.kyi.solution.utility.JWTTokenProvider;
import org.kyi.solution.constant.FileConstant;
import org.kyi.solution.constant.SecurityConstant;
import org.kyi.solution.exception.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController extends ExceptionHandling {

    public static final String EMAIL_SENT = "An Email with new password was sent to : ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully.";
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final SubscriptionService subscriptionService;


    private <T> ResponseEntity<HttpResponse<T>> createResponse(T data, HttpStatus httpStatus) {
        HttpResponse<T> body = new HttpResponse<>(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), data);
        return new ResponseEntity<>(body, httpStatus);
    }

    private <T> ResponseEntity<HttpResponse<T>> createResponse(T data, HttpStatus httpStatus, HttpHeaders headers) {
        HttpResponse<T> body = new HttpResponse<>(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), data);
        return new ResponseEntity<>(body, headers, httpStatus);
    }

    @GetMapping("/me")
    public ResponseEntity<HttpResponse<User>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> userOptional = userService.findUserByEmail(authentication.getName());
            User loginUser = userOptional.get();
            return createResponse(loginUser, OK);
        }
        return createResponse(null, UNAUTHORIZED);
    }


    @PostMapping("/login")
    public ResponseEntity<HttpResponse<User>> login(@RequestBody User user) {
        authenticate(user.getEmail(), user.getPassword());
        Optional<User> userOptional = userService.findUserByEmail(user.getEmail());
        User loginUser = userOptional.get();
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        User validated = subscriptionService.validate(loginUser);
        return createResponse(validated, OK, jwtHeader);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse<User>> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getEmail());
        return createResponse(newUser, OK);
    }

    @PostMapping("/add")
    public ResponseEntity<HttpResponse<User>> addNewUser(@RequestParam("firstName") String firstName,
                                                         @RequestParam("lastName") String lastName,
                                                         @RequestParam("email") String email,
                                                         @RequestParam("role") String role,
                                                         @RequestParam("isActive") String isActive,
                                                         @RequestParam("isNonLocked") String isNonLocked,
                                                         @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
        User newUser = userService.addNewUser(firstName, lastName, email, role, Boolean.parseBoolean(isActive), Boolean.parseBoolean(isNonLocked), profileImage);
        return createResponse(newUser, OK);
    }

    @PostMapping("/update")
    public ResponseEntity<HttpResponse<User>> updateUser(@RequestParam("currentUserName") String currentUsername,
                                                         @RequestParam("firstName") String firstName,
                                                         @RequestParam("lastName") String lastName,
                                                         @RequestParam("email") String email,
                                                         @RequestParam("role") String role,
                                                         @RequestParam("isActive") String isActive,
                                                         @RequestParam("isNonLocked") String isNonLocked,
                                                         @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
        User updateUser = userService.updateUser(currentUsername, firstName, lastName, email, role, Boolean.parseBoolean(isActive), Boolean.parseBoolean(isNonLocked), profileImage);
        return createResponse(updateUser, OK);
    }

    @GetMapping("/find/{userName}")
    public ResponseEntity<HttpResponse<User>> getUser(@PathVariable("userName") String userName) {
        User user = userService.findUserByUsername(userName);
        return createResponse(user, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse<List<User>>> getUsers() {
        List<User> users = userService.getUsers();
        return createResponse(users, OK);
    }

    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws EmailNotFoundException {
        userService.resetPassword(email);
        return response(OK, EMAIL_SENT + email);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return response(NO_CONTENT, USER_DELETED_SUCCESSFULLY);
    }

    @GetMapping(value = "/image/{userName}/{fileName}", produces = {IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE})
    public byte[] getProfileImage(@PathVariable("userName") String userName, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(FileConstant.USER_FOLDER + userName + FileConstant.FORWARD_SLASH + fileName));
    }

    @GetMapping(value = "/image/profile/{userName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getTempProfileImage(@PathVariable("userName") String userName) throws IOException {
        URL url = new URL(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + userName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int byteRead;
            byte[] chunk = new byte[1024];
            while ((byteRead = inputStream.read(chunk)) > 0) {
                baos.write(chunk, 0, byteRead);
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }


    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());
        return new ResponseEntity<>(body, httpStatus);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<HttpResponse<User>> updateProfileImage(
            @RequestParam("userName") String userName,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
        User user = userService.updateProfileImage(userName, profileImage);
        return createResponse(user, OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }


}
