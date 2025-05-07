package subscription.app.demo.exception.user;

/**
 * Исключение, выбрасываемое, если пользователь не найден.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Пользователь с Id " + id + " не найден");
    }
}
