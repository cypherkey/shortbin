package com.example.shortbin.sqlite;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name="fileitem")
public class FileItem {
    @Id
    private String filename;
    private String type;
    private Boolean istext;
    private String expiration;

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

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public void setExpiration(Calendar cal) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormat.setTimeZone(cal.getTimeZone());
        expiration = dateFormat.format(cal.getTime());
    }

    public Boolean getIstext() {
        return istext;
    }

    public void setIstext(Boolean istext) {
        this.istext = istext;
    }
}
