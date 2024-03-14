package ca.netopia.projects.shortbin.item.exception;

public class ItemNotFoundException extends Exception {
    public ItemNotFoundException() {
        super();
    }

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
