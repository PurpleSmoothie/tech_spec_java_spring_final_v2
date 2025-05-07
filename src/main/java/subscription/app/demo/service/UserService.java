package subscription.app.demo.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscription.app.demo.dto.subscription.SubscriptionResponseDto;
import subscription.app.demo.dto.user.UserDto;
import subscription.app.demo.dto.user.UserResponseDto;
import subscription.app.demo.entity.User;
import subscription.app.demo.exception.user.EmailAlreadyExistsException;
import subscription.app.demo.exception.user.UserNameAlreadyExistsException;
import subscription.app.demo.repository.UserRepository;
import java.util.List;
import subscription.app.demo.exception.user.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public User createUser(UserDto dto) {
        logger.info("Создание пользователя с именем: {} и email: {}", dto.name(), dto.email());
        UniqEmailAndNameOrThrow(dto.email(), dto.name());

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .subscriptions(List.of())
                .build();

        User savedUser = userRepository.save(user);
        logger.info("Пользователь успешно создан с id: {}", savedUser.getId());
        return savedUser;
    }

    public User getUserById(Long id) {
        logger.info("Получение пользователя с id: {}", id);
        return getUserOrThrow(id);
    }

    public User updateUser(Long id, UserDto dto) {
        logger.info("Обновление пользователя с id: {}", id);
        User user = getUserOrThrow(id);

        if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            logger.error("Email {} уже существует", dto.email());
            throw new EmailAlreadyExistsException(dto.email());
        }

        if (!user.getName().equals(dto.name()) && userRepository.existsByName(dto.name())) {
            logger.error("Имя пользователя {} уже существует", dto.name());
            throw new UserNameAlreadyExistsException(dto.name());
        }

        user.setName(dto.name());
        user.setEmail(dto.email());

        User updatedUser = userRepository.save(user);
        logger.info("Пользователь с id: {} успешно обновлен", updatedUser.getId());
        return updatedUser;
    }

    public void deleteUser(Long id) {
        logger.info("Удаление пользователя с id: {}", id);
        getUserOrThrow(id);
        userRepository.deleteById(id);
        logger.info("Пользователь с id: {} успешно удален", id);
    }

    public UserResponseDto toResponseDto(User user) {
        logger.info("Преобразование пользователя в DTO для userId: {}", user.getId());
        List<SubscriptionResponseDto> subscriptions = user.getSubscriptions().stream()
                .map(sub -> new SubscriptionResponseDto(sub.getId(), sub.getName()))
                .toList();

        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), subscriptions);
    }

    public User getUserOrThrow(Long id) {
        logger.info("Получение пользователя с id: {}", id);
        return userRepository.findById(id).orElseThrow(() -> {
            logger.error("Пользователь с id: {} не найден", id);
            return new UserNotFoundException(id);
        });
    }

    public void UniqEmailAndNameOrThrow(String email, String name) {
        logger.info("Проверка уникальности email: {} и имени: {}", email, name);
        if (userRepository.existsByEmail(email)) {
            logger.error("Email {} уже существует", email);
            throw new EmailAlreadyExistsException(email);
        }

        if (userRepository.existsByName(name)) {
            logger.error("Имя пользователя {} уже существует", name);
            throw new UserNameAlreadyExistsException(name);
        }
    }
}