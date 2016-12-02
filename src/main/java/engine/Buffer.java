package engine;

import org.assertj.core.util.VisibleForTesting;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class represents a state of the text input area or a part of it.
 *
 * It can represent the current text visible, the content of the clipboard
 * or the content as part of a Memento object.
 */
public class Buffer {
    private List<Character> content;

    public Buffer() {
        content = new ArrayList<>();
    }

    public Buffer(List<Character> content) {
        /**
         * Copy the list here to avoid ConcurrentModificationException in
         * insertAtPosition(Buffer, int). There we work in elements
         * of ArrayList in addAll() and getContent() which leads to the exception.
         * With this copy, we completely work on different objects (not only
         * ArrayList-objects are different but also elements in it are different
         * between the lists. Creates a 'true copy'.
         */
        this.content = new ArrayList<>(content);
    }

    /**
     * Creates a copy of the buffer or a subset of it specified by the parameters.
     *
     * Since the start is not always smaller than the end of a selection, the parameters
     * of this method are validated twice to guarantee to provide a correct copy of a
     * selection.
     *
     * @param selection object of the desired selection
     *
     * @return the copied Buffer.
     */
    public Buffer getCopy(Selection selection) {
        int base = selection.getSelectionBase();
        int end = selection.getSelectionEnd();

        if (isValidSelection(base, end)) {
            return new Buffer(content.subList(base, end));
        } else if (isValidSelection(end, base)) {
            return new Buffer(content.subList(end, base));
        } else {
            // Should not be reached.
            throw new IndexOutOfBoundsException("Couldn't create Buffer copy. Start and/or end index are invalid.");
        }
    }

    /**
     * Creates a complete and true copy of the Buffer.
     *
     * @return the complete copy of Buffer.
     */
    public Buffer getCopy() {
        return new Buffer(content);
    }

    public char getCharAtPosition(int position) {
        if(isValidPositionWithFirst(position)) {
            return content.get(position);
        }
        throw new NullPointerException("Invalid position: " + position);
    }

    /**
     * Used for inserting single characters in the Buffer.
     *
     * @param character to be inserted.
     * @param position at which the character should be inserted.
     */
    public void insertAtPosition(Character character, int position) {
        if (isValidPositionWithFirst(position)) {
            content.add(position, character);
        } else if (isLastPosition(position)) {
            content.add(character);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Used for inserting multiple characters (stored in a Buffer object).
     *
     * @param buffer to be inserted.
     * @param position at which the characters in the Buffer should be inserted.
     */
    public void insertAtPosition(Buffer buffer, int position) {
        if (isValidPositionWithFirst(position)) {
            content.addAll(position, buffer.getContent());
        } else if(isLastPosition(position)) {
            content.addAll(buffer.getContent());
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Deletes one character at a position.
     *
     * @param position at which a character should be deleted.
     */
    public void deleteAtPosition(int position) {
        if (isValidPositionWithFirst(position) && !content.isEmpty()) {
            content.remove(position);
        }
    }

    /**
     * Deletes a range of characters specified by a selection.
     *
     * Since the start is not always smaller than the end of a selection, the parameters
     * of this method are validated twice to guarantee that specified selection is deleted.
     *
     * @param base position of selection to be deleted.
     * @param end position of selection to be deleted.
     */
    public void deleteInterval(int base, int end) {
        if (isValidSelection(base, end)) {
            content.subList(base, end).clear();
        } else if (isValidSelection(end, base)) {
            content.subList(end, base).clear();
        } else {
            throw new IndexOutOfBoundsException("Couldn't delete specified interval. Start and/or end index are invalid.");
        }
    }

    /**
     * Used for computing the selection start when user double-clicks a word.
     *
     * This method decrements position as long as type of character at position
     * is the same as the type of character at the initial position.
     *
     * @param position at which the user double-clicked.
     *
     * @return the computed selection start.
     */
    public int getWordStart(int position) {
        /** There is no character at the last position, so we pretend the (n-1)th position is clicked. */
        if(isLastPosition(position)) {
            position--;
        }
        char c = content.get(position);
        int nextCheck = position - 1;

        while (nextCheck >= 0 &&
                areCharsOfSameType(c, content.get(nextCheck))) {
            nextCheck--;
        }
        return nextCheck + 1;
    }

    /**
     * Used for computing the selection end when user double-clicks a word.
     *
     * This method increments position as long as type of character at position
     * is the same as the type of character at the initial position.
     *
     * @param position at which the user double-clicked.
     *
     * @return the computed selection end.
     */
    public int getWordEnd(int position) {
        /** There is no character at the last position, so we pretend the (n-1)th position is clicked. */
        if(isLastPosition(position)) {
            position--;
        }
        char c = content.get(position);
        int nextCheck = position + 1;

        while (nextCheck < content.size() &&
                areCharsOfSameType(c, content.get(nextCheck))) {
            nextCheck++;
        }
        return nextCheck;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public int getSize() {
        return content.size();
    }

    public List<Character> getContent() {
        return content;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Character c : content) {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    /**
     * Checks if a position is valid for the current content of the text editor.
     * E.g. this method is used to check if a character can be deleted at a position.
     *
     * This method returns true if cursor is at very first position.
     * This method returns false if cursor is at very last position.
     *
     * @param position to be checked.
     *
     * @return result of the check as boolean.
     */
    private boolean isValidPositionWithFirst(int position) {
        return position >= 0 && position < content.size();
    }

    /**
     * Checks if a position is valid for the current content of the text editor.
     * E.g. this method is used to check if a selection is valid.
     *
     * This method returns false if cursor is at very first position.
     * This method returns true if cursor is at very last position.
     *
     * @param position to be checked.
     *
     * @return result of the check as boolean.
     */
    private boolean isValidPositionWithLast(int position) {
        return position > 0 && position <= content.size();
    }

    /**
     * Checks if a position is the last valid position in the text editor.
     *
     * @param position to be checked.
     * @return result of the check as boolean.
     */
    private boolean isLastPosition(int position) {
        return position == content.size();
    }

    /**
     * Checks if a selection specified by start and end position is a valid one
     * in the context of the current state of the Buffer.
     *
     * @param start of selection.
     * @param end of selection.
     * @return result of the check as boolean.
     */
    private boolean isValidSelection(int start, int end) {
        return isValidPositionWithFirst(start) &&
                isValidPositionWithLast(end) &&
                start < end;
    }

    /**
     * Checks if two characters are both whitespace characters or both non-whitespace
     * characters. Used to determine start and end of a word when double-clicking it.
     *
     * @param c1 first character to be checked.
     * @param c2 second character to be checked.
     *
     * @return result of the check as boolean
     */
    private static boolean areCharsOfSameType(char c1, char c2) {
        return ((isWhitespaceCharacter(c1) && isWhitespaceCharacter(c2)) ||
                (!isWhitespaceCharacter(c1) && !isWhitespaceCharacter(c2)));
    }

    /**
     * Checks if a character is considered as whitespace character.
     *
     * @param character to be checked.
     * @return true if character is a whitespace character. False if not.
     */
    public static boolean isWhitespaceCharacter(char character) {
        return character == ' ' || character == '\r' || character == '\t' ||
                character == '\n' || character == '\f' || character =='\u000B'; // last one is vertical tab (VT)
    }

    private static Pattern PATTERN_SPECIAL_CHARS = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

    /**
     * Checks if a character is considered as special character.
     *
     * @param character to be checked.
     * @return true if character is a special character. False if not.
     */
    public static boolean isSpecialChar(char character) {
        Matcher m = PATTERN_SPECIAL_CHARS.matcher(String.valueOf(character));
        return m.find();
    }
}
