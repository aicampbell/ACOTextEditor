package engine;

/**
 * This class abstracts a selection in the text.
 *
 * Instead of two own integer values an abstraction class like this makes
 * sense to express the strong link and semantic between both the start
 * and end index. Secondly, it makes sense for the spell checker to return
 * a list of misspelled words and their respective Selections (with this
 * class this association can be implemented as a map).
 */
public class Selection {
    int selectionBase;
    int selectionEnd;

    public Selection() {
    }

    public Selection(int base, int end) {
        selectionBase = base;
        selectionEnd = end;
    }

    /**
     * Creates and returns a copied Selection object with the state of the current instance.
     *
     * @return a copied Selection object
     */
    public Selection getCopy() {
        return new Selection(selectionBase, selectionEnd);
    }


    /**
     * Resets the selection values to zero.
     */
    public void clear() {
        selectionBase = 0;
        selectionEnd = 0;
    }

    public int getSelectionBase() {
        return selectionBase;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public void setSelectionBase(int selectionBase) {
        this.selectionBase = selectionBase;
    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }
}
