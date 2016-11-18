package engine;

/**
 * Memento object that can store complete Engine states.
 *
 * A state of the Engine is defined as the sum of the following values:
 * <ul>
 *     <li>text content</li>
 *     <li>clipboard content</li>
 *     <li>cursor position</li>
 *     <li>selection start and end positions</li>
 * </ul>
 */
public class Memento implements IMemento {
    /**
     * Provides an initial/empty Memento. Makes use of the Null-object design pattern.
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

    /**
     * Constructor requires values for each of the defined component that contributes
     * to a Momento object.
     *
     * @param buffer text to be saved
     * @param clipboard clipboard to be saved
     * @param cursorPosition cursor position to be saved
     * @param selectionBase selection start to be saved
     * @param selectionEnd selection end to be saved
     */
    public Memento(Buffer buffer, Buffer clipboard, int cursorPosition, int selectionBase, int selectionEnd) {
        this.buffer = buffer;
        this.clipboard = clipboard;
        this.cursorPosition = cursorPosition;
        this.selectionBase = selectionBase;
        this.selectionEnd = selectionEnd;
    }

    /**
     * Returns a copy of the text.
     *
     * @return copy of text as Buffer object.
     */
    public Buffer getBuffer() {
        return buffer.getCopy();
    }

    /**
     * Returns a copy of the clipboard.
     *
     * @return copy of clipboard as Buffer object.
     */
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
