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
        if (Character.isAlphabetic(textElement.getCharacter())
                || Character.isDigit(textElement.getCharacter())) {
            currentWord.append(textElement.getCharacter());
        } else {
            if (currentWord.length() > 0) {
                spellCheck();
            }
        }
    }

    private void spellCheck() {
        String word = currentWord.toString();
        if (!word.equals("") &&
                SpellChecker.getInstance().isMisspelled(word)) {
            System.out.println("error at word: " + word);
        }
        currentWord = new StringBuffer();
    }
}
