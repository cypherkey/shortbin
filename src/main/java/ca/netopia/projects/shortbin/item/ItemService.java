package ca.netopia.projects.shortbin.item;

import ca.netopia.projects.shortbin.filestorage.FileStorageException;
import ca.netopia.projects.shortbin.filestorage.FileStorageService;
import ca.netopia.projects.shortbin.item.exception.ItemErrorException;
import ca.netopia.projects.shortbin.item.exception.ItemNotFoundException;
import ca.netopia.projects.shortbin.sqlite.FileItem;
import ca.netopia.projects.shortbin.sqlite.FileItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemService {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileItemService fileItemService;


    public void add(Item item) throws ItemErrorException {
        // Add the file to the file system first
        try {
            fileStorageService.save(item.getId(), item.getData());
        } catch (FileStorageException ex) {
            throw new ItemErrorException(String.format("Unable to save file"), ex);
        }

        // Add the record to the DB
        FileItem dbItem = new FileItem();
        dbItem.setId(item.getId());
        dbItem.setExpiration(item.getExpirationAsTimestamp());
        dbItem.setType(item.getType());
        dbItem.setIstext(item.getIstext());
        dbItem.setFileName(item.getFilename());
        fileItemService.add(dbItem);
    }

    public Item get(String id) throws ItemNotFoundException, ItemErrorException{
        // Get the DB record first
        FileItem dbItem = fileItemService.get(id);

        if (dbItem == null) {
            throw new ItemNotFoundException();
        }

        // Get the data from the file system
        try {
            byte[] data = fileStorageService.get(id);
            Item item = new Item(dbItem, data);
            return item;
        } catch (FileStorageException ex) {
            throw new ItemErrorException(String.format("Unable to retrieve file for item %s", id), ex);
        }
    }

    public List<Item> getAll() {
        // Only get the DB records
        List<Item> items = new ArrayList<>();
        fileItemService.getAll().forEach( x -> {
            items.add(new Item(x));
        });
        return items;
    }

    public void delete(String id) throws ItemErrorException {
        // Delete the file first
        try {
            fileStorageService.delete(id);
        } catch (FileStorageException ex) {
            throw new ItemErrorException(String.format("Unable to delete item %s", id), ex);
        }

        // Delete the DB entry
        fileItemService.delete(id);
    }

    public List<String> getExpired() {
        return fileItemService.getExpired();
    }
}