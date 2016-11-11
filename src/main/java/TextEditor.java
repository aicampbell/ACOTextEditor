import engine.Engine;
import listener.CursorListener;
import listener.MouseActionListener;
import util.EngineObserver;
import listener.KeyActionListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.TextAction;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Created by mo on 08.11.16.
 */
public class TextEditor implements EngineObserver {
    Engine engine;

    private JMenuBar jMenuBar;
    private JMenu jMenu;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;

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
        jMenuBar = new JMenuBar();
        jMenu = new JMenu("File");
        jMenu.setMnemonic(KeyEvent.VK_F);
        jMenuBar.add(jMenu);
        jMenuItem1 = new JMenuItem("Open");
        jMenu.add(jMenuItem1);
        jMenuItem2 = new JMenuItem("Save");
        jMenu.add(jMenuItem2);
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
