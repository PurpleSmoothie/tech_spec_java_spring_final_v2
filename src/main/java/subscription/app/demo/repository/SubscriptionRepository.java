package subscription.app.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import subscription.app.demo.entity.Subscription;
import subscription.app.demo.repository.projection.SubscriptionNameCount;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с подписками.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserId(Long userId);

    Optional<Subscription> findByIdAndUserId(Long subscriptionId, Long userId);

    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

    @Query("SELECT s.name AS name, COUNT(s.id) AS count " +
            "FROM Subscription s GROUP BY s.name ORDER BY COUNT(s.id) DESC")
    List<SubscriptionNameCount> findTopSubscriptionNames(Pageable pageable);
}
