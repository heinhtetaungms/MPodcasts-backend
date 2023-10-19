package org.kyi.solution.model;

import jakarta.persistence.*;
import lombok.*;
import org.kyi.solution.enumeration.SubscriptionTier;

@Entity
@Table(name = "SUBSCRIPTION")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private SubscriptionTier subscriptionTier;
    private int month;
    private double price;
}
