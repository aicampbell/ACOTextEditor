import commands.Command;
import commands.CopyCommand;
import commands.PasteCommand;
import engine.Buffer;
import engine.Engine;
import listener.MouseActionListener;
import util.EngineObserver;
import listener.KeyActionListener;

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
    private JMenu fileMenu;

    private JSplitPane jSplitPane;

    private JTextPane textPaneLeft;
    private JTextPane textPaneRight;

    public TextEditor() {
        engine = new Engine();
    }

    public void start(){
        engine.registerObserver(this);

        setupMenu();
        setupTextPanes();
        setupSplitPane();
        setupFrame();

        textPaneLeft.requestFocus();
    }

    private void setupTextPanes() {
        textPaneLeft = new JTextPane();

        ActionMap am = textPaneLeft.getActionMap();
        textPaneLeft.getActionMap().put(DefaultEditorKit.selectWordAction,
                new TextAction(DefaultEditorKit.selectWordAction) {
                    public void actionPerformed(ActionEvent e) {
                        // DO NOTHING
                    }
                });
        am.clear();
        InputMap im = textPaneLeft.getInputMap();
        im.clear();


        textPaneLeft.addKeyListener(new KeyActionListener(engine, textPaneLeft));
        //textPaneLeft.addCaretListener(new CursorListener(engine));
        MouseActionListener mouseActionListener = new MouseActionListener(engine, textPaneLeft);
        textPaneLeft.addMouseListener(mouseActionListener);
        textPaneLeft.addMouseMotionListener(mouseActionListener);

        // Dirty hack to disable default event propagation. With that only our previously added listeners
        // receive events.
        textPaneLeft.setCaret(new DefaultCaret() {
            @Override
            protected void positionCaret(MouseEvent e) {
                if (!e.isConsumed()) super.positionCaret(e);
            }
            @Override
            protected void moveCaret(MouseEvent e) {
                if (!e.isConsumed()) super.moveCaret(e);
            }
        });
        textPaneLeft.getCaret().setVisible(true);
        textPaneLeft.getCaret().setBlinkRate(500); // restore blinking caret

        textPaneLeft.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    private void setupSplitPane() {
        jSplitPane = new JSplitPane();
        jSplitPane.setPreferredSize(new Dimension(768, 512));
        jSplitPane.setLeftComponent(textPaneLeft);
        jSplitPane.setRightComponent(textPaneRight);
        jSplitPane.setResizeWeight(0.5d);
        jSplitPane.setEnabled(false);
    }

    private void setupMenu() {

        //Setup Menu Bar
        jMenuBar = new JMenuBar();



        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenu editMenu = new JMenu("Edit");
        JMenu macroMenu = new JMenu("Macro");

        //File

        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");

        saveItem.setMnemonic(KeyEvent.VK_S);

//        openItem.addActionListener();
        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        //Edit
        JMenuItem redoItem = new JMenuItem("Redo");
        JMenuItem undoItem = new JMenuItem("undo");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem insertItem = new JMenuItem("Insert");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem pasteItem = new JMenuItem("Paste");


        copyItem.setMnemonic(KeyEvent.VK_C);
        cutItem.setMnemonic(KeyEvent.VK_X);
        pasteItem.setMnemonic(KeyEvent.VK_V);

        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Command command = new CopyCommand();
                command.execute(engine);
            }
        });

        pasteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Command command = new PasteCommand();
                command.execute(engine);
            }
        });
        editMenu.add(redoItem);
        editMenu.add(undoItem);
        editMenu.add(copyItem);
        editMenu.add(cutItem);
        editMenu.add(insertItem);
        editMenu.add(deleteItem);
        editMenu.add(pasteItem);

        //Macro
        JMenuItem startRecordItem = new JMenuItem("Start Record");
        JMenuItem stopRecordItem = new JMenuItem("Stop Record");
        JMenuItem playItem = new JMenuItem("Play");

        macroMenu.add(startRecordItem);
        macroMenu.add(stopRecordItem);
        macroMenu.add(playItem);

        //MenuBar
        jMenuBar.add(fileMenu);
        jMenuBar.add(editMenu);
        jMenuBar.add(macroMenu);

    }

    private void setupFrame() {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setJMenuBar(jMenuBar);
        frame.setContentPane(jSplitPane);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // centers window on screen
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        textEditor.start();
    }

    public void updateText(String content) {
        textPaneLeft.setText(content);
    }

    public void updateCursor(int position) {
        textPaneLeft.setCaretPosition(position);
    }

    public void updateSelection(boolean active, int selectionBase, int selectionEnd) {
        textPaneLeft.setCaretPosition(selectionBase);
        textPaneLeft.moveCaretPosition(selectionEnd);
        //textPaneLeft.setSelectionStart(start);
        //textPaneLeft.setSelectionEnd(end);
    }
}
