package ca.netopia.projects.shortbin;

import ca.netopia.projects.shortbin.item.Item;
import ca.netopia.projects.shortbin.item.exception.ItemErrorException;
import ca.netopia.projects.shortbin.item.exception.ItemNotFoundException;
import ca.netopia.projects.shortbin.item.ItemService;
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
    private ItemService itemService;

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
        for(String id : itemService.getExpired()) {
            try {
                Item item = itemService.get(id);
                logger.info(String.format("Deleting %s with expiration %s", item.getId(), item.getExpiration()));
                itemService.delete(id);
            } catch (ItemErrorException ex) {
                logger.error(String.format("Unable to delete item %s", id), ex);
            } catch (ItemNotFoundException ex) {
                logger.error(String.format("Item %s not found", id), ex);
            }
        }
    }
}
