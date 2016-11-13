package commands;

import engine.Engine;

/**
 * This interface is implemented by all existing commands that are constructed on
 * the GUI side and sent to the Engine. The implementations are mostly stateless
 * and only hold the minimum required information to run the command.
 */
public interface Command {
    /**
     * Every command must be executable. The implementation of this operation
     * is the sum of actions that are needed in order to run the command.
     * In fact it mostly only specifies which method to call on the Engine.
     *
     * @param engine instance on which a command is executed.
     */
    void execute(Engine engine);
}
