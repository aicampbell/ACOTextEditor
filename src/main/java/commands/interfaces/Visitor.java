package commands.interfaces;

import engine.TextElement;

/**
 * Created by aidan on 12/11/16.
 */
public interface Visitor {

     void visitCharacter(TextElement textElement);
}
