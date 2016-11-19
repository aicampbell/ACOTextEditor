package ui;

import javax.swing.text.*;
import java.awt.*;

/**
 * This class implements a custom visual effect that is used to mark misspelled words with
 * a specifically colored underline.
 *
 * @see <a href="http://www.java2s.com/Code/Java/Swing-JFC/JTextPaneHighlightExample.htm">JTextPaneHightlight example</a>
 */
public class Underliner extends LayeredHighlighter.LayerPainter {
    /** Color to be used for the underline. */
    private Color color;

    public Underliner(Color c) {
        color = c;
    }

    public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view) {
        g.setColor(color == null ? c.getSelectionColor() : color);

        Rectangle alloc;
        if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
            if (bounds instanceof Rectangle) {
                alloc = (Rectangle) bounds;
            } else {
                alloc = bounds.getBounds();
            }
        } else {
            try {
                Shape shape = view.modelToView(offs0,
                        Position.Bias.Forward, offs1,
                        Position.Bias.Backward, bounds);
                alloc = (shape instanceof Rectangle) ? (Rectangle) shape
                        : shape.getBounds();
            } catch (BadLocationException e) {
                return null;
            }
        }

        FontMetrics fm = c.getFontMetrics(c.getFont());
        int baseline = alloc.y + alloc.height - fm.getDescent() + 1;
        g.drawLine(alloc.x, baseline, alloc.x + alloc.width, baseline);
        g.drawLine(alloc.x, baseline + 1, alloc.x + alloc.width,
                baseline + 1);

        return alloc;
    }

    @Override
    public void paint(Graphics graphics, int i, int i1, Shape shape, JTextComponent jTextComponent) {
    }
}
