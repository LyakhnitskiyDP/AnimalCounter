package com.animalcounter.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class FileUtil {

    private final String pathToFile;

    public void readFile(Consumer<String> callback) {

        try (Stream<String> lines = getLineStream()) {

            lines.forEach(callback);

        } catch (IOException e) {
            log.error("Exception while IO operation", e);
        }

    }

    public void readFile(Consumer<String> callback, Page page) {

        try (Stream<String> lines = getLineStream()) {

            lines.skip(page.offSet)
                 .limit(page.size)
                 .forEach(callback);

        } catch (IOException e) {
            log.error("Exception while IO operation", e);
        }
    }

    public int countLines() {

        try (Stream<String> lines = getLineStream()) {

            return (int) lines.count();

        } catch (IOException e) {
            log.error("Exception while IO operation", e);
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(String content) {

        Path pathToFile = Paths.get(this.pathToFile);

        try (
                FileWriter fileWriter = new FileWriter(pathToFile.toFile(), true);
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ) {

            writer.write(content);

        } catch (FileNotFoundException e) {

            log.error("Cannot find file with path: {}", pathToFile, e);
            throw new RuntimeException(e);
        } catch (IOException e) {

            log.error("Exception while IO operation", e);
            throw new RuntimeException(e);
        }

    }

    public Stream<String> getLineStream() throws IOException{

        Path pathToFile = Paths.get(this.pathToFile);

        return Files.lines(pathToFile);
    }

    @RequiredArgsConstructor
    public static class Page {

        private final int offSet;
        private final int size;

    }

}
