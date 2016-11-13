package engine;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class represents a state of the text input area or a part of it.
 */
public class Buffer {
    private List<TextElement> content;

    public Buffer() {
        content = new ArrayList<>();
    }

    public Buffer(List<TextElement> content) {
        /**
         * Copy the list here to avoid ConcurrentModificationException in
         * insertAtPosition(Buffer buffer, int position). There we work in elements
         * of ArrayList in addAll() and getContent() which leads to the exception.
         * With this copy, we completely work on different objects (not only
         * ArrayList-objects are different but also elements in it are different
         * between the lists.
         */
        this.content = new ArrayList<>(content);
    }

    public Buffer getCopy(int start, int end) {
        if (isValidSelection(start, end)) {
            return new Buffer(content.subList(start, end));
        } else if (isValidSelection(end, start)) {
            return new Buffer(content.subList(end, start));
        } else {
            // TODO: OR return new Buffer();
            throw new IndexOutOfBoundsException("Couldn't create Buffer copy. Start and/or end index are invalid.");
        }
    }

    public Buffer getCopy() {
        return new Buffer(content);
    }

    public void insertAtPosition(TextElement character, int position) {
        if (isValidPositionWithFirst(position)) {
            content.add(position, character);
        } else if (isLastPosition(position)) {
            content.add(character);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void insertAtPosition(Buffer buffer, int position) {
        if (isValidPositionWithFirst(position)) {
            content.addAll(position, buffer.getContent());
        } else if(isLastPosition(position)) {
            content.addAll(buffer.getContent());
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void deleteAtPosition(int position) {
        if (isValidPositionWithFirst(position) && !content.isEmpty()) {
            content.remove(position);
        }
    }

    public void deleteInterval(int base, int end) {
        if (isValidSelection(base, end)) {
            content.subList(base, end).clear();
        } else if (isValidSelection(end, base)) {
            content.subList(end, base).clear();
        } else {
            throw new IndexOutOfBoundsException("Couldn't delete specified interval. Start and/or end index are invalid.");
        }
    }

    public int getWordStart(int position) {
        char c = content.get(position).getChar();
        int nextCheck = position - 1;

        while (nextCheck >= 0 &&
                areCharsOfSameType(c, content.get(nextCheck).getChar())) {
            nextCheck--;
        }
        return nextCheck + 1;
    }

    public int getWordEnd(int position) {
        char c = content.get(position).getChar();
        int nextCheck = position + 1;

        while (nextCheck < content.size() &&
                areCharsOfSameType(c, content.get(nextCheck).getChar())) {
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

    public List<TextElement> getContent() {
        return content;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(TextElement s : content) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    private boolean isValidPositionWithFirst(int position) {
        return position >= 0 && position < content.size();
    }

    private boolean isValidPositionWithLast(int position) {
        return position > 0 && position <= content.size();
    }

    private boolean isLastPosition(int position) {
        return position == content.size();
    }

    private boolean isValidSelection(int start, int end) {
        return isValidPositionWithFirst(start) &&
                isValidPositionWithLast(end) &&
                start < end;
    }

    private static boolean areCharsOfSameType(char c1, char c2) {
        return ((isWhitespaceCharacter(c1) && isWhitespaceCharacter(c2)) ||
                (!isWhitespaceCharacter(c1) && !isWhitespaceCharacter(c2)));
    }

    public static boolean isWhitespaceCharacter(char c) {
        return c == ' ' || c == '\r' || c == '\t' || c == '\n' || c == '\f' || c =='\u000B'; // last one is vertical tab (VT)
    }

    private static Pattern PATTERN_SPECIAL_CHARS = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

    public static boolean isSpecialChar(char c) {
        Matcher m = PATTERN_SPECIAL_CHARS.matcher(String.valueOf(c));
        return m.find();
    }

    public static List<TextElement> convertCharsToTextElements(List<Character> chars) {
        return chars.stream().map(c -> new TextElement(c)).collect(Collectors.toList());
    }
}
