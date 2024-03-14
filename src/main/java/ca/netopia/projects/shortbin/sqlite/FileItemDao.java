package ca.netopia.projects.shortbin.sqlite;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileItemDao extends CrudRepository<FileItem, String> {
    FileItem findFileItemById(String filename);

    @Query("select id from FileItem where expiration < current_timestamp")
    List<String> findExpired();
}
