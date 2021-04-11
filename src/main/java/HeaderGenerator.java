import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class HeaderGenerator {
    // ------------------------------------------------------------------

    // Constants
    private static final int MIN_HEADERS = 1;
    private static final int MAX_HEADERS = 6;
    private static final String INDENTATION = "    ";

    // Last header level
    private int lastLevel = 0;

    // Potential header line for alternative header style
    private String potentialHeader = "";

    // Header numeration order by level
    private final int[] order = new int[MAX_HEADERS + 1];

    // Number of repetitions of each header by content
    private final Map<String, Integer> repeats = new HashMap<>();

    // ------------------------------------------------------------------

    private static boolean isHeaderLevelValid(int level) {
        return MIN_HEADERS <= level && level <= MAX_HEADERS;
    }

    private static boolean isWordCharacter(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '-';
    }

    private static String indentation(int level) {
        return INDENTATION.repeat((level - 1));
    }

    private static boolean consistsOf(String line, char c) {
        line = line.strip();
        return countPrefix(line, c) == line.length();
    }

    private static int countPrefix(String line, char c) {
        int i = 0;
        int cnt = 0;

        while (i < line.length() && line.charAt(i++) == c) {
            cnt++;
        }

        return cnt;
    }

    private static String removeRepeatingSpaces(String line) {
        StringBuilder builder = new StringBuilder();
        char last = '!'; // any non-space character

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (last != ' ' || c != ' ') {
                builder.append(c);
            }

            last = line.charAt(i);
        }

        return builder.toString();
    }

    private static String toKebabCase(String line) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (isWordCharacter(c)) {
                builder.append(Character.toLowerCase(c));
            } else if (c == ' ') {
                builder.append('-');
            }
        }

        return builder.toString();
    }

    private static int alternativeHeaderLevel(String line) {
        if (line.isEmpty()) {
            return 0;
        }

        // alternative first level header
        if (consistsOf(line, '=')) {
            return 1;
        }

        // alternative second level header
        if (consistsOf(line, '-')) {
            return 2;
        }

        return 0;
    }

    // ------------------------------------------------------------------

    private static void safeWrite(BufferedWriter writer, String line) {
        try {
            writer.write(line + '\n');
        } catch (IOException e) {
            System.err.printf("Unable to write to output file: %s\n", e.getMessage());
        }
    }

    // ------------------------------------------------------------------

    private String updateRepeats(String link) {
        int reps = repeats.getOrDefault(link, -1);
        if (reps != -1) {
            repeats.put(link, reps + 1);
            link = link + "-" + reps;
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

        line = removeRepeatingSpaces(line);
        lastLevel = level;

        return String.format("%s%d. [%s](#%s)", indentation(level), order[level], line, link);
    }

    // ------------------------------------------------------------------

    private void extractHeader(BufferedWriter writer, String line) {
        int level;
        line = line.replace("\t", INDENTATION).replace("\r", "");

        int spaces = countPrefix(line, ' ');

        // if the number of spaces at the beginning
        // is greater or equal than 4, code snippet starts
        if (spaces >= 4) {
            return;
        }

        line = line.substring(spaces).strip();
        level = countPrefix(line, '#');

        if (isHeaderLevelValid(level)) {
            safeWrite(writer, headerToLink(line.substring(level), level));
        } else {
            level = alternativeHeaderLevel(line);

            if (isHeaderLevelValid(level) && !potentialHeader.isEmpty()) {
                // if potential header consists of '-' only, we need exactly "--"
                // to interpret it as a header, otherwise "-" - should be interpreted as a list element,
                // "---", "----", "-----", ... - as an <HR> HTML-tag
                if (consistsOf(potentialHeader, '-')) {
                    if (countPrefix(potentialHeader, '-') == 2) {
                        safeWrite(writer, headerToLink(potentialHeader, level));
                    }
                } else {
                    safeWrite(writer, headerToLink(potentialHeader, level));
                }
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
            writer.write("\n");
            Files.lines(Paths.get(inputFile)).forEach(line -> safeWrite(writer, line));
        } catch (IOException e) {
            System.err.println("couldn't process output file: " + e.getMessage());
        }
    }
}
