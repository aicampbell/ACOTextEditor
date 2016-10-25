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
        if (isValidPosition(position)) {
            content.add(position, character);
        } else if (position == content.size()) {
            content.add(character);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void insertAtPosition(Buffer buffer, int position) {
        if (isValidPosition(position)) {
            content.addAll(position, buffer.getContent());
        }
    }

    public void deleteAtPosition(int position) {
        if (isValidPosition(position) && content.size() > 0) {
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
            throw new IndexOutOfBoundsException("Couldn't create Buffer copy. Start and/or end index are invalid.");
        }
    }

    public int getSize() {
        return content.size();
    }

    private List<String> getContent() {
        return content;
    }

    private boolean isValidPosition(int position) {
        return position >= 0 && position < content.size();
    }

    private boolean isValidSelection(int start, int end) {
        return isValidPosition(start) && isValidPosition(end) && start < end;
    }
}
