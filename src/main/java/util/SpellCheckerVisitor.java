package util;

import commands.interfaces.Visitor;

/**
 * Created by aidan on 12/11/16.
 */
public class SpellCheckerVisitor implements Visitor {

    private StringBuffer currentWord;


    public SpellCheckerVisitor() {
        this.currentWord = new StringBuffer();
    }

    @Override
    public void visitCharacter(Character character) {
        if (Character.isAlphabetic(character.charValue())
                || Character.isDigit(character.charValue())) {
            this.currentWord.append(character);
        } else {
            this.spellCheck();
        }
    }

    private void spellCheck() {
        String word = this.currentWord.toString();
        if (!word.equals("") && SpellChecker.getInstance().isMisspelled(word)) {
            System.out.println("erroe");
        }

        this.currentWord = new StringBuffer();

    }



}
