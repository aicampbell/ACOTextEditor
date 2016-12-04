package engine;

import commands.DeleteCommand;
import commands.InsertCommand;
import engine.interfaces.EngineObserver;
import org.junit.Before;
import org.junit.Test;
import ui.GUI;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This class implements unit tests for the Engine.
 */
public class EngineTest {
    Engine engine;
    Buffer randomBuffer;
    Buffer randomClipboard;
    Selection randomSelection;
    SpellCheckModule spellCheckModule;

    @Before
    public void setUp() throws Exception {
        engine = new Engine();
        randomBuffer = new Buffer(getRandomText());
        randomClipboard = new Buffer(getRandomText());
        randomSelection = new Selection(1, 3);
        spellCheckModule = new SpellCheckModule();
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



    @Test
    public void checkUpdateCursor() {
        engine.setBuffer(randomBuffer);
        engine.setCursorPosition(getRandomText().size());

        engine.updateCursor(getRandomText().size() - 1);

        assertEquals(engine.getCursorPosition(), getRandomText().size() - 1);
    }

    @Test
    public void checkExpandSelection() {
        engine.setSelection(randomSelection);
        engine.setIsTextSelected(true);
        int initialSelectionEnd = engine.getSelection().getSelectionEnd();
        engine.expandSelection(6);

        assertEquals(engine.getSelection().getSelectionBase(), 1);
        assertNotEquals(engine.getSelection().getSelectionEnd(), initialSelectionEnd);
        assertEquals(engine.getSelection().getSelectionEnd(), 6);

    }

    @Test
    public void checkSelectCurrentWord() {
        List<Character> list = getRandomTxtWithMultipleWords();

        Buffer buffer = new Buffer(list);
        engine.setBuffer(buffer);

        engine.setCursorPosition(3);
        engine.selectCurrentWord(engine.getCursorPosition());
        assertEquals(engine.getSelection().getSelectionBase(), 0);
        assertEquals(engine.getSelection().getSelectionEnd(), 6);

    }

    @Test
    public void checkSelectCurrentWord_when_space() {
        List<Character> list = getRandomTxtWithMultipleWords();

        Buffer buffer = new Buffer(list);
        engine.setBuffer(buffer);

        engine.setCursorPosition(6);
        engine.selectCurrentWord(engine.getCursorPosition());
        assertEquals(engine.getSelection().getSelectionBase(), 6);
        assertEquals(engine.getSelection().getSelectionEnd(), 7);
    }

    @Test
    public void getCopy() {

        List<Character> list = getRandomTxtWithMultipleWords();

        engine.setBuffer(new Buffer(list));
        engine.setSelection(new Selection(1, 5));
        engine.setIsTextSelected(true);
        engine.copySelection();

        engine.getClipboard();

        for (int i = engine.getSelection().getSelectionBase(); i < engine.getSelection().getSelectionEnd() - 1; i++) {
            assertEquals(engine.getClipboard().getContent().get(i), list.get(i + 1));
        }
    }

    @Test
    public void checkCutSelection() {
        List<Character> originalList = getRandomTxtWithMultipleWords();
        Buffer buffer = new Buffer(originalList);
        engine.setBuffer(buffer);
        assertEquals(engine.getBuffer().getContent(), originalList);

        engine.setSelection(new Selection(0, 5));
        engine.setIsTextSelected(true);
        engine.cutSelection();

        assertNotEquals(engine.getBuffer().getContent(), originalList);
        for (int i = engine.getSelection().getSelectionBase(); i < engine.getSelection().getSelectionEnd() - 1; i++) {
            assertEquals(engine.getClipboard().getContent().get(i), originalList.get(i));
        }
    }

    @Test
    public void checkPasteClipboard() {
        engine.setBuffer(randomBuffer);
        engine.setClipboard(randomClipboard);
        engine.setCursorPosition(randomBuffer.getSize());

        engine.pasteClipboard();

        randomBuffer.getContent().addAll(randomBuffer.getContent());
        assertThat(engine.getBuffer().getContent()).containsAll(randomBuffer.getContent());
    }

    @Test
    public void givenTxtInBuffer_whenBufferHasContents_DeleteInBackWardsDirection() {
        engine.setCursorPosition(getRandomText().size());
        engine.setBuffer(randomBuffer);

        engine.deleteInDirection(DeleteCommand.DEL_BACKWARDS);
        List<Character> originalContent = getRandomText();

        //Shows that a character was deleted by showing the number of characters is one less
        assertEquals(engine.getBuffer().getContent().size(), originalContent.size() - 1);

        //Shows that the character was deleted backwards by putting the cursor at the last position, deleting a character,
        //then comparing the new content's last character with the second last character of the original array.
        assertEquals(engine.getBuffer().getContent().get(engine.getBuffer().getContent().size() - 1), originalContent.get(originalContent.size() - 2));

    }

    @Test
    public void givenTxtInBuffer_whenBufferHasContents_DeleteInForwardsDirection() {
        engine.setCursorPosition(0);
        engine.setBuffer(randomBuffer);
        engine.deleteInDirection(DeleteCommand.DEL_FORWARDS);
        List<Character> originalContent = getRandomText();

        //Shows that a character was deleted by showing the number of characters is one less
        assertEquals(engine.getBuffer().getContent().size(), originalContent.size() - 1);

        //Shows that the character was deleted backwards by putting the cursor at the last position, deleting a character,
        //then comparing the new content's last character with the second last character of the original array.
        assertEquals(engine.getBuffer().getContent().get(0), originalContent.get(1));
    }

    @Test
    public void givenTxtInBufferAndSelection_whenBufferHasContents_thenDeleteSelection() {
        engine.setBuffer(randomBuffer);
        engine.setCursorPosition(3);
        engine.setSelection(randomSelection);
        engine.setIsTextSelected(true);
        engine.deleteInDirection(DeleteCommand.DEL_FORWARDS);

        List<Character> expectedResult = new ArrayList<>();
        expectedResult.add('r');
        expectedResult.add('\t');
        expectedResult.add('d');
        expectedResult.add('.');
        expectedResult.add(' ');
        expectedResult.add('0');
        expectedResult.add('m');


        //Shows that selection was deleted by checking new buffer size.
        assertEquals(engine.getBuffer().getContent().size(), expectedResult.size());
        assertThat(engine.getBuffer().getContent()).isEqualTo(expectedResult);
    }

    @Test
    public void givenTxtInBufferIsMisspelled_whenBufferHasContents_spellCheck() {
        List<Character> list = new ArrayList<>();
        list.add('r');
        list.add('a');
        list.add('n');
        list.add('d');
        list.add('o');
        list.add('m');
        list.add('k');

        Buffer buffer = new Buffer(list);
        engine.setBuffer(buffer);

        engine.spellCheck();

        List<Selection> misspelledWordSelections = engine.getMisspelledWordSelections();
        assertEquals(misspelledWordSelections.get(0).getSelectionBase(), 0);
        assertEquals(misspelledWordSelections.get(0).getSelectionEnd(), list.size());
    }

    @Test
    public void givenTxtInBufferIsNotMisspelled_whenBufferHasContents_spellCheck() {
        List<Character> list = getRandomTxtWithMultipleWords();

        Buffer buffer = new Buffer(list);
        engine.setBuffer(buffer);
        engine.spellCheck();

        List<Selection> misspelledWordSelections = engine.getMisspelledWordSelections();
        assertEquals(misspelledWordSelections.size(), 0);
    }

    public List<Character> getRandomTxtWithMultipleWords(){
        List<Character>list = new ArrayList<>();
        list.add('r');
        list.add('a');
        list.add('n');
        list.add('d');
        list.add('o');
        list.add('m');
        list.add(' ');
        list.add('w');
        list.add('o');
        list.add('r');
        list.add('d');
        list.add(' ');
        list.add('i');
        list.add('s');

        return list;
    }

    @Test
    public void checkUndoCommand() {
        engine.setBuffer(randomBuffer);
        engine.setCursorPosition(randomBuffer.getSize());
        List<Character> expectedResult = getRandomText();
        expectedResult.add('n');

        engine.insertChar('n');
        engine.insertChar('m');
        engine.undoCommand();

        assertThat(engine.getBuffer().getContent()).isEqualTo(expectedResult);
    }

    @Test
    public void checkRedoCommand() {
        engine.setBuffer(randomBuffer);
        engine.setCursorPosition(randomBuffer.getSize());
        List<Character> expectedResult = getRandomText();
        expectedResult.add('n');
        expectedResult.add('m');

        engine.insertChar('n');
        engine.insertChar('m');
        engine.undoCommand();
        engine.redoCommand();

        assertThat(engine.getBuffer().getContent()).isEqualTo(expectedResult);
    }

    @Test
    public void checkRecordReplayFeature() {
        engine.setBuffer(randomBuffer);
        engine.setCursorPosition(randomBuffer.getSize());
        List<Character> expectedResult = getRandomText();
        InsertCommand insertCommand1 = new InsertCommand('n');
        InsertCommand insertCommand2 = new InsertCommand('1');
        expectedResult.add('n');
        expectedResult.add('1');
        expectedResult.add('n');
        expectedResult.add('1');
        expectedResult.add('n');
        expectedResult.add('1');

        engine.startRecording();
        insertCommand1.execute(engine);
        insertCommand2.execute(engine);
        engine.stopRecording();
        // replay the commands twice to also proof that multiple replays are running fine
        engine.replayRecording();
        engine.replayRecording();

        assertThat(engine.getBuffer().getContent()).isEqualTo(expectedResult);
    }

    @Test
    public void checkOpenFile() {
        // Assumes that File-IO was successful and List<Character> was successfully extracted from file.
        // File-IO could be mocked but in this case, the mocking doesn't add any additional test value.
        engine.openFile(getRandomText());

        assertThat(engine.getBuffer().getContent()).isEqualTo(getRandomText());
    }

    @Test
    public void checkRegisteringObserver() {
        EngineObserver engineObserver = new GUI();

        engine.registerObserver(engineObserver);

        assertThat(engine.getObservers()).hasSize(1);
        assertThat(engine.getObservers().get(0)).isInstanceOf(EngineObserver.class);
        assertThat(engine.getObservers().get(0)).isEqualTo(engineObserver);
    }

    @Test
    public void checkUnregisteringObserver() {
        EngineObserver engineObserver = new GUI();

        engine.registerObserver(engineObserver);
        engine.unregisterObserver(engineObserver);

        assertThat(engine.getObservers()).hasSize(0);
    }

    private List<Character> getRandomText() {
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