package engine;

import java.util.Stack;

/**
 * Created by mo on 14.10.16.
 */
public class UndoModule {
    private Stack<Memento> undoStack;
    private Stack<Memento> redoStack;

    public UndoModule() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void save(Memento memento) {
        undoStack.push(memento);
        
        /** Clear redoStack to implement mose common undo/redo policy */
        redoStack.clear();
    }

    /**
     * Is called for every undo() operation. Last Memento (state) in undoStack will be reverted
     * and pushed to the redoStack.
     *
     * @return Memento that should be new state of Engine and UI.
     */
    public Memento undo() {
        /**
         * If undoStack is not empty, push most recent state n to redoStack.
         * Return the state n-1 if undoStack is still not empty.
         * If undoStack is in fact empty, return a zero state.
         */
        if (!undoStack.isEmpty()) {
            Memento memento = undoStack.pop();
            redoStack.push(memento);

            if(!undoStack.isEmpty()) {
                return undoStack.peek();
            }
        }
        return Memento.InitialMemento;
    }

    /**
     * Is called for every redo() operation. Last undone Memento in redoStack will be recovered
     * and pushed to the undoStack.
     *
     * @return Memento that should be new state of Engine and UI.
     */
    public Memento redo() {
        /**
         * If redoStack is not empty, push most recent state n to undoStack and return it.
         * If redoStack is empty, nothing can be redone, so we keep returning the most recent
         * state of undoStack. If undoStack is empty as well, then there hasn't been any
         * interaction with the text editor yet, so we return a zero-state.
         */
        if (!redoStack.empty()) {
            Memento memento = redoStack.pop();
            undoStack.push(memento);
            return memento;
        }

        if(!undoStack.isEmpty()) {
            return undoStack.peek();
        }
        return Memento.InitialMemento;
    }
}
