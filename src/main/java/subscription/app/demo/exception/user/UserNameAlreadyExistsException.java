package subscription.app.demo.exception.user;

public class UserNameAlreadyExistsException extends RuntimeException {
    public UserNameAlreadyExistsException(String name) {
        super("Пользователь с именем '" + name + "' уже существует");
    }
}
