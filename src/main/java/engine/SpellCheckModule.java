package engine;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static engine.Buffer.isWhitespaceCharacter;

/**
 * Created by aidan on 12/11/16.
 */
public class SpellCheckModule {
    private static String PATH_TO_DICTIONARY = "/dictionaries/british-english.txt";

    private Set<String> dictionary;

    public SpellCheckModule() {
        dictionary = new HashSet<>();
        loadDictionary();
    }

    public void spellCheck(Buffer buffer) {
        List<String> words = getWords(buffer);
        words.forEach(w -> spellCheck(w));
    }

    private List<String> getWords(Buffer buffer) {
        List<String> words = new ArrayList<>();
        StringBuilder word = new StringBuilder();

        for (TextElement te : buffer.getContent()) {
            char c = te.getChar();
            if (isWhitespaceCharacter(c)) {
                /**
                 * Whitespace char signals end of previous word. If word isn't empty,
                 * we build it to a string and add it to the list. Then clean the StringBuilder.
                 */
                if (word.toString().length() > 0) {
                    words.add(word.toString());
                    word.setLength(0);
                }
            } else {
                word.append(c);
            }
        }

        /**
         * Every character of Buffer has been traversed. However the last word might not have
         * been saved to the list since a following whitespace char didn't exist (buffer end).
         *
         * In this case, StringBuilder word is not empty. We must build the string and add it
         * to the list as last entry.
         */
        if (word.toString().length() > 0) {
            words.add(word.toString());
        }

        return words;
    }

    private void spellCheck(String word) {
        if (!dictionary.contains(word)) {
            // TODO: Output on console not so nice. Better: Underline word in UI.
            System.out.println("Spell check failed for word: " + word);
        }
    }

    private void loadDictionary() {
        InputStream inputStream = SpellCheckModule.class.getResourceAsStream(PATH_TO_DICTIONARY);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String word;
            while ((word = br.readLine()) != null) {
                dictionary.add(word);
            }
        } catch (IOException e) {
            System.out.println("Can't load dictionary. " + e.getMessage());
        }
    }
}
