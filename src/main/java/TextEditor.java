import commands.*;
import commands.Command;
import engine.Engine;
import engine.Selection;
import listener.KeyActionListener;
import listener.MouseActionListener;
import engine.EngineObserver;
import ui.Underliner;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Created by mo on 08.11.16.
 */
public class TextEditor implements EngineObserver {
    Engine engine;

    private JMenuBar jMenuBar;
    private JPanel jPanel;
    private JTextPane textPane;

    private Underliner redUnderliner;

    public TextEditor() {
        engine = new Engine();
        redUnderliner = new Underliner(Color.RED);
    }

    public void start(){
        engine.registerObserver(this);

        setupMenu();
        setupTextPanes();
        setupPanel();
        setupFrame();

        textPane.requestFocus();
    }

    private void setupTextPanes() {
        textPane = new JTextPane();
        textPane.setPreferredSize(new Dimension(768, 512));

        ActionMap am = textPane.getActionMap();
        textPane.getActionMap().put(DefaultEditorKit.selectWordAction,
                new TextAction(DefaultEditorKit.selectWordAction) {
                    public void actionPerformed(ActionEvent e) {
                        // DO NOTHING
                    }
                });
        am.clear();
        InputMap im = textPane.getInputMap();
        im.clear();


        // key listener
        textPane.addKeyListener(new KeyActionListener(
                textPane,
                engine
        ));

        // mouse listener
        MouseActionListener mouseActionListener = new MouseActionListener(
                textPane,
                engine
        );
        textPane.addMouseListener(mouseActionListener);
        textPane.addMouseMotionListener(mouseActionListener);

        // Dirty hack to disable default event propagation. With that only our previously added listeners
        // receive events which is desired.
        textPane.setCaret(new DefaultCaret() {
            @Override
            protected void positionCaret(MouseEvent e) {
                if (!e.isConsumed()) super.positionCaret(e);
            }
            @Override
            protected void moveCaret(MouseEvent e) {
                if (!e.isConsumed()) super.moveCaret(e);
            }
        });
        textPane.getCaret().setVisible(true);
        textPane.getCaret().setBlinkRate(500); // restore blinking caret

        textPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    private void setupPanel() {
        jPanel = new JPanel();
        jPanel.add(textPane);
    }

    private void setupMenu() {
        jMenuBar = new JMenuBar();

        // MenuBar
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu macroMenu = new JMenu("Macro");
        JMenu toolMenu = new JMenu("Tools");

        jMenuBar.add(fileMenu);
        jMenuBar.add(editMenu);
        jMenuBar.add(macroMenu);
        jMenuBar.add(toolMenu);

        editMenu.setMnemonic(KeyEvent.VK_E);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        macroMenu.setMnemonic(KeyEvent.VK_M);

        // File
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))){
                        StringBuilder stringBuilder = new StringBuilder();
                        String line = br.readLine();
                        while (line != null) {
                            stringBuilder.append(line);
                            line = br.readLine();
                            stringBuilder.append(System.getProperty("line.separator"));
                        }
                        char[] charArray = stringBuilder.toString().toCharArray();

                        List<Character> chars = new ArrayList<>();
                        for(Character c : charArray) {
                            chars.add(c);
                        }

                        Command openCommand = new OpenCommand(chars);
                        openCommand.execute(engine);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e ->{
            JFileChooser fileChooser = new JFileChooser();

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                Command saveCommand = new SaveCommand(selectedFile);
                saveCommand.execute(engine);
            }
        });

        openItem.setMnemonic(KeyEvent.VK_O);
        saveItem.setMnemonic(KeyEvent.VK_S);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        // Edit
        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem pasteItem = new JMenuItem("Paste");

        undoItem.setMnemonic(KeyEvent.VK_U);
        redoItem.setMnemonic(KeyEvent.VK_R);
        copyItem.setMnemonic(KeyEvent.VK_C);
        cutItem.setMnemonic(KeyEvent.VK_T);
        pasteItem.setMnemonic(KeyEvent.VK_P);

        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Command command = new CopyCommand();
                command.execute(engine);
            }
        });
        cutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Command command = new CutCommand();
                command.execute(engine);
            }
        });
        pasteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Command command = new PasteCommand();
                command.execute(engine);
            }
        });

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.add(copyItem);
        editMenu.add(cutItem);
        editMenu.add(pasteItem);

        // Macro
        JMenuItem startRecordItem = new JMenuItem("Start Recording");
        JMenuItem stopRecordItem = new JMenuItem("Stop Recording");
        JMenuItem replayRecordItem = new JMenuItem("Play Recording");

        startRecordItem.setMnemonic(KeyEvent.VK_S);
        stopRecordItem.setMnemonic(KeyEvent.VK_T);
        replayRecordItem.setMnemonic(KeyEvent.VK_P);

        startRecordItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Command command = new StartRecordingCommand();
                command.execute(engine);
            }
        });
        stopRecordItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Command command = new StopRecordingCommand();
                command.execute(engine);
            }
        });
        replayRecordItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Command command = new ReplayRecordingCommand();
                command.execute(engine);
            }
        });

        macroMenu.add(startRecordItem);
        macroMenu.add(stopRecordItem);
        macroMenu.add(replayRecordItem);

        JMenuItem spellCheckItem = new JMenuItem("Spell Check");

        spellCheckItem.addActionListener(e ->{
            Command command = new SpellCheckCommand();
            command.execute(engine);
        });

        toolMenu.add(spellCheckItem);
    }

    private void setupFrame() {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setJMenuBar(jMenuBar);
        frame.setContentPane(jPanel);
        frame.pack();
        frame.setLocationRelativeTo(null); // centers window on screen
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        textEditor.start();
    }

    public void updateText(String content) {
        textPane.setText(content);
    }

    public void updateCursor(int position) {
        textPane.setCaretPosition(position);
    }

    public void updateSelection(boolean active, int selectionBase, int selectionEnd) {
        if(active) {
            textPane.setCaretPosition(selectionBase);
            textPane.moveCaretPosition(selectionEnd);
        }
    }

    public void updateMisspelledWords(List<Selection> selections) {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setUnderline(attributeSet, true);

        Highlighter highlighter = textPane.getHighlighter();
        highlighter.removeAllHighlights();

        for(Selection selection : selections) {
            try {
                highlighter.addHighlight(
                        selection.getSelectionBase(),
                        selection.getSelectionEnd(),
                        redUnderliner
                );
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        /**
         * We can also use underline-attributes on a StyledDocument of the textPane.
         * However, this makes underlined text copyable and 'part of the content'
         * which is not desirable for a spell-check underlining.
         */
        /*selections.forEach(s -> textPane
                .getStyledDocument()
                .setCharacterAttributes(
                        s.getSelectionBase(),
                        s.getSelectionEnd() - s.getSelectionBase(),
                        attributeSet,
                        true)
        );*/
    }
}
