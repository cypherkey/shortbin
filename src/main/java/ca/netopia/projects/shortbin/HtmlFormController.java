package ca.netopia.projects.shortbin;

import ca.netopia.projects.shortbin.item.Item;
import ca.netopia.projects.shortbin.item.exception.ItemErrorException;
import ca.netopia.projects.shortbin.item.exception.ItemNotFoundException;
import ca.netopia.projects.shortbin.item.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

@Controller
public class HtmlFormController {

    private static final Logger logger = LoggerFactory.getLogger(HtmlFormController.class);

    @Autowired
    private AppConfiguration config;

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public String root(Model model) {
        return "root";
    }

    @PostMapping("/item/create")
    public String create(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "thetext", defaultValue = "") String text,
            @RequestParam(value = "expiration", defaultValue = "1") Integer expirationDays,
            Model model) {
        Item item = new Item();

        // Validate and define a default expiration days
        if (expirationDays < 0 || expirationDays > 30) {
            expirationDays = 1;
        }
        Calendar exp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        exp.add(Calendar.DAY_OF_MONTH, expirationDays);
        item.setExpiration(exp);

        try {
            if (text != null && ! text.isEmpty()) {
                //TODO - should be done by ItemService
                item.setType(MediaType.TEXT_PLAIN_VALUE);
                item.setIstext(true);
                item.setData(text);
            } else if (! file.isEmpty()) {
                item.setFileName(file.getOriginalFilename());
                item.setType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                item.setIstext(false);
                item.setData(file.getBytes());
            } else {
                model.addAttribute("warnmsg", "Nothing to save");
                return "root";
            }
            itemService.add(item);

        } catch (ItemErrorException ex) {
            logger.error(String.format("Error creating entry"), ex);
            model.addAttribute("errormsg", "ERROR!");
            return "root";
        } catch (IOException ex) {
            logger.error(String.format("Error reading uploaded file"), ex);
            model.addAttribute("errormsg", "ERROR!");
            return "root";
        }
        return String.format("redirect:/item/%s", item.getId());
    }

    @GetMapping("/item/{id}")
    public String view(
            @PathVariable("id") String id,
            Model model) {

        try {
            Item item = itemService.get(id);
            model.addAttribute("link", String.format("%s%s%s", config.getBaseUrl(), "/item/", item.getId()));
            model.addAttribute("download", String.format("%s%s%s%s", config.getBaseUrl(), "/item/", item.getId(), "/download"));
            model.addAttribute("expiration", item.getExpirationAsString());

            if (item.getIstext()) {
                String text = new String(item.getData());
                model.addAttribute("thetext", text);
                model.addAttribute("istext", true);
            } else {
                model.addAttribute("istext", false);
            }
        } catch (ItemErrorException ex) {
            logger.error(String.format("Error retrieving file for item %s", id), ex);
            model.addAttribute("errormsg", "ERROR!");
            return "view";
        } catch (ItemNotFoundException ex) {
            logger.error(String.format("Item %s not found", id), ex);
            model.addAttribute("errormsg", "Invalid entry");
            return "root";
        }
        return "view";
    }

    @GetMapping("/item/{id}/download")
    public ResponseEntity<?> download(
            @PathVariable("id") String id,
            Model model) {

        try {
            Item item = itemService.get(id);
            Resource fileData = new ByteArrayResource(item.getData());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(item.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", item.getFilename()))
                    .body(fileData);
        } catch (ItemErrorException ex) {
            logger.error(String.format("Error retreiving file"), ex);
            model.addAttribute("errormsg", "ERROR!");
            //return "view";
        } catch (ItemNotFoundException ex) {
            logger.error(String.format("Item %s not found", id), ex);
            model.addAttribute("errormsg", "Invalid entry");
            //return "root";
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/item/{id}/delete")
    public String delete(
            @PathVariable("id") String id,
            Model model) {

        try {
            itemService.delete(id);
        } catch (ItemErrorException ex) {
            logger.error(String.format("Error retreiving file"), ex);
            model.addAttribute("errormsg", "ERROR!");
        }

        return "root";
    }
}
