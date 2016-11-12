package util;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by aidan on 12/11/16.
 */
public class SpellChecker {
    private static SpellChecker instance = null;
    private Set<String> dictionary;

    // TODO: remove singleton
    public static SpellChecker getInstance() {
        if (instance == null) {
            instance = new SpellChecker();
        }
        return instance;
    }

    private SpellChecker() {
        dictionary = new HashSet<>();

        try {
            loadDictionary();
        } catch (IOException e) {

        }
    }

    private void loadDictionary() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test.txt").getFile());

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String word;
            while ((word = br.readLine()) != null) {
                dictionary.add(word);
            }
        } catch (IOException e) {
            // TODO: refactor. rethrowing exception is bad.
            throw e;
        }
    }

    public boolean isMisspelled(String word) {
        return !dictionary.contains(word);
    }
}
