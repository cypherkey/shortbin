package com.example.shortbin.filestorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

@Component
public class WordList {
    private final String WORDLIST_PATH = "wordlist.txt";
    private ArrayList<String> words = new ArrayList<String>();
    private static final Logger logger = LoggerFactory.getLogger(WordList.class);

    public WordList() {
        try {
            logger.info(String.format("Loading wordlist from %s", WORDLIST_PATH));
            Resource resource = new ClassPathResource(WORDLIST_PATH);
            InputStream is = resource.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String word;
            while((word = br.readLine()) != null) {
                words.add(word);
            }
            br.close();
            isr.close();
            is.close();
            logger.info(String.format("Loaded %d words", words.size()));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(String.format("word list %s not found", WORDLIST_PATH));
        } catch (IOException e) {
            throw new RuntimeException("error parsing word list");
        }
    }

    public String getWord() {
        Random r = new Random();
        return words.get(r.nextInt(words.size()));
    }
}
