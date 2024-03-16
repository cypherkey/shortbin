package ca.netopia.projects.shortbin.filestorage;

import ca.netopia.projects.shortbin.AppConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
public class FileStorageService {
    private Path storagePath;

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Autowired
    public FileStorageService(AppConfiguration config) {
        if (config == null) {
            logger.error("Failed to load configuration");
            throw new RuntimeException("Failed to load configuration");
        }

        String path = config.getStorageDir();
        if (path == null) {
            logger.error("Storage path not defined");
            throw new RuntimeException("Storage path not defined");
        }

        storagePath = Paths.get(path);
        if (! Files.isDirectory(storagePath)) {
            logger.error(String.format("Invalid Storage Path [%s]. Current working directory [%s]", storagePath, System.getProperty("user.dir")));
            throw new RuntimeException(String.format("Invalid Storage Path [%s]. Current working directory [%s]", storagePath, System.getProperty("user.dir")));
        }
        logger.info(String.format("Storage path is [%s]. Current working directory [%s]", storagePath.toString(), System.getProperty("user.dir")));
    }

    public void save(String id, byte[] data) throws FileStorageException {
        Path filePath = storagePath.resolve(id);
        try {
            Files.write(filePath, data, StandardOpenOption.CREATE_NEW);
        } catch (IOException ex) {
            logger.error(String.format("Unable to save file %s", id), ex);
            throw new FileStorageException(String.format("Unable to save file %s", id), ex);
        }
    }

    public byte[] get(String fileName) throws FileStorageException {
        Path filePath = storagePath.resolve(fileName);

        try {
            return Files.readAllBytes(filePath);
        } catch (IOException ex) {
            logger.error(String.format("Unable to read file %s", fileName), ex);
            throw new FileStorageException(String.format("Unable to read file %s", fileName), ex);
        }
    }

    public void delete(String id) throws FileStorageException {
        Path filePath = storagePath.resolve(id);
        logger.debug("Deleting file with path %s", filePath);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                logger.warn(String.format("File %s does not exist", filePath));
            }
        } catch (IOException ex) {
            logger.error(String.format("Unable to delete file %s", id), ex);
            throw new FileStorageException(String.format("Unable to delete file %s", id), ex);
        }
    }
}