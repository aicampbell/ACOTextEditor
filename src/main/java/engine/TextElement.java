package engine;

import commands.interfaces.Visitor;

/**
 * Created by aidan on 12/11/16.
 */
public class TextElement {
    Character character;

    public TextElement(char c){
        character = c;
    }

    public void accept(Visitor visitor){

    }
}
