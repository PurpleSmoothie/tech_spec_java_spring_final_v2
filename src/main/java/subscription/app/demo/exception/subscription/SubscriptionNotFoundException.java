package subscription.app.demo.exception.subscription;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(Long subId) {
        super("Подписка с Id '" + subId + "' не найдена");;
    }
}
