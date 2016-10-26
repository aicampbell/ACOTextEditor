package engine;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a state of the text input area or a part of it.
 */
public class Buffer {
    private List<String> content;

    public Buffer() {
        content = new ArrayList<String>();
    }

    private Buffer(List<String> content) {
        this.content = content;
    }

    public void insertAtPosition(String character, int position) {
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

    public void deleteInterval(int start, int end) {
        if (isValidSelection(start, end)) {
            content.subList(start, end).clear();
        } else {
            throw new IndexOutOfBoundsException("Couldn't delete specified interval. Start and/or end index are invalid.");
        }
    }

    public Buffer getCopy(int start, int end) {
        if (isValidSelection(start, end)) {
            return new Buffer(content.subList(start, end));
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

    private List<String> getContent() {
        return content;
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
