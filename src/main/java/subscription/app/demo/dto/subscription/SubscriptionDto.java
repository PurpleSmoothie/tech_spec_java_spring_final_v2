package subscription.app.demo.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO для добавления подписки.
 */
@Schema(description = "Данные для создания подписки")
public record SubscriptionDto(

        @Schema(description = "Название подписки", example = "Netflix")
        @NotBlank(message = "Название подписки не должно быть пустым")
        String name
) {}
