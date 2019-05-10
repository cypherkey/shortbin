package com.example.shortbin.sqlite;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileItemDao extends CrudRepository<FileItem, String> {
    FileItem findFileItemById(String filename);

    @Query("select f from FileItem f where expiration < current_timestamp")
    List<FileItem> findExpired();
}
