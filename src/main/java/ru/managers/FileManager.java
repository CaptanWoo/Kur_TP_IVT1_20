package ru.managers;

import java.io.*;

public class FileManager {

    public static boolean isFileExists(String filePath) {
        if (filePath == null) return false;
        File file = new File(filePath);
        return file.exists();
    }

    public static void createFileIfNotExists(String filePath) {
        if (!FileManager.isFileExists(filePath)) {
            try(BufferedWriter ignored = new BufferedWriter(new FileWriter(filePath))) {} catch (IOException ignored) {}
        }
    }


}
