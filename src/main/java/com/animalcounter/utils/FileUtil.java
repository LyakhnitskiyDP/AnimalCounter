package com.animalcounter.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.function.Consumer;

public class FileUtil {

    private static final Logger log =
            LoggerFactory.getLogger(FileUtil.class);

    private final String pathToFile;

    public FileUtil(String pathToFile) {

        this.pathToFile = pathToFile;
    }

    public void readFile(Consumer<String> callback) {

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

    public void readFile(Consumer<String> callback, Page page) {

        try (
                FileReader fileReader = new FileReader(pathToFile);
                BufferedReader reader = new BufferedReader(fileReader)
        ) {

            String line;

            for (int i = 0; i < page.offSet; i++) reader.readLine();

            int lineCounter = 0;

            while ((line = reader.readLine()) != null && !pageIsFull(page, lineCounter)) {
                callback.accept(line);
                lineCounter++;
            }

        } catch (FileNotFoundException e) {

            log.error("Cannot find file with path: {}", pathToFile, e);
        } catch (IOException e) {

            log.error("Exception while IO operation", e);
        }
    }



    public int countLines() {

        int lines = 0;

        try (
                FileReader fileReader = new FileReader(pathToFile);
                LineNumberReader reader = new LineNumberReader(fileReader)
        ) {
            while (reader.readLine() != null);

            lines = reader.getLineNumber();
        } catch (FileNotFoundException e) {

            log.error("Cannot find file with path: {}", pathToFile, e);
        } catch (IOException e) {

            log.error("Exception while IO operation", e);
        }

        return lines;
    }

    public void writeToFile(String content) {

        try (
                FileWriter fileWriter = new FileWriter(pathToFile, true);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {

            writer.write(content);

        } catch (FileNotFoundException e) {

            log.error("Cannot find file with path: {}", pathToFile, e);
        } catch (IOException e) {

            log.error("Exception while IO operation", e);
        }

    }

    @RequiredArgsConstructor
    public static class Page {

        private final int offSet;
        private final int size;

    }

    private boolean pageIsFull(Page page, int lineCounter) {
        return !(lineCounter < page.size);
    }

}
