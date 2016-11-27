package engine;

import commands.DeleteCommand;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    Selection randomSelection;

    SpellCheckModule spellCheckModule;

    @Before
    public void setUp() throws Exception {
        engine = new Engine();
        randomBuffer = new Buffer(getRandomText());
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
        List<Character> list = new ArrayList<>();
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

        Buffer buffer = new Buffer(list);
        engine.setBuffer(buffer);

        engine.setCursorPosition(3);
        engine.selectCurrentWord(engine.getCursorPosition());
        assertEquals(engine.getSelection().getSelectionBase(), 0);
        assertEquals(engine.getSelection().getSelectionEnd(), 6);

    }

    @Test
    public void checkSelectCurrentWord_when_space() {
        List<Character> list = new ArrayList<>();
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

        Buffer buffer = new Buffer(list);
        engine.setBuffer(buffer);

        engine.setCursorPosition(6);
        engine.selectCurrentWord(engine.getCursorPosition());
        assertEquals(engine.getSelection().getSelectionBase(), 6);
        assertEquals(engine.getSelection().getSelectionEnd(), 7);
    }

    @Test
    public void getCopy() {

        List<Character> list = new ArrayList<>();
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
        List<Character> originalList = new ArrayList<>();
        originalList.add('r');
        originalList.add('a');
        originalList.add('n');
        originalList.add('d');
        originalList.add('o');
        originalList.add('m');
        originalList.add(' ');
        originalList.add('w');
        originalList.add('o');
        originalList.add('r');
        originalList.add('d');
        originalList.add(' ');
        originalList.add('i');
        originalList.add('s');


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
        List<Character> originalList = new ArrayList<>();
        originalList.add('r');
        originalList.add('a');
        originalList.add('n');
        originalList.add('d');
        originalList.add('o');
        originalList.add('m');
        originalList.add(' ');
        originalList.add('w');
        originalList.add('o');
        originalList.add('r');
        originalList.add('d');
        originalList.add(' ');
        originalList.add('i');
        originalList.add('s');


        Buffer buffer = new Buffer(originalList);
        engine.setBuffer(buffer);

        engine.setSelection(new Selection(0, 5));
        engine.setIsTextSelected(true);
        engine.copySelection();

        assertNotEquals(engine.getBuffer().getContent(), originalList);
        for (int i = engine.getSelection().getSelectionBase(); i < engine.getSelection().getSelectionEnd() - 1; i++) {
            assertEquals(engine.getClipboard().getContent().get(i), originalList.get(i));
        }
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
g
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

        List<Character> list = new ArrayList<>();
        list.add('r');
        list.add('a');
        list.add('n');
        list.add('d');
        list.add('o');
        list.add('m');

        Buffer buffer = new Buffer(list);
        engine.setBuffer(buffer);
        engine.spellCheck();

        List<Selection> misspelledWordSelections = engine.getMisspelledWordSelections();
        assertEquals(misspelledWordSelections.size(), 0);
    }

    //Cant get location of file to work
    @Test
    public void givenTxtFile_whenTxtFileOpened_bufferIsOverwritten() {
        FileReader file = null;
        try {
            file = new FileReader(String.valueOf(getClass().getResource("Test.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(file.read());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        engine.openFile();
    }
}