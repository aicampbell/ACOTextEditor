package engine;

/**
 * Created by mo on 12.11.16.
 */
public interface IMemento {
    Buffer getBuffer();

    Buffer getClipboard();

    int getCursorPosition() ;

    int getSelectionBase();

    int getSelectionEnd();
}
