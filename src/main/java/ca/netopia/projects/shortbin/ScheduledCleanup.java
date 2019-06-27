package ca.netopia.projects.shortbin;

import ca.netopia.projects.shortbin.filestorage.FileStorageException;
import ca.netopia.projects.shortbin.filestorage.FileStorageService;
import ca.netopia.projects.shortbin.sqlite.FileItem;
import ca.netopia.projects.shortbin.sqlite.FileItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledCleanup {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledCleanup.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileItemService fileItemService;

    @EventListener(ApplicationReadyEvent.class)
    public void startUp() {
        cleanUp();
    }
    @Scheduled(cron = "${app.cleanup}")
    public void scheduled() {
        cleanUp();
    }

    private void cleanUp() {
        logger.info("Beginning cleanup");
        for(FileItem item : fileItemService.getExpired()) {
            try {
                logger.info(String.format("Deleting %s with expiration %s", item.getFilename(), item.getExpiration()));
                fileStorageService.delete(item);
                fileItemService.delete(item);
            } catch (FileStorageException ex) {
                logger.error(String.format("Unable to delete item %s", item.getFilename()), ex);
            }
        }
    }
}
