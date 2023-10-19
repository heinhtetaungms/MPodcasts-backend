package org.kyi.solution.service;

import org.kyi.solution.dto.SubscriptionDTO;
import org.kyi.solution.model.Subscription;
import org.kyi.solution.model.User;

import java.util.List;

public interface SubscriptionService {
    User subscribe(SubscriptionDTO subscriptionDTO);
    Subscription findById(long id);
    List<Subscription> findAll();
    User validate(User user);
}
