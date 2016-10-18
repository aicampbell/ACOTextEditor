package engine;

import commands.DeleteCommand;

    /**
     * Created by Aidan on 10/10/2016.
     */
    public class Engine implements EngineI
    {
        private CommandHistory commandHistory;
        private Buffer buffer;
        private Buffer clipboard;

        private int cursorPosition=0;
        private int selectionStart=0;
        private int selectionEnd=0;

        public Engine() {
            commandHistory = new CommandHistory(this);
            buffer = new Buffer();
        }

        /**
         * Gets the new position of cursor and updates the cursor position. It takes the LineBreak as one character
         * and increment position in each line break by 1
         * so first character in the second line of the text has the position of: (number of character in first line + 1)
         * position 0 is the place before first character
         * @param position is the new position that this function will put the cursor on
         *                 the position should be less than buffer length
         */
        public void updateCursorPosition(int position) {

            int size = buffer.getSize();
            if(position>=0 && position<=size)
            {
                cursorPosition = position;
            }

        }

        public void insertChar(String character) {
            //TODO: implementation needed
            /*
            if(already selected text)
            {
                first delete selection

            }*/
            buffer.insert(character, cursorPosition);
            cursorPosition++;
        }

        public void deleteInDirection(int delDirection) {

            //TODO: implementation needed
            /*
            if(already selected text)
            {
                 delete selection

            }
            else: */
            if (delDirection == DeleteCommand.DEL_FORWARDS) {
                // "backspace" key was used - cursorPosition will not change
                if(cursorPosition<buffer.getSize())
                {
                    buffer.delete(cursorPosition,cursorPosition+1);
                }
            } else if (delDirection == DeleteCommand.DEL_BACKWARDS) {
                // "backspace" key was used
                if(cursorPosition>0)
                {
                    buffer.delete(cursorPosition-1,cursorPosition);
                    cursorPosition--;
                }
            }
        }

        /**
         * assumes that selectionStart is less than selectinEnd
         */
        public void cutCurrentSelection() {
            clipboard = new Buffer(buffer.copy(selectionStart, selectionEnd));
            buffer.delete(selectionStart,selectionEnd);
            cursorPosition = selectionStart;
        }

        /**
         * assumes that selectionStart is less than selectinEnd
         */
        public void copyCurrentSelection() {
            clipboard = new Buffer(buffer.copy(selectionStart, selectionEnd));
            cursorPosition = selectionStart;

        }

        public void pasteCurrentSelection() {

            //TODO: implementation needed
            /*
            if(already selected text)
            {
                first delete selection

            }*/
            if(clipboard!=null)
            {
                buffer.insert(clipboard, cursorPosition);
                cursorPosition+=clipboard.getSize();
            }
        }

        public void undoCommand() {

        }

        public void redoCommand() {

        }
    }
