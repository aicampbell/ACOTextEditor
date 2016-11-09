import engine.Engine;
import listener.CursorListener;
import util.EngineObserver;
import listener.KeyboardListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

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
        setupMenu();
        setupTextPanes();
        setupSplitPane();
        setupFrame();
    }

    private void setupTextPanes() {
        textPaneLeft = new JTextPane();
        textPaneRight = new JTextPane();

        // TODO: make sure we can update text, cursor and selection in outer-right textPane
        textPaneRight.setEnabled(false);

        textPaneLeft.addKeyListener(new KeyboardListener(engine));
        textPaneLeft.addCaretListener(new CursorListener(engine));
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
        frame.setJMenuBar(jMenuBar);
        frame.setContentPane(jSplitPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // centers window on screen
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        textEditor.start();
    }

    public void updateText(String content) {
        textPaneRight.setText(content);
    }

    public void updateCursor(int position) {
        textPaneRight.setCaretPosition(position);
    }

    public void updateSelection(boolean active, int start, int end) {
        textPaneRight.setSelectionStart(start);
        textPaneRight.setSelectionStart(end);
    }
}
