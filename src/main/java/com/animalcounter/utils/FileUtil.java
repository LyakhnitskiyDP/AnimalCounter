package com.animalcounter.utils;

import java.io.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    public static void writeToFile(String pathToFile, Supplier<String> supplier) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile))) {

            writer.write(supplier.get());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
