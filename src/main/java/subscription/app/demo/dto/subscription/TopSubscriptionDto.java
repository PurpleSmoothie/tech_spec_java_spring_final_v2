package subscription.app.demo.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO для получения самых популярных подписок.
 */
@Schema(description = "Информация о самых популярных подписках")
public record TopSubscriptionDto(String name, long count) {
}
