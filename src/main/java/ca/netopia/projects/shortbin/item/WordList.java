package ca.netopia.projects.shortbin.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class WordList {
    private final static String WORDLIST_PATH = "wordlist.txt";
    private static ArrayList<String> words = new ArrayList<String>();
    private static final Logger logger = LoggerFactory.getLogger(WordList.class);

   private static void init() {
        try {
            words.clear();

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

    private static Boolean isInitialized() {
       return ! words.isEmpty();
    }

    public static String getWord() {
       if (! isInitialized()) {
           init();
       }
       Random r = new Random();
       return words.get(r.nextInt(words.size()));
    }
}
