package engine;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a state of the text input area or a part of it.
 */
public class Buffer {
    private List<Character> content;

    public Buffer() {
        content = new ArrayList<Character>();
    }

    private Buffer(List<Character> content) {
        /**
         * Copy the list here to avoid ConcurrentModificationException in
         * insertAtPosition(Buffer buffer, int position). There we work in elements
         * of ArrayList in addAll() and getContent() which leads to the exception.
         * With this copy, we completely work on different objects (not only
         * ArrayList-objects are different but also elements in it are different
         * between the lists.
         */
        this.content = new ArrayList<Character>(content);
    }

    public void insertAtPosition(char character, int position) {
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

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public int getSize() {
        return content.size();
    }

    private List<Character> getContent() {
        return content;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Character s : content) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }


    public boolean isValidPositionWithFirst(int position) {
        return position >= 0 && position < content.size();
    }

    public boolean isValidPositionWithLast(int position) {
        return position > 0 && position <= content.size();
    }

    public boolean isLastPosition(int position) {
        return position == content.size();
    }

    public boolean isValidSelection(int start, int end) {
        return isValidPositionWithFirst(start) &&
                isValidPositionWithLast(end) &&
                start < end;
    }
}
