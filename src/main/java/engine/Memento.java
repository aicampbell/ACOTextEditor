package engine;

/**
 * Created by mo on 12.11.16.
 */
public class Memento {
    /**
     * Use of null-object design pattern
     */
    public static Memento InitialMemento = new Memento(
            new Buffer(),
            new Buffer(),
            0,
            0,
            0
    );

    private Buffer buffer;
    private Buffer clipboard;
    private int cursorPosition;
    private int selectionBase;
    private int selectionEnd;

    public Memento(Buffer buffer, Buffer clipboard, int cursorPosition, int selectionBase, int selectionEnd) {
        this.buffer = buffer;
        this.clipboard = clipboard;
        this.cursorPosition = cursorPosition;
        this.selectionBase = selectionBase;
        this.selectionEnd = selectionEnd;
    }

    public Buffer getBuffer() {
        return buffer.getCopy();
    }

    public Buffer getClipboard() {
        return clipboard.getCopy();
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public int getSelectionBase() {
        return selectionBase;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }
}
