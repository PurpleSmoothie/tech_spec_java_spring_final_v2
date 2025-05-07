package subscription.app.demo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO для создания или обновления пользователя.
 */
@Schema(description = "Данные пользователя")
public record UserDto(

        @Schema(description = "Имя пользователя", example = "Иван Иванов")
        @NotBlank(message = "Имя не должно быть пустым")
        String name,

        @Schema(description = "Email пользователя", example = "ivan@example.com")
        @Email(message = "Некорректный email")
        @NotBlank(message = "Email не должен быть пустым")
        String email
) {}
