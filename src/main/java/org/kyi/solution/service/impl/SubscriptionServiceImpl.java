package org.kyi.solution.service.impl;

import lombok.AllArgsConstructor;
import org.kyi.solution.dto.SubscriptionDTO;
import org.kyi.solution.model.Payment;
import org.kyi.solution.model.Subscription;
import org.kyi.solution.model.User;
import org.kyi.solution.repository.PaymentRepository;
import org.kyi.solution.repository.SubscriptionRepository;
import org.kyi.solution.repository.UserRepository;
import org.kyi.solution.service.SubscriptionService;
import org.kyi.solution.utility.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User subscribe(SubscriptionDTO subscriptionDTO) {
        User user = userRepository.findById(subscriptionDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("Failed to find user by id " + subscriptionDTO.getUserId()));
        Subscription subscription = subscriptionRepository.findById(subscriptionDTO.getSubscriptionId()).orElseThrow(() -> new IllegalArgumentException("Failed to find subscription by id " + subscriptionDTO.getSubscriptionId()));

        LocalDate currentDate = LocalDate.now();

        Period period = Period.ofMonths(subscription.getMonth());
        LocalDate expireDate = currentDate.plus(period);

        user.setEffectiveDate(DateUtils.toUtilDate(currentDate));
        user.setExpireDate(DateUtils.toUtilDate(expireDate));
        user.setSubscription(subscription);
        user.setSubscriptionActive(true);

        Payment payment = new Payment();
        payment.setPaymentType(subscriptionDTO.getPaymentType());
        payment.setPrice(subscription.getPrice());
        payment.setPaymentDate(new Date());
        payment.setSubscription(subscription);
        payment.setUser(user);
        paymentRepository.save(payment);

        userRepository.save(user);
        return user;
    }

    @Override
    public Subscription findById(long id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find subscription by id " + id));
    }

    @Override
    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public User validate(User user) {
        if (user.getExpireDate() != null && user.getExpireDate().after(new Date())) {
            user.setSubscriptionActive(false);
            return userRepository.save(user);
        }
        return user;
    }
}
