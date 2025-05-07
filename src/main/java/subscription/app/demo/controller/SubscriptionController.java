package subscription.app.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subscription.app.demo.dto.subscription.SubscriptionDto;
import subscription.app.demo.dto.subscription.SubscriptionResponseDto;
import subscription.app.demo.dto.subscription.TopSubscriptionDto;
import subscription.app.demo.service.SubscriptionService;

import java.util.List;

/**
 * Контроллер для управления подписками пользователей.
 */
@Valid
@RestController
@RequestMapping("/users/{userId}/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Подписки", description = "Управление подписками пользователя")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @Operation(summary = "Добавить подписку пользователю")
    public ResponseEntity<SubscriptionResponseDto> addSubscription(
            @PathVariable
            @Min(value = 1, message = "ID должен быть положительным")
            Long userId,
            @RequestBody @Valid SubscriptionDto dto) {
        return ResponseEntity.ok(subscriptionService.addSubscription(userId, dto));
    }

    @GetMapping
    @Operation(summary = "Получить все подписки пользователя")
    public ResponseEntity<List<SubscriptionResponseDto>> getUserSubscriptions(
            @PathVariable
            @Min(value = 1, message = "ID должен быть положительным")
            Long userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUser(userId));
    }

    @DeleteMapping("/{subscriptionId}")
    @Operation(summary = "Удалить подписку пользователя")
    public ResponseEntity<Void> deleteSubscription(
            @Min(value = 1, message = "ID должен быть положительным")
            @PathVariable Long userId,
            @Min(value = 1, message = "ID должен быть положительным")
            @PathVariable Long subscriptionId) {
        subscriptionService.removeSubscription(userId, subscriptionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top")
    @Operation(summary = "Получить топ популярных подписок")
    public ResponseEntity<List<TopSubscriptionDto>> getTopSubscriptions(
            @RequestParam(defaultValue = "3") @Min(1) int limit) {
        return ResponseEntity.ok(subscriptionService.getTopSubscriptions(limit));
    }

    @GetMapping("/{subscriptionId}")
    @Operation(summary = "Получить подписку по ID")
    public ResponseEntity<SubscriptionResponseDto> getSubscriptionById(
            @Min(value = 1, message = "ID должен быть положительным") @PathVariable Long userId,
            @Min(value = 1, message = "ID должен быть положительным") @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(userId, subscriptionId));
    }

    @PutMapping("/{subscriptionId}")
    @Operation(summary = "Обновить подписку пользователя")
    public ResponseEntity<SubscriptionResponseDto> updateSubscription(
            @Min(value = 1, message = "ID должен быть положительным") @PathVariable Long userId,
            @Min(value = 1, message = "ID должен быть положительным") @PathVariable Long subscriptionId,
            @RequestBody @Valid SubscriptionDto dto) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(userId, subscriptionId, dto));
    }
}
