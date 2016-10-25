package listener;

import commands.Command;
import commands.UpdateCursorCommand;
import engine.Engine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created by mo on 25.10.16.
 */
public class CaretListener implements ChangeListener<Number> {
    private Engine engine;

    public CaretListener(Engine engine) {
        this.engine = engine;
    }

    public void changed(ObservableValue<? extends Number> observable,
                        Number oldPosition, Number newPosition) {
        // Always keep track of cursor changes, independent of selections.
        Command command = new UpdateCursorCommand(newPosition.intValue());
        command.execute(engine);
    }
}
