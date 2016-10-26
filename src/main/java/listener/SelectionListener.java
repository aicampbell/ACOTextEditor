package listener;

import commands.Command;
import commands.UpdateSelectionCommand;
import engine.Engine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.IndexRange;

/**
 * Created by mo on 26.10.16.
 */
public class SelectionListener implements ChangeListener<IndexRange> {
    private Engine engine;

    public SelectionListener(Engine engine) {
        this.engine = engine;
    }

    public void changed(ObservableValue<? extends IndexRange> observable,
                        IndexRange oldRange, IndexRange newRange) {
        if(newRange.getStart() < newRange.getEnd()) {
            Command command = new UpdateSelectionCommand(newRange.getStart(), newRange.getEnd());
            command.execute(engine);
        }
    }
}
