package subscription.app.demo.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность подписки пользователя.
 */
@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название подписки (например, Netflix, Spotify).
     */
    @Column(nullable = false)
    private String name;

    /**
     * Пользователь, владеющий подпиской.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
