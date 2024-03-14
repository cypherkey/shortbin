package ca.netopia.projects.shortbin.item;

import ca.netopia.projects.shortbin.sqlite.FileItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Item {
    private String id;
    private String filename;
    private String type;
    private Boolean istext;
    private Calendar expiration;
    private byte[] data;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public Item() {
        this.id = String.format("%s-%s", WordList.getWord(), WordList.getWord());
        this.filename = String.format("%s.txt", id);
    }

    public Item(String id) {
        this.id = id;
        this.filename = String.format("%s.txt", id);
    }

    public Item(FileItem dbItem, byte[] data) {
        this.id = dbItem.getId();
        setFileName(dbItem.getFilename());
        setExpiration(dbItem.getExpiration());
        setType(dbItem.getType());
        setIstext(dbItem.getIstext());
        setData(data);
    }

    public String getId() {
        return id;
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

    public Calendar getExpiration() {
        return expiration;
    }

    public String getExpirationAsString() {
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return this.dateFormat.format(expiration.getTime());
    }

    public Timestamp getExpirationAsTimestamp() {
        return new Timestamp(this.expiration.getTime().getTime());
    }

    public void setExpiration(Calendar expiration) {
        this.expiration = expiration;
    }

    public void setExpiration(Timestamp timestamp) {
        this.expiration = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.expiration.setTimeInMillis(timestamp.getTime());

    }
    public Boolean getIstext() {
        return istext;
    }

    public void setIstext(Boolean istext) {
        this.istext = istext;
    }

    public void setData(String text) {
        setData(text.getBytes());
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return this.data;
    };

}
