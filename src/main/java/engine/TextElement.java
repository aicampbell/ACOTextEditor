package engine;

/**
 * Created by aidan on 12/11/16.
 */
public class TextElement {
    private Character character;

    public TextElement(char c){
        character = c;
    }

    public char getChar(){
        return character;
    }

    @Override
    public String toString() {
        return String.valueOf(character);
    }
}
