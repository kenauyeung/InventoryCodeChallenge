package inventoryCodeChallenge.exception;

public class MissingRecordException extends RuntimeException {
    public MissingRecordException() {
    }

    public MissingRecordException(String message) {
        super(message);
    }
}
