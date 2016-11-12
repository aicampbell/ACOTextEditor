package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aidan on 12/11/16.
 */
public class SpellChecker {

    private static SpellChecker instance = null;
    private Map<String, String> dictionary;

    public static SpellChecker getInstance() {
        if(instance == null) {
            instance = new SpellChecker();
        }
        return instance;
    }

    protected SpellChecker(){
        dictionary = new HashMap<String, String>();
        loadDictionary();
    }

    public void loadDictionary() {

        URL resource = SpellChecker.class.getResource("test.txt");
            System.out.println(resource.toString());

        BufferedReader reader =  null;
        try {
            String word;
            reader = new BufferedReader(new FileReader(""));
            while ((word = reader.readLine()) != null) {
                this.dictionary.put(word, word);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Boolean isMisspelled(String word){
        return !this.dictionary.containsKey(word);
    }
}
