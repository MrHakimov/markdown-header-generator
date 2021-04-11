import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ContentsTableGenerator {
    // ------------------------------------------------------------------

    // Constants
    private static final int MIN_HEADER_LEVEL = 1;
    private static final int MAX_HEADER_LEVEL = 6;
    private static final String INDENTATION = "    ";

    private static final int TAB_SIZE = 4;

    private static final char DASH = '-';
    private static final char SHARP = '#';
    private static final char SPACE = ' ';
    private static final char EQUALITY = '=';
    private static final char NON_SPACE = '!'; // any non-space character
    private static final char UNDERSCORE = '_';

    // Last header level
    private int lastLevel = 0;

    // Potential header line for alternative header style
    private String potentialHeader = "";

    // Header numeration order by level
    private final int[] order = new int[MAX_HEADER_LEVEL + 1];

    // Number of repetitions of each header by content
    private final Map<String, Integer> repeats = new HashMap<>();

    // ------------------------------------------------------------------

    private static boolean isHeaderLevelValid(int level) {
        return MIN_HEADER_LEVEL <= level && level <= MAX_HEADER_LEVEL;
    }

    private static boolean isWordCharacter(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == UNDERSCORE || c == DASH;
    }

    private static String indentation(int level) {
        return INDENTATION.repeat((level - 1));
    }

    private static int countPrefix(String line, char c) {
        return (int) line.chars().takeWhile(s -> s == c).count();
    }

    private static boolean consistsOf(String line, char c) {
        line = line.strip();
        return countPrefix(line, c) == line.length();
    }

    private static void addToBuilder(StringBuilder builder, char c) {
        if (isWordCharacter(c)) {
            builder.append(Character.toLowerCase(c));
        } else if (c == SPACE) {
            builder.append(DASH);
        }
    }

    private static String toKebabCase(String line) {
        StringBuilder builder = new StringBuilder();

        line.chars().forEach(c -> addToBuilder(builder, (char) c));

        return builder.toString();
    }

    private static int alternativeHeaderLevel(String line) {
        if (line.isEmpty()) {
            return 0;
        }

        // alternative first level header
        if (consistsOf(line, EQUALITY)) {
            return 1;
        }

        // alternative second level header
        if (consistsOf(line, DASH)) {
            return 2;
        }

        return 0;
    }

    // ------------------------------------------------------------------

    private static void safeWrite(BufferedWriter writer, String line) {
        try {
            writer.write(line + String.format("%n"));
        } catch (IOException e) {
            System.err.printf("Unable to write to output file: %s%n", e.getMessage());
        }
    }

    // ------------------------------------------------------------------

    private String updateRepeats(String link) {
        Integer reps = repeats.get(link);
        if (reps != null) {
            repeats.put(link, reps + 1);
            link = link + DASH + reps;
        }

        repeats.put(link, 1);

        return link;
    }

    private void updateOrder(int level) {
        if (level < lastLevel) {
            Arrays.fill(order, level + 1, order.length, 0);
        }

        order[level]++;
    }

    private String headerToLink(String line, int level) {
        line = line.strip();
        updateOrder(level);

        String link = toKebabCase(line);
        link = updateRepeats(link);

        line = line.replaceAll(" +", " ");;
        lastLevel = level;

        return String.format("%s%d. [%s](#%s)", indentation(level), order[level], line, link);
    }

    // ------------------------------------------------------------------

    private void extractHeader(BufferedWriter writer, String line) {
        int level;
        line = line.replace("\t", INDENTATION).replace("\r", "");

        int spaces = countPrefix(line, SPACE);

        // if the number of spaces at the beginning
        // is greater or equal than 4, code snippet starts
        if (spaces >= TAB_SIZE) {
            return;
        }

        line = line.substring(spaces).strip();
        level = countPrefix(line, SHARP);

        if (isHeaderLevelValid(level)) {
            safeWrite(writer, headerToLink(line.substring(level), level));
        } else {
            level = alternativeHeaderLevel(line);

            if (isHeaderLevelValid(level) && !potentialHeader.isEmpty()) {
                // if potential header consists of '-' only, we need exactly "--"
                // to interpret it as a header, otherwise "-" - should be interpreted as a list element,
                // "---", "----", "-----", ... - as an <HR> HTML-tag
                if (consistsOf(potentialHeader, DASH) && countPrefix(potentialHeader, DASH) != 2) {
                    return;
                }

                safeWrite(writer, headerToLink(potentialHeader, level));
            } else {
                potentialHeader = line;
            }
        }
    }

    // ------------------------------------------------------------------

    public void generate(String inputFile, String outputFile, boolean isPrintToFile) {
        try (BufferedWriter writer = new BufferedWriter(
                isPrintToFile
                        ? new FileWriter(outputFile)
                        : new OutputStreamWriter(System.out))
        ) {
            Files.lines(Paths.get(inputFile)).forEach(line -> extractHeader(writer, line));
            writer.write(String.format("%n"));
            Files.lines(Paths.get(inputFile)).forEach(line -> safeWrite(writer, line));
        } catch (IOException e) {
            System.err.println("couldn't process output file: " + e.getMessage());
        }
    }
}
