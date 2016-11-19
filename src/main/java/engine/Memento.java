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
    private Buffer buffer;
    private Buffer clipboard;
    private Selection selection;
    private int cursorPosition;

    public static Memento getInitialMomento() {
        return new Memento();
    }

    /**
     * Private empty default constructor to enable method {@link Memento#getInitialMomento()}.
     */
    private Memento() {
    }

    /**
     * Constructor requires values for each of the defined component that contributes
     * to a Memento object.
     *
     * @param buffer text to be saved
     * @param clipboard clipboard to be saved
     * @param cursorPosition cursor position to be saved
     * @param selection selection to be saved
     */
    public Memento(Buffer buffer, Buffer clipboard, Selection selection, int cursorPosition) {
        this.buffer = buffer;
        this.clipboard = clipboard;
        this.selection = selection;
        this.cursorPosition = cursorPosition;
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

    public Selection getSelection() {
        return selection;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }
}
