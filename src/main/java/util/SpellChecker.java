package util;

import commands.interfaces.Visitor;

import java.io.BufferedReader;
import java.io.FileReader;
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
    }

    public void LoadDictionary(String dictionaryPath){
        BufferedReader reader =  null;
        try {
            String word;
            reader = new BufferedReader(new FileReader(dictionaryPath));
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
