package org.kyi.solution.controller;

import lombok.AllArgsConstructor;
import org.kyi.solution.dto.SubscriptionDTO;
import org.kyi.solution.model.Subscription;
import org.kyi.solution.model.User;
import org.kyi.solution.response.HttpResponse;
import org.kyi.solution.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/subscription")
@AllArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    private <T> ResponseEntity<HttpResponse<T>> createResponse(T data, HttpStatus httpStatus) {
        HttpResponse<T> body = new HttpResponse<>(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), data);
        return new ResponseEntity<>(body, httpStatus);
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse<List<Subscription>>> getSubscriptions() {
        List<Subscription> users = subscriptionService.findAll();
        return createResponse(users, OK);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<HttpResponse<User>> subscribe(@RequestBody SubscriptionDTO subscriptionDTO) {

        User user = subscriptionService.subscribe(subscriptionDTO);
        return createResponse(user, OK);
    }
}
