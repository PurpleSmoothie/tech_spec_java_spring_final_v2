package subscription.app.demo.exception.subscription;

public class SubscriptionNameAlreadyExistsException extends RuntimeException {
    public SubscriptionNameAlreadyExistsException(String name) {
        super("Подписка с названием '" + name + "' уже существует у пользователя");
    }
}
