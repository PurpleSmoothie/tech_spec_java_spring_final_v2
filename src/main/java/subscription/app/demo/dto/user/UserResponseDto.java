package subscription.app.demo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import subscription.app.demo.dto.subscription.SubscriptionResponseDto;

import java.util.List;

/**
 * DTO для отображения пользователя с подписками.
 */
@Schema(description = "Информация о пользователе")
public record UserResponseDto(
        @Schema(description = "ID пользователя", example = "1")
        Long id,

        @Schema(description = "Имя пользователя", example = "Иван Иванов")
        String name,

        @Schema(description = "Email пользователя", example = "ivan@example.com")
        String email,

        @Schema(description = "Список подписок пользователя")
        List<SubscriptionResponseDto> subscriptions
) {}

