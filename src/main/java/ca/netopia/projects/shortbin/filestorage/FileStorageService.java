package ca.netopia.projects.shortbin.filestorage;

import ca.netopia.projects.shortbin.AppConfiguration;
import ca.netopia.projects.shortbin.sqlite.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileStorageService {

    @Autowired
    private WordList wordList;

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
            logger.error("Invalid Storage Path");
            throw new RuntimeException("Invalid Storage Path");
        }
    }

    // collapse these two as they are basically the same
    // use .getbytes()
    public String saveText(String text) throws FileStorageException {
        String fileName = generateFileName();
        Path filePath = storagePath.resolve(fileName);

        try {
            Files.write(filePath, text.getBytes());
        } catch (IOException ex) {
            logger.error(String.format("Unable to save file %s", fileName), ex);
            throw new FileStorageException(String.format("Unable to save file %s", fileName), ex);
        }
        return fileName;
    }

    public String saveFile(MultipartFile file) throws FileStorageException {
        String fileName = generateFileName();
        Path filePath = storagePath.resolve(fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            logger.error(String.format("Unable to save file %s", fileName), ex);
            throw new FileStorageException(String.format("Unable to save file %s", fileName), ex);
        }
        return fileName;
    }

    public byte[] getFile(String fileName) throws FileStorageException {
        Path filePath = storagePath.resolve(fileName);

        try {
            return Files.readAllBytes(filePath);
        } catch (IOException ex) {
            logger.error(String.format("Unable to read file %s", fileName), ex);
            throw new FileStorageException(String.format("Unable to read file %s", fileName), ex);
        }
    }

    public void getFile(String fileName, OutputStream outputStream) throws FileStorageException {
        Path filePath = storagePath.resolve(fileName);

        try {
            Files.copy(filePath, outputStream);
        } catch (IOException ex) {
            logger.error(String.format("Unable to read file %s", fileName), ex);
            throw new FileStorageException(String.format("Unable to read file %s", fileName), ex);
        }
    }

    private String generateFileName() {
        //return UUID.randomUUID().toString();
        return String.format("%s-%s", wordList.getWord(), wordList.getWord());
    }

    public void delete(FileItem item) throws FileStorageException {
        // files are stored on the filesystem based on their ID
        delete(item.getId());
    }

    public void delete(String id) throws FileStorageException {
        Path filePath = storagePath.resolve(id);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException ex) {
            logger.error(String.format("Unable to delete file %s", id), ex);
            throw new FileStorageException(String.format("Unable to delete file %s", id), ex);
        }
    }
}