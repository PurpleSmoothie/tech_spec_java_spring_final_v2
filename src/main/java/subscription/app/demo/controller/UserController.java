package subscription.app.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import subscription.app.demo.dto.user.UserDto;
import subscription.app.demo.dto.user.UserResponseDto;
import subscription.app.demo.service.UserService;

/**
 * Контроллер для управления пользователями.
 */
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создать пользователя")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.toResponseDto(userService.createUser(userDto)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    public ResponseEntity<UserResponseDto> getUser(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable
            @Min(value = 1, message = "ID должен быть положительным")
            Long id) {
        return ResponseEntity.ok(userService.toResponseDto(userService.getUserById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable
            @Min(value = 1, message = "ID должен быть положительным")
            Long id, @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.toResponseDto(userService.updateUser(id, userDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @Min(value = 1, message = "ID должен быть положительным")
            Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

