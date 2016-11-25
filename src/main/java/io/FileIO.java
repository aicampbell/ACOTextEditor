package io;

import engine.Buffer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * This utility class provides helper methods for IO operations with the filesystem.
 */
public class FileIO {
    /**
     * Reads a File object and returns the file contents of specified file.
     *
     * @param file File to be read
     * @return the contents of the file as List<Character>
     * @throws IOException if something went wrong during reading the file.
     */
    public static List<Character> getContentsOfFile(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = br.readLine();
                stringBuilder.append(System.getProperty("line.separator"));
            }
            char[] charArray = stringBuilder.toString().toCharArray();

            List<Character> chars = new ArrayList<>();
            for (Character c : charArray) {
                chars.add(c);
            }

            return chars;
        }
    }

    /**
     * Writes content to a file.
     *
     * @param file File to which content should be saved to
     * @param chars to be saved. Type is the data structure of {@link Buffer#getContent()}.
     * @throws IOException if something went wrong during saving the contents to the file.
     */
    public static void saveContentToFile(File file, List<Character> chars) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file)))) {
            for (Character character : chars) {
                writer.write(character);
            }
        }
    }
}
