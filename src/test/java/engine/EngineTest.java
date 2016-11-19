package engine;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class implements unit tests for the Engine.
 */
public class EngineTest {
    Engine engine;
    Buffer randomBuffer;
    Selection randomSelection;

    @Before
    public void setUp() throws Exception {
        engine = new Engine();
        randomBuffer = new Buffer(getRandomText());
        randomSelection = new Selection(1, 3);
    }

    @Test
    public void givenCharAndInitialEngineState_whenCharInserted_thenEngineCorrectlyUpdatesTextAndCursor() throws Exception {
        // given
        char c = 'm';
        int cursorPosition = 0;
        engine.setCursorPosition(cursorPosition);

        // when
        engine.insertChar(c);

        // then
        assertThat(engine.getCursorPosition()).isEqualTo(cursorPosition + 1);
        assertThat(engine.getBuffer().getContent()).hasSize(1);
        assertThat(engine.getBuffer().getContent().get(cursorPosition)).isEqualTo(c);
    }

    @Test
    public void givenCharAndRandomEngineTextState_whenCharInserted_thenEngineCorrectlyUpdatesTextAndCursor() throws Exception {
        // given
        char c = 'm';
        int cursorPosition = 3;
        engine.setCursorPosition(cursorPosition);
        engine.setBuffer(randomBuffer);

        // when
        engine.insertChar(c);

        // then
        assertThat(engine.getCursorPosition()).isEqualTo(cursorPosition + 1);
        assertThat(engine.getBuffer().getContent()).hasSize(engine.getBuffer().getSize());
        assertThat(engine.getBuffer().getContent().get(cursorPosition)).isEqualTo(c);
    }

    @Test
    public void givenCharAndRandomEngineTextState_whenCharInserted_thenSelectionIsOverwrittenByChar() throws Exception {
        // given
        char c = 'm';
        engine.setBuffer(randomBuffer);
        engine.setSelection(randomSelection);
        int initialBufferSize = engine.getBuffer().getSize();
        engine.setIsTextSelected(true);

        // when
        engine.insertChar(c);

        // then
        assertThat(engine.getCursorPosition()).isEqualTo(randomSelection.getSelectionBase() + 1);
        assertThat(engine.getBuffer().getContent()).hasSize(
                initialBufferSize -
                engine.getSelection().getSelectionSize() + 1);
        assertThat(engine.getBuffer().getContent()).containsExactly('r', 'm', '\t', 'd', '.', ' ', '0', 'm');
    }

    private List<Character> getRandomText(){
        // Builds "r4n   d. 0m" string
        List<Character> list = new ArrayList<>();
        list.add('r');
        list.add('4');
        list.add('n');
        list.add('\t');
        list.add('d');
        list.add('.');
        list.add(' ');
        list.add('0');
        list.add('m');
        return list;
    }

}