package ca.netopia.projects.shortbin.item;

import ca.netopia.projects.shortbin.item.exception.ItemErrorException;
import ca.netopia.projects.shortbin.sqlite.FileItem;

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
    private Boolean istext = true;
    private Calendar expiration = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private byte[] data;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public Item() {
        generateId();
    }

    public Item(String id) {
        setId(id);
    }

    public Item(FileItem dbItem) {
        this.id = dbItem.getId();
        setFilename(dbItem.getFilename());
        setExpiration(dbItem.getExpiration());
        setType(dbItem.getType());
        setIstext(dbItem.getIstext());
    }

    public Item(FileItem dbItem, byte[] data) {
        this(dbItem);
        setData(data);
    }
    private void setId(String id) {
        this.id = id;
        if (this.istext == true) {
            this.filename = String.format("%s.txt", id);
        }
    }
    public String getId() {
        return id;
    }

    public String generateId() {
        setId(String.format("%s-%s", WordList.getWord(), WordList.getWord()));
        return getId();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        if (this.istext) {
            this.filename = filename;
        }
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
        return this.dateFormat.format(expiration.getTime());
    }

    public Timestamp getExpirationAsTimestamp() {
        return new Timestamp(this.expiration.getTime().getTime());
    }

    public void setExpiration(String expiration) throws ItemErrorException {
        try {
            Date date = dateFormat.parse(expiration);
            this.expiration = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            this.expiration.setTime(date);
        } catch (ParseException ex) {
            throw new ItemErrorException(String.format("Unable to parse [%s]", expiration), ex);
        }
    }
    public void setExpiration(Calendar expiration) {
        this.expiration = expiration;
    }

    public void setExpiration(Timestamp timestamp) {
        this.expiration = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.expiration.setTimeInMillis(timestamp.getTime());

    }

    public void validateExpiration() {
        Calendar min = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar max = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        max.add(Calendar.DAY_OF_MONTH, 30);

        if (this.expiration.compareTo(min) < 0) {
            this.expiration = min;
        } else if (this.expiration.compareTo(max) > 0) {
            this.expiration = max;
        }
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
