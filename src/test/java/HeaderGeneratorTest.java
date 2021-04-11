import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeaderGeneratorTest {
    private static final String INPUT_FILES_PREF = "src/test/java/inputs/";
    private static final String ACTUAL_FILES_PREF = "src/test/java/actual/";
    private static final String EXPECTED_FILES_PREF = "src/test/java/expected/";

    private static void checkContents(String actualFile, String expectedFile) {
        try {
            String actual = new String(Files.readAllBytes(Paths.get(actualFile)));
            String expected = new String(Files.readAllBytes(Paths.get(expectedFile)));

            assertEquals(actual, expected);
        } catch (IOException e) {
            System.err.printf("error occurred while processing files: %s and %s. %s%n", actualFile, expectedFile, e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Sample test")
    public void testSample() {
        HeaderGenerator generator = new HeaderGenerator();
        String fileName = "sample.md";

        generator.generate(
                INPUT_FILES_PREF + fileName,
                ACTUAL_FILES_PREF + fileName,
                true
        );

        checkContents(
                ACTUAL_FILES_PREF + fileName,
                EXPECTED_FILES_PREF + fileName
        );
    }

    @Test
    @DisplayName("Alternative header style test")
    public void testAlternativeHeaderStyle() {
        HeaderGenerator generator = new HeaderGenerator();
        String fileName = "alternative_header_style.md";

        generator.generate(
                INPUT_FILES_PREF + fileName,
                ACTUAL_FILES_PREF + fileName,
                true
        );

        checkContents(
                ACTUAL_FILES_PREF + fileName,
                EXPECTED_FILES_PREF + fileName
        );
    }

    @Test
    @DisplayName("Repetitions simple test")
    public void testRepetitions() {
        HeaderGenerator generator = new HeaderGenerator();
        String fileName = "repetitions.md";

        generator.generate(
                INPUT_FILES_PREF + fileName,
                ACTUAL_FILES_PREF + fileName,
                true
        );

        checkContents(
                ACTUAL_FILES_PREF + fileName,
                EXPECTED_FILES_PREF + fileName
        );
    }

    @Test
    @DisplayName("Spaces simple test")
    public void testSpaces() {
        HeaderGenerator generator = new HeaderGenerator();
        String fileName = "spaces.md";

        generator.generate(
                INPUT_FILES_PREF + fileName,
                ACTUAL_FILES_PREF + fileName,
                true
        );

        checkContents(
                ACTUAL_FILES_PREF + fileName,
                EXPECTED_FILES_PREF + fileName
        );
    }

    @Test
    @DisplayName("Normal test")
    public void testNormal() {
        HeaderGenerator generator = new HeaderGenerator();
        String fileName = "normal.md";

        generator.generate(
                INPUT_FILES_PREF + fileName,
                ACTUAL_FILES_PREF + fileName,
                true
        );

        checkContents(
                ACTUAL_FILES_PREF + fileName,
                EXPECTED_FILES_PREF + fileName
        );
    }

    @Test
    @DisplayName("Special alternative test")
    public void testSpecialAlternative() {
        HeaderGenerator generator = new HeaderGenerator();
        String fileName = "alternative_special.md";

        generator.generate(
                INPUT_FILES_PREF + fileName,
                ACTUAL_FILES_PREF + fileName,
                true
        );

        checkContents(
                ACTUAL_FILES_PREF + fileName,
                EXPECTED_FILES_PREF + fileName
        );
    }

    @Test
    @DisplayName("Strong repeats test")
    public void testStrongRepeats() {
        HeaderGenerator generator = new HeaderGenerator();
        String fileName = "repeats_strong.md";

        generator.generate(
                INPUT_FILES_PREF + fileName,
                ACTUAL_FILES_PREF + fileName,
                true
        );

        checkContents(
                ACTUAL_FILES_PREF + fileName,
                EXPECTED_FILES_PREF + fileName
        );
    }

    @Test
    @DisplayName("Special characters test")
    public void testSpecialCharacters() {
        HeaderGenerator generator = new HeaderGenerator();
        String fileName = "special_characters.md";

        generator.generate(
                INPUT_FILES_PREF + fileName,
                ACTUAL_FILES_PREF + fileName,
                true
        );

        checkContents(
                ACTUAL_FILES_PREF + fileName,
                EXPECTED_FILES_PREF + fileName
        );
    }
}
