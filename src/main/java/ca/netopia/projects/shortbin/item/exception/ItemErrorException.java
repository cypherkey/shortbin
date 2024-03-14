package ca.netopia.projects.shortbin.item.exception;

public class ItemErrorException extends Exception {
    public ItemErrorException() {
        super();
    }

    public ItemErrorException(String message) {
        super(message);
    }

    public ItemErrorException(String message, Throwable ex) {
        super(message, ex);
    }
}
