package org.kyi.solution.service;


public interface EmailService {
    void sendNewPasswordEmail(String firstName, String password, String email);
}
