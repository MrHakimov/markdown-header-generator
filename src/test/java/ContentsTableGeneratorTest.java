import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ContentsTableGeneratorTest {
    private static final String INPUT_FILES_PREF = "src/test/java/inputs/";
    private static final String ACTUAL_FILES_PREF = "src/test/java/actual/";
    private static final String EXPECTED_FILES_PREF = "src/test/java/expected/";

    private static void checkContents(String actualFile, String expectedFile) {
        try {
            Iterator<String> actualIterator = Files.lines(Paths.get(actualFile)).iterator();
            Iterator<String> expectedIterator = Files.lines(Paths.get(expectedFile)).iterator();


            while (actualIterator.hasNext() && expectedIterator.hasNext()) {
                assertEquals(actualIterator.next(), expectedIterator.next());
            }

            assertTrue(!actualIterator.hasNext() && !expectedIterator.hasNext());
        } catch (IOException e) {
            System.err.printf("error occurred while processing files: %s and %s. %s%n", actualFile, expectedFile, e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Base tests")
    public void testAllCases() throws IOException {
        List<String> testNames = Files.list(Paths.get("src/test/java/inputs"))
                .filter(file -> !Files.isDirectory(file))
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());

        for (String testName : testNames) {
            ContentsTableGenerator generator = new ContentsTableGenerator();

            generator.generate(
                    INPUT_FILES_PREF + testName,
                    ACTUAL_FILES_PREF + testName,
                    true
            );

            checkContents(
                    ACTUAL_FILES_PREF + testName,
                    EXPECTED_FILES_PREF + testName
            );
        }
    }
}
