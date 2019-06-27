package ca.netopia.projects.shortbin.filestorage;

import net.bytebuddy.implementation.bytecode.Throw;

public class FileStorageException extends Exception {
    public FileStorageException() {
        super();
    }

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable ex) {
        super(message, ex);
    }
}
