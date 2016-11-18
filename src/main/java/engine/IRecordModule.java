package engine;

import commands.Command;

import java.util.List;

/**
 * This interface specifies the API of an object responsible for recording
 * macros.
 */
public interface IRecordModule {
    /**
     * Starts recording a marco if there isn't already a recording happening.
     */
    void start();

    /**
     * Stops recording a macro if there is currently a recording happening.
     */
    void stop();

    /**
     * Records the passed Command by adding it to a list of all recorded commands
     * for the current macro.
     *
     * @param command to be recorded.
     */
    void record(Command command);

    /**
     * Deletes the currently stored macro if there has been one recorded.
     *
     * @return an instance of RecordModule to allow a fluent API
     */
    RecordModule clear();

    /**
     * Returns the recorded macro as a list of Commands that can be replayed
     * by the caller in order.
     *
     * @return the macro as a list of recorded Commands
     */
    List<Command> getReplayList();
}
