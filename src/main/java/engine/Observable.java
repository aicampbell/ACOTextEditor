package engine;

/**
 * Created by mo on 12.11.16.
 */
public interface Observable {
    void registerObserver(EngineObserver engineObserver);

    void unregisterObserver(EngineObserver engineObserver);

    void notifyTextChange();

    void notifyCursorChange();

    void notifySelectionChange();
}
