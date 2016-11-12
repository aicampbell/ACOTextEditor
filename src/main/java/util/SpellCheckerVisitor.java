package util;

import commands.interfaces.Visitor;
import engine.TextElement;

/**
 * Created by aidan on 12/11/16.
 */
public class SpellCheckerVisitor implements Visitor {

    private StringBuffer currentWord;


    public SpellCheckerVisitor() {
        this.currentWord = new StringBuffer();
    }

    @Override
    public void visitCharacter(TextElement textElement) {
        if (Character.isAlphabetic(textElement.getCharacter().charValue())
                || Character.isDigit(textElement.getCharacter().charValue())) {
            this.currentWord.append(textElement.getCharacter());
        } else {
//            this.spellCheck();
            System.out.println(currentWord);
            if (this.currentWord.length() > 0) {
                this.spellCheck();
            }
        }

    }

    private void spellCheck() {
        String word = this.currentWord.toString();
        if (!word.equals("") && SpellChecker.getInstance().isMisspelled(word)) {
            System.out.println("error at word: "+ word);
        }

        this.currentWord = new StringBuffer();

    }



}
