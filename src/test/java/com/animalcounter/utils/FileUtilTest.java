package com.animalcounter.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FileUtilTest {

    private static final String[] PATH_TO_TEST_TXT_FILE =
            {"src", "test", "resources", "testTextFile.txt"};

    private static String absolutePathToTestTxtFile;

    @BeforeAll
    public static void setup() {

        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(System.getProperty("user.dir"));
        pathBuilder.append(File.separator);
        pathBuilder.append(
                Arrays.stream(PATH_TO_TEST_TXT_FILE)
                      .map(e -> e + File.separator)
                      .collect(Collectors.joining())
        );

        absolutePathToTestTxtFile = pathBuilder.toString();
    }

    @Test
    public void should_read_file_with_one_page() {

        FileUtil fileUtil = new FileUtil(absolutePathToTestTxtFile);
        FileUtil.Page page = new FileUtil.Page(1, 5);

        StringBuilder readingResult = new StringBuilder();

        fileUtil.readFile(
                readingResult::append,
                page
        );

        Assertions.assertEquals("23456", readingResult.toString());
    }



}
