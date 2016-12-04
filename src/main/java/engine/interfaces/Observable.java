package engine.interfaces;

import engine.Selection;

import java.util.List;

/**
 * This interface specifies the Observable element in the Observer design pattern.
 *
 * It is responsible for registering and unregistering observers as well es providing
 * operations that can be called to notify the observers about events. In our case we
 * distinguish between 3 different events:
 *
 * <ul>
 *      <li>text state changed</li>
 *      <li>cursor position state changed</li>
 *      <li>selection positions state changed</li>
 *      <li>results of a performed spell check are available</li>
 * </ul>
 */
public interface Observable {
    /**
     * Registers an EngineObserver instance as observer for the implementing class.
     *
     * @param engineObserver to be registered.
     */
    void registerObserver(EngineObserver engineObserver);

    /**
     * Unregisters an EngineObserver instance in the implementing class. In practise
     * this operation is never used but still available to complete the Observer pattern
     * and to allow future extensibility.
     *
     * @param engineObserver to be unregistered.
     */
    void unregisterObserver(EngineObserver engineObserver);

    /**
     * Runs necessary code to notify EngineObserver(s) about changes in the text state.
     */
    void notifyTextChange();

    /**
     * Runs necessary code to notify EngineObserver(s) about changes in the cursor
     * position state.
     */
    void notifyCursorChange();

    /**
     * Runs necessary code to notify EngineObserver(s) about changes in the selection
     * positions state.
     */
    void notifySelectionChange();

    /**
     * Runs necessary code to notify EngineObserver(s) about a new result of a spell
     * check run. The result is in our design not a state of the Engine because it is
     * independent on other operations. In fact, the notify will only occur when a spell
     * check is chosen by the user. Therefore we pass the result directly as parameter.
     */
    void notifyMisspelledWordsChange(List<Selection> selections);
}
