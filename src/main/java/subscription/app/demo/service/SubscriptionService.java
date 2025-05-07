package subscription.app.demo.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscription.app.demo.dto.subscription.SubscriptionDto;
import subscription.app.demo.dto.subscription.SubscriptionResponseDto;
import subscription.app.demo.dto.subscription.TopSubscriptionDto;
import subscription.app.demo.entity.Subscription;
import subscription.app.demo.entity.User;
import subscription.app.demo.exception.subscription.SubscriptionNameAlreadyExistsException;
import subscription.app.demo.exception.subscription.SubscriptionNotFoundException;
import subscription.app.demo.repository.SubscriptionRepository;
import subscription.app.demo.repository.projection.SubscriptionNameCount;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    public SubscriptionResponseDto addSubscription(Long userId, SubscriptionDto dto) {
        logger.info("Добавление подписки для userId: {}, имя подписки: {}", userId, dto.name());
        User user = userService.getUserById(userId);

        boolean exists = subscriptionRepository.existsByUserIdAndNameIgnoreCase(userId, dto.name().trim());
        if (exists) {
            logger.error("Подписка с именем {} уже существует для пользователя {}", dto.name(), userId);
            throw new SubscriptionNameAlreadyExistsException(dto.name());
        }

        Subscription subscription = Subscription.builder()
                .name(dto.name().trim())
                .user(user)
                .build();

        Subscription saved = subscriptionRepository.save(subscription);
        logger.info("Подписка успешно добавлена с id: {}", saved.getId());
        return new SubscriptionResponseDto(saved.getId(), saved.getName());
    }

    public List<SubscriptionResponseDto> getSubscriptionsByUser(Long userId) {
        logger.info("Получение подписок для userId: {}", userId);
        List<SubscriptionResponseDto> subscriptions = subscriptionRepository.findByUserId(userId).stream()
                .map(s -> new SubscriptionResponseDto(s.getId(), s.getName()))
                .toList();
        logger.info("Найдено {} подписок для userId: {}", subscriptions.size(), userId);
        return subscriptions;
    }

    public void removeSubscription(Long userId, Long subscriptionId) {
        logger.info("Удаление подписки с id: {} для userId: {}", subscriptionId, userId);
        Subscription subscription = subscriptionRepository.findByIdAndUserId(subscriptionId, userId)
                .orElseThrow(() -> {
                    logger.error("Подписка с id: {} не найдена для userId: {}", subscriptionId, userId);
                    return new SubscriptionNotFoundException(subscriptionId);
                });
        subscriptionRepository.delete(subscription);
        logger.info("Подписка с id: {} успешно удалена", subscriptionId);
    }

    public SubscriptionResponseDto getSubscriptionById(Long userId, Long subscriptionId) {
        logger.info("Получение подписки с id: {} для userId: {}", subscriptionId, userId);
        Subscription subscription = subscriptionRepository.findByIdAndUserId(subscriptionId, userId)
                .orElseThrow(() -> {
                    logger.error("Подписка с id: {} не найдена для userId: {}", subscriptionId, userId);
                    return new SubscriptionNotFoundException(subscriptionId);
                });
        return new SubscriptionResponseDto(subscription.getId(), subscription.getName());
    }

    public SubscriptionResponseDto updateSubscription(Long userId, Long subscriptionId, SubscriptionDto dto) {
        logger.info("Обновление подписки с id: {} для userId: {}", subscriptionId, userId);
        Subscription subscription = subscriptionRepository.findByIdAndUserId(subscriptionId, userId)
                .orElseThrow(() -> {
                    logger.error("Подписка с id: {} не найдена для userId: {}", subscriptionId, userId);
                    return new SubscriptionNotFoundException(subscriptionId);
                });

        String newName = dto.name().trim();
        boolean nameExists = subscriptionRepository.existsByUserIdAndNameIgnoreCase(userId, newName)
                && !subscription.getName().equalsIgnoreCase(newName);

        if (nameExists) {
            logger.error("Подписка с именем {} уже существует для пользователя {}", newName, userId);
            throw new SubscriptionNameAlreadyExistsException(newName);
        }

        subscription.setName(newName);
        Subscription updated = subscriptionRepository.save(subscription);
        logger.info("Подписка с id: {} успешно обновлена", updated.getId());
        return new SubscriptionResponseDto(updated.getId(), updated.getName());
    }

    public List<TopSubscriptionDto> getTopSubscriptions(int limit) {
        logger.info("Получение топ {} подписок", limit);
        List<SubscriptionNameCount> topSubscriptions = subscriptionRepository.findTopSubscriptionNames(PageRequest.of(0, limit));
        List<TopSubscriptionDto> result = topSubscriptions.stream()
                .map(s -> new TopSubscriptionDto(s.getName(), s.getCount()))
                .toList();
        logger.info("Найдено {} топовых подписок", result.size());
        return result;
    }
}