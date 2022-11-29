package socialnetwork.domain.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public String toString() {
        return this.getMessage();
    }
}