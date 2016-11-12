package engine;

import commands.Command;

import java.util.List;

/**
 * Created by mo on 12.11.16.
 */
public interface IRecordModule {
    void start();
    void stop();
    void record(Command command);
    RecordModule clear();
    List<Command> getReplayList();
}
