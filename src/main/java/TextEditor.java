import commands.CopyCommand;
import commands.CutCommand;
import commands.PasteCommand;
import commands.interfaces.Command;
import engine.Engine;
import listener.KeyActionListener;
import listener.MouseActionListener;
import util.EngineObserver;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.TextAction;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Created by mo on 08.11.16.
 */
public class TextEditor implements EngineObserver {
    Engine engine;

    private JMenuBar jMenuBar;
    private JPanel jPanel;
    private JTextPane textPane;

    public TextEditor() {
        engine = new Engine();
        //undoModule = new UndoModule();
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
        // receive events.
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

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        JMenu macroMenu = new JMenu("Macro");
        macroMenu.setMnemonic(KeyEvent.VK_M);

        // File
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setMnemonic(KeyEvent.VK_O);
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setMnemonic(KeyEvent.VK_S);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        // Edit
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic(KeyEvent.VK_U);
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.setMnemonic(KeyEvent.VK_R);

        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setMnemonic(KeyEvent.VK_C);
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.setMnemonic(KeyEvent.VK_T);
        JMenuItem pasteItem = new JMenuItem("Paste");
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
        startRecordItem.setMnemonic(KeyEvent.VK_S);
        JMenuItem stopRecordItem = new JMenuItem("Stop Recording");
        stopRecordItem.setMnemonic(KeyEvent.VK_T);
        JMenuItem playItem = new JMenuItem("Play Recording");
        playItem.setMnemonic(KeyEvent.VK_P);
        macroMenu.add(startRecordItem);
        macroMenu.add(stopRecordItem);
        macroMenu.add(playItem);

        // MenuBar
        jMenuBar.add(fileMenu);
        jMenuBar.add(editMenu);
        jMenuBar.add(macroMenu);
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
        textPane.setCaretPosition(selectionBase);
        textPane.moveCaretPosition(selectionEnd);
    }
}
