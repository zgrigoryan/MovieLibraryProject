package exception;

public class NoSuchListException extends RuntimeException {
    public NoSuchListException(String message) {
        super(message);
    }
}
