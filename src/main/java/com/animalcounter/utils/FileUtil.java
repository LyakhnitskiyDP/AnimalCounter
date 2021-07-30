package com.animalcounter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FileUtil {

    private static final Logger log =
            LoggerFactory.getLogger(FileUtil.class);

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

            log.error("Cannot find file with path: {}", pathToFile, e);
        } catch (IOException e) {

            log.error("Exception while IO operation", e);
        }
    }

    public static void writeToFile(String pathToFile, String content) {

        try (
                FileWriter fileWriter = new FileWriter(pathToFile);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {

            writer.write(content);

        } catch (FileNotFoundException e) {

            log.error("Cannot find file with path: {}", pathToFile, e);
        } catch (IOException e) {

            log.error("Exception while IO operation", e);
        }

    }

}
