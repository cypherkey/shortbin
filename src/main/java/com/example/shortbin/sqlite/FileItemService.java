package com.example.shortbin.sqlite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileItemService {
    @Autowired
    private FileItemDao fileItemDao;

    public void add(FileItem fileItem) {
        fileItemDao.save(fileItem);
    }

    public FileItem get(String fileName) {
        return fileItemDao.findFileItemByFilename(fileName);
    }

    public void delete(String fileName) {
        fileItemDao.deleteById(fileName);
    }

    public void delete(FileItem fileItem) {
        delete(fileItem.getFilename());
    }

    public List<FileItem> getExpired() { return fileItemDao.findExpired(); }
}