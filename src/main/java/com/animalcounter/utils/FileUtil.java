package com.animalcounter.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FileUtil {

    public static void readFile(
            String pathToFile,
            Consumer<String> callback) {

        try (
                FileReader fileReader = new FileReader(pathToFile);
                BufferedReader reader = new BufferedReader(fileReader)
        ) {

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

    public static void writeToFile(String pathToFile, Supplier<String> supplier) {

        try (
                FileWriter fileWriter = new FileWriter(pathToFile);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {

            writer.write(supplier.get());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
