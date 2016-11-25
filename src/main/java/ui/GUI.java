package ui;

import commands.*;
import commands.Command;
import engine.Engine;
import engine.Selection;
import io.FileIO;
import listener.KeyActionListener;
import listener.MouseActionListener;
import engine.EngineObserver;

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
 * This class represents the GUI (frontend) part of the text editor. In order to
 * function properly it needs to know an instance of Engine which is used as backend.
 */
public class GUI implements EngineObserver {
    private Engine engine;

    private JMenuBar jMenuBar;
    private JScrollPane jScrollpane;
    private JTextPane textPane;

    private Underliner redUnderliner;

    /** Constructor in which {@link GUI#redUnderliner} is initialized for the spell checker. */
    public GUI() {
        redUnderliner = new Underliner(Color.RED);
    }

    /**
     * Sets the Engine to which the GUI sends messages via Commands.
     *
     * @param engine instance to be used.
     * @return the GUI instance to allow a fluent API.
     */
    public GUI setEngine(Engine engine) {
        this.engine = engine;
        engine.registerObserver(this);

        return this;
    }

    /**
     * Set up the GUI (menu, text pane and structural components as panel
     * and frame).
     */
    public void start(){
        setupMenu();
        setupTextPanes();
        setupPanel();
        setupFrame();

        /**
         * Explicitly set the focus on the {@link GUI#textPane} so that the user can start
         * typing immediately.
         */
        textPane.requestFocus();
    }

    /**
     * Set up the text pane which is the heart of the GUI.
     */
    private void setupTextPanes() {
        textPane = new JTextPane();
        textPane.setPreferredSize(new Dimension(768, 512));

        /**
         * Remove default action and input map of {@link GUI#textPane} to disable
         * potential default behaviours.
         */
        ActionMap am = textPane.getActionMap();
        textPane.getActionMap().put(DefaultEditorKit.selectWordAction,
                new TextAction(DefaultEditorKit.selectWordAction) {
                    public void actionPerformed(ActionEvent e) {
                        // Do nothing
                    }
                });
        am.clear();
        InputMap im = textPane.getInputMap();
        im.clear();

        /** Set a key listener. */
        textPane.addKeyListener(new KeyActionListener(
                textPane,
                engine
        ));

        /**
         * Set a mouse listener. In order to support click and drag events,
         * our listener must be added as regular {@code MouseListener} and as
         * {@code MouseMotionListener}.
         */
        MouseActionListener mouseActionListener = new MouseActionListener(
                textPane,
                engine
        );
        textPane.addMouseListener(mouseActionListener);
        textPane.addMouseMotionListener(mouseActionListener);

        /**
         * Workaround to disable the default event propagation. With this in place, only our
         * previously added key and mouse listeners receive events which is a desired situation.
         */
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

        /**
         * Sets the cursor to an appropriate text cursor.
         * Is not working in every environment though.
         */
        textPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    /**
     * Set up panel which contains {@link GUI#textPane}.
     */
    private void setupPanel() {
        jScrollpane = new JScrollPane(textPane);
    }

    /**
     * Set up the top menu and its clickable items as well as the actions to be run when
     * an item is clicked.
     */
    private void setupMenu() {
        jMenuBar = new JMenuBar();

        /** Definition of our top level menu items */
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

        /** FILE menu dropdown items */
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    try {
                        List<Character> chars = FileIO.getContentsOfFile(selectedFile);
                        Command openCommand = new OpenCommand(chars);
                        openCommand.execute(engine);
                    }  catch (IOException e) {
                        System.out.println("Error while opening a file.");
                        e.printStackTrace();
                    }
                }
            }
        });

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();

                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    Command saveCommand = new SaveCommand(selectedFile);
                    saveCommand.execute(engine);
                }
            }
        });

        openItem.setMnemonic(KeyEvent.VK_O);
        saveItem.setMnemonic(KeyEvent.VK_S);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        /** EDIT menu dropdown items */
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

        undoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Command command = new UndoCommand();
                command.execute(engine);
            }
        });
        redoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Command command = new RedoCommand();
                command.execute(engine);
            }
        });
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

        /** MACRO menu dropdown items */
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

        /** SPELLCHECK menu dropdown item */
        JMenuItem spellCheckItem = new JMenuItem("Spell Check");

        spellCheckItem.addActionListener(e ->{
            Command command = new SpellCheckCommand();
            command.execute(engine);
        });

        toolMenu.add(spellCheckItem);
    }

    /**
     * Set up the root graphical component in the GUI which contains
     * a panel which contains the text pane.
     */
    private void setupFrame() {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setJMenuBar(jMenuBar);
        frame.setContentPane(jScrollpane);
        frame.pack();
        frame.setLocationRelativeTo(null); // centers window on screen
        frame.setVisible(true);
    }

    /**
     * Updates the UI by assigning the passed text content to the content of
     * the {@link GUI#textPane} object.
     *
     * @param content new text content
     */
    public void updateText(String content) {
        textPane.setText(content);
    }

    /**
     * Updates the UI by setting the cursor to the passed position of the text.
     *
     * @param position new cursor position
     */
    public void updateCursor(int position) {
        textPane.setCaretPosition(position);
    }

    /**
     * Updates the UI by visualizing the passed selection instance.
     *
     * @param active    boolean specifying if selection is active
     * @param selection object of the selection
     */
    public void updateSelection(boolean active, Selection selection) {
        if(active) {
            textPane.setCaretPosition(selection.getSelectionBase());
            textPane.moveCaretPosition(selection.getSelectionEnd());
        }
    }

    /**
     * Updates the UI by marking the words that are contained in the passed
     * list of selections.
     *
     * @param selections list of text selections in which words are represented
     */
    public void updateMisspelledWords(List<Selection> selections) {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setUnderline(attributeSet, true);

        /**
         * We can also use underline-attributes on a StyledDocument of {@link GUI#textPane}.
         * However, this makes underlined text copyable and 'part of the content'
         * which is not desirable for a spell-check underlining. This is why we use
         * the Highlighter object here.
         */
        Highlighter highlighter = textPane.getHighlighter();
        highlighter.removeAllHighlights();

        /** Highlight every word which's position is given in a Selection object. */
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
    }
}
