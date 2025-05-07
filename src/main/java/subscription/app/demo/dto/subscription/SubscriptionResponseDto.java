package subscription.app.demo.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO для отображения подписки.
 */
@Schema(description = "Информация о подписке")
public record SubscriptionResponseDto(

        @Schema(description = "ID подписки", example = "1")
        Long id,

        @Schema(description = "Название подписки", example = "YouTube Premium")
        String name
) {}
