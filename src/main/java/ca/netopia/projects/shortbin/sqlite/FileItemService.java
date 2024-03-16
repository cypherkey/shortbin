package ca.netopia.projects.shortbin.sqlite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileItemService {
    @Autowired
    private FileItemDao fileItemDao;

    public void add(FileItem id) {
        fileItemDao.save(id);
    }

    public FileItem get(String id) {
        return fileItemDao.findFileItemById(id);
    }

    public List<FileItem> getAll() {
        List<FileItem> items = new ArrayList<FileItem>();
        fileItemDao.findAll().forEach(items::add);
        return items;
    }

    public void delete(String id) {
        fileItemDao.deleteById(id);
    }

    public void delete(FileItem fileItem) {
        delete(fileItem.getId());
    }

    public List<String> getExpired() { return fileItemDao.findExpired(); }
}