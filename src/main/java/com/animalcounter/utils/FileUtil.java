package com.animalcounter.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

public class FileUtil {

    public static void readFile(
            String pathToFile,
            Consumer<String> callback) {

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                callback.accept(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
