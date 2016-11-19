package engine;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static engine.Buffer.isSpecialChar;
import static engine.Buffer.isWhitespaceCharacter;

/**
 * This class realizes a spell checker that takes a Buffer object, processes it by
 * comparing its content with a dictionary, and returns the misspelled words in the
 * Buffer object.
 */
public class SpellCheckModule implements ISpellCheckModule {
    /** Path to the dictionary that is loaded during initialization. */
    private static String PATH_TO_DICTIONARY = "/dictionaries/british-english.txt";

    /** Loaded dictionary as set of correctly spelled words. */
    private Set<String> dictionary;

    public SpellCheckModule() {
        dictionary = new HashSet<>();
        loadDictionary(PATH_TO_DICTIONARY);
    }

    /**
     * Returns misspelled words in the buffer by first transforming the buffer into
     * a map in which words are mapped to its position in the buffer. As a second
     * step each word in the map is spell checked and misspelled words are returned as
     * a list of Selections.
     *
     * @param buffer to be checked for misspelled words
     * @return list of selections of words that are considered misspelled
     */
    public List<Selection> getMisspelledWords(Buffer buffer) {
        /** Transform buffer into Map<Selection, String>. */
        Map<Selection, String> words = getWords(buffer);

        /**
         * Iterate over every map entry and filter them by misspelled words. The
         * result is returned as list.
         */
        return words.entrySet()
                .stream()
                .filter(m -> isWordMisspelled(m.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * This method processes a Buffer into a Map<Selection, String> in which each
     * word is mapped to its start and end position in the buffer.
     *
     * @param buffer to be transformed into word-map.
     * @return map representation of the buffer which maps words to its position
     * in the buffer.
     */
    private Map<Selection, String> getWords(Buffer buffer) {
        Map<Selection, String> words = new HashMap<>();
        StringBuilder word = new StringBuilder();

        int indexStart = 0;
        int indexEnd;

        for (int i = 0; i < buffer.getContent().size(); i++) {
            char c = buffer.getCharAtPosition(i);

            if (isWhitespaceCharacter(c) || isSpecialChar(c)) {
                /**
                 * Whitespace character signals end of previous word. If word isn't empty,
                 * we build it to a string and add it to the list. Then clean the StringBuilder.
                 */
                if (word.toString().length() > 0) {
                    indexEnd = i;
                    words.put(new Selection(indexStart, indexEnd), word.toString());
                    word.setLength(0);
                }
            } else {
                if (word.toString().length() == 0) {
                    indexStart = i;
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

    /**
     * Checks if a string is contained in the dictionary. The string is normalized
     * because the dictionary is normalized too (all words are lowercase).
     *
     * @param word to be checked.
     * @return true if words is not found in the dictionary. False otherwise.
     */
    private boolean isWordMisspelled(String word) {
        return !dictionary.contains(word.toLowerCase());
    }

    /**
     * Loads the file at location {@link SpellCheckModule#PATH_TO_DICTIONARY} and adds each
     * line in the text file as word in the dictionary.
     *
     * Assumes that the dictionary file contains one (normalized) word per line.
     *
     * @param path to the dictionary file
     */
    private void loadDictionary(String path) {
        InputStream inputStream = SpellCheckModule.class.getResourceAsStream(path);

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
