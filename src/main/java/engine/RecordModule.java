package engine;

import commands.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 11.11.16.
 */
public class RecordModule {
    private List<Command> replayList;

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