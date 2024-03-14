package ca.netopia.projects.shortbin.sqlite;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name="fileitem")
public class FileItem {
    @Id
    private String id;
    private String filename;
    private String type;
    private Boolean istext;
    private Timestamp expiration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFileName(String fileName) {
        this.filename = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public Boolean getIstext() {
        return istext;
    }

    public void setIstext(Boolean istext) {
        this.istext = istext;
    }
}
