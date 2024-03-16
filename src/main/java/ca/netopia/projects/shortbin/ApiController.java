package ca.netopia.projects.shortbin;

import ca.netopia.projects.shortbin.item.Item;
import ca.netopia.projects.shortbin.item.ItemService;
import ca.netopia.projects.shortbin.item.exception.ItemErrorException;
import ca.netopia.projects.shortbin.item.exception.ItemNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private AppConfiguration config;

    @Autowired
    private ItemService itemService;

    @GetMapping("/item")
    public ResponseEntity<List<Item>> listItems() {
        return new ResponseEntity<>(itemService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/item")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        // Generate an ID
        item.generateId();
        item.validateExpiration();

        if (item.getIstext()) {
            item.setType(MediaType.TEXT_PLAIN_VALUE);
        } else {
            item.setType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        }

        try {
            itemService.add(item);
        } catch (ItemErrorException ex) {
            logger.error(String.format("Error creating entry"), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<Item> getItem(@PathVariable("id") String id) {
        try {
            Item item = itemService.get(id);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } catch (ItemErrorException ex) {
            logger.error(String.format("Error retrieving file for item %s", id), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException ex) {
            logger.error(String.format("Item %s not found", id), ex);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") String id) {
        try {
            Item item = itemService.get(id);
            itemService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ItemErrorException ex) {
            logger.error(String.format("Error retrieving file for item %s", id), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ItemNotFoundException ex) {
            logger.error(String.format("Item %s not found", id), ex);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
