package engine;

import commands.Command;

import java.util.Stack;

/**
 * Created by mo on 14.10.16.
 */
public class CommandHistory {
    private Engine engine;

    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public CommandHistory(Engine engine) {
        this.engine = engine;
        undoStack = new Stack<Command>();
        redoStack = new Stack<Command>();
    }

    /**
     * Is called for every undo() operation. Last Command in undoStack will be reverted
     * and pushed on the redoStack.
     */
    public void undo() {
        if (!undoStack.empty()) {
            Command undoCommand = undoStack.pop();
            //undoCommand.undo(); // Not implemented yet
            redoStack.push(undoCommand);
        }
    }

    /**
     * Is called for every redo() operation. Last undone Command in redoStack will be re-executed
     * and pushed on the undoStack.
     */
    public void redo() {
        if (!redoStack.empty()) {
            Command redoCommand = redoStack.pop();
            redoCommand.execute(engine);
            undoStack.push(redoCommand);
        }
    }
}
