package engine;

import java.util.Stack;

/**
 * This class handles Undos and Redos by using the Memento design pattern.
 *
 * It does so by maintaining Mementos (state of Engine) over two stacks.
 */
public class UndoModule implements MementoCaretaker {
    private Stack<Memento> undoStack;
    private Stack<Memento> redoStack;

    public UndoModule() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void save(Memento memento) {
        undoStack.push(memento);

        /** Clear redoStack to implement mose common undo/redo policy. */
        redoStack.clear();
    }

    /**
     * Is called for every undo operation. Last Memento (state) in {@link UndoModule#undoStack}
     * will be reverted and pushed to the {@link UndoModule#redoStack}.
     *
     * @return Memento that should be new state of Engine and UI.
     */
    public Memento undo() {
        /**
         * If {@link UndoModule#undoStack} is not empty, push most recent state n to
         * {@link UndoModule#redoStack}. Return the state n-1 if {@link UndoModule#undoStack}
         * is still not empty. If {@link UndoModule#undoStack} is in fact empty, return an initial
         * object.
         */
        if (!undoStack.isEmpty()) {
            Memento memento = undoStack.pop();
            redoStack.push(memento);

            if(!undoStack.isEmpty()) {
                return undoStack.peek();
            }
        }
        return Memento.getInitialMemento();
    }

    /**
     * Is called for every redo operation. Last undone Memento in {@link UndoModule#redoStack}
     * will be recovered and pushed to the {@link UndoModule#undoStack}.
     *
     * @return Memento that should be new state of Engine and GUI.
     */
    public Memento redo() {
        /**
         * If {@link UndoModule#undoStack} is not empty, push most recent state n to
         * {@link UndoModule#redoStack} and return it. If {@link UndoModule#undoStack} is empty,
         * nothing can be redone, so we keep returning the most recent state of
         * {@link UndoModule#redoStack}. If {@link UndoModule#redoStack} is empty as well,
         * then there hasn't been any interaction with the text editor yet, so we return a zero-state.
         */
        if (!redoStack.empty()) {
            Memento memento = redoStack.pop();
            undoStack.push(memento);
            return memento;
        }

        if(!undoStack.isEmpty()) {
            return undoStack.peek();
        }

        return Memento.getInitialMemento();
    }
}
