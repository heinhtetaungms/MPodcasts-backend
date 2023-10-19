package org.kyi.solution.service;

import jakarta.mail.MessagingException;
import org.kyi.solution.exception.domain.EmailExistException;
import org.kyi.solution.exception.domain.EmailNotFoundException;
import org.kyi.solution.exception.domain.UserNotFoundException;
import org.kyi.solution.exception.domain.UsernameExistException;
import org.kyi.solution.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User register(String firstName, String lastName, String email) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;
    List<User> getUsers();
    User findById(long id);
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User addNewUser(String firstName, String lastName, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
    User updateUser(String currentUsername, String newFirstName, String newLastName, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
    void deleteUser(long id);
    void resetPassword(String email) throws EmailNotFoundException;
    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
}
