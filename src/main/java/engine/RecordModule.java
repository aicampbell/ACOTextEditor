package engine;

import commands.Command;
import engine.interfaces.IRecordModule;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles recording and providing macros.
 */
public class RecordModule implements IRecordModule {
    /**
     * The commands recorded in a macro are added to this list.
     */
    private List<Command> replayList;

    /**
     * This boolean keeps track of the recording state. In case its value is
     * true, incoming commands are stored within {@link RecordModule#record(Command)}.
     */
    private boolean isRecording;

    public RecordModule() {
        replayList = new ArrayList<>();
    }

    public void start() {
        isRecording = true;
    }

    public void stop() {
        isRecording = false;
    }

    public void record(Command command) {
        if (isRecording) {
            replayList.add(command);
        }
    }

    public RecordModule clear() {
        replayList.clear();
        return this;
    }

    public List<Command> getReplayList() {
        return replayList;
    }
}