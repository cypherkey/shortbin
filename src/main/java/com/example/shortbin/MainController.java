package com.example.shortbin;

import com.example.shortbin.filestorage.FileStorageException;
import com.example.shortbin.filestorage.FileStorageService;
import com.example.shortbin.sqlite.FileItem;
import com.example.shortbin.sqlite.FileItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private AppConfiguration config;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileItemService fileItemService;

    @GetMapping("/")
    public String root(Model model) {
        return "root";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "thetext", defaultValue = "") String text,
            @RequestParam(value = "expiration", defaultValue = "1") Integer expirationDays,
            Model model) {
        FileItem fileItem = new FileItem();

        // Validate and define a default expiration days
        if (expirationDays < 0 || expirationDays > 30) {
            expirationDays = 1;
        }
        Calendar exp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        exp.add(Calendar.DAY_OF_MONTH, expirationDays);
        fileItem.setExpiration(exp);

        String fileName = "ERROR";

        try {
            if (text != null && ! text.isEmpty()) {
                fileName = fileStorageService.saveText(text);
                //model.addAttribute("infomsg", "Saved!");
                //model.addAttribute("link", fileName);
                fileItem.setFileName(fileName);
                fileItem.setType(MediaType.TEXT_PLAIN_VALUE);
                fileItem.setIstext(true);
                fileItemService.add(fileItem);
            } else if (! file.isEmpty()) {
                fileName = fileStorageService.saveFile(file);
                //model.addAttribute("infomsg", "Saved!");
                //model.addAttribute("link", fileName);
                fileItem.setFileName(fileName);
                fileItem.setType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                fileItem.setIstext(false);
                fileItemService.add(fileItem);
            } else {
                model.addAttribute("warnmsg", "Nothing to save");
                return "root";
            }

        } catch (FileStorageException ex) {
            logger.error(String.format("Error creating entry"), ex);
            model.addAttribute("errormsg", "ERROR!");
            return "root";
        }
        return String.format("redirect:/view/%s", fileName);
    }

    @GetMapping("/view/{filename}")
    public String view(
            @PathVariable("filename") String fileName,
            Model model) {

        FileItem fileItem = fileItemService.get(fileName);
        if (fileItem == null) {
            model.addAttribute("warnmsg", "Invalid entry");
            return "root";
        }

        byte[] data;
        try {
            data = fileStorageService.getFile(fileName);
        } catch (FileStorageException ex) {
            logger.error(String.format("Error retreiving file"), ex);
            model.addAttribute("errormsg", "ERROR!");
            return "view";
        }

        model.addAttribute("link", String.format("%s%s%s", config.getBaseUrl(), "/view/", fileItem.getFilename()));
        model.addAttribute("download", String.format("%s%s%s%s", config.getBaseUrl(), "/view/", fileItem.getFilename(), "/download"));
        model.addAttribute("expiration", fileItem.getExpiration());

        if (fileItem.getIstext()) {
            model.addAttribute("thetext", new String(data));
            model.addAttribute("istext", true);
        } else {
            model.addAttribute("istext", false);
        }

        return "view";
    }

    @GetMapping("/view/{filename}/download")
    public void download(
            @PathVariable("filename") String fileName,
            HttpServletResponse response,
            Model model) {

        try {
            fileStorageService.getFile(fileName, response.getOutputStream());
        } catch (IOException ex) {
            logger.error(String.format("Error retreiving file"), ex);
            model.addAttribute("errormsg", "ERROR!");
            //return "view";
        } catch (FileStorageException ex) {
            logger.error(String.format("Error retreiving file"), ex);
            model.addAttribute("errormsg", "ERROR!");
            //return "view";
        }
        //return "view";
    }
}
