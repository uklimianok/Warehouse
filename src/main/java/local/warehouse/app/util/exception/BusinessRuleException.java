package local.warehouse.app.util.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(
        String message
    ) {
        super(message);
    }
}
