package engine.interfaces;

import engine.Buffer;
import engine.Selection;

import java.util.List;

/**
 * This interface specifies the API of a spell checker.
 */
public interface ISpellCheckModule {
    /**
     * This operation accepts a Buffer over which a spell check should be
     * applied. It returns a list of selections (start and end position)
     * at which misspelled words are located.
     *
     * @param buffer to be spell checked.
     * @return list of selections of misspelled words.
     */
    List<Selection> getMisspelledWords(Buffer buffer);
}
