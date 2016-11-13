package engine;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static engine.Buffer.isSpecialChar;
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

    public Map<Selection, String> getMisspelledWords(Buffer buffer) {
        Map<Selection, String> words = getWords(buffer);

        return words.entrySet()
                .stream()
                .filter(m -> isWordMisspelled(m.getValue()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    private Map<Selection, String> getWords(Buffer buffer) {
        Map<Selection, String> words = new HashMap<>();
        StringBuilder word = new StringBuilder();

        int indexStart = 0;
        int indexEnd;

        for (TextElement te : buffer.getContent()) {
            char c = te.getChar();
            if (isWhitespaceCharacter(c) || isSpecialChar(c)) {
                /**
                 * Whitespace char signals end of previous word. If word isn't empty,
                 * we build it to a string and add it to the list. Then clean the StringBuilder.
                 */
                if (word.toString().length() > 0) {
                    indexEnd = buffer.getContent().indexOf(te);
                    words.put(new Selection(indexStart, indexEnd), word.toString());
                    word.setLength(0);
                }
            } else {
                if (word.toString().length() == 0) {
                    indexStart = buffer.getContent().indexOf(te);
                }
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
            indexEnd = buffer.getContent().size();
            words.put(new Selection(indexStart, indexEnd), word.toString());
        }

        return words;
    }

    private boolean isWordMisspelled(String word) {
        if (!dictionary.contains(word.toLowerCase())) {
            // TODO: Output on console not so nice. Better: Underline word in UI.
            System.out.println("Spell check failed for word: " + word);
            return true;
        }
        return false;
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
