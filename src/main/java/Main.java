import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    // TODO
    // 1. title limit constant
    // 2. indentation constant
    // 3. stack for levels
    // 4. streams for file printing
    // 5. accept file as an arg
    // 6. use format in headerToLink
    // 7. tests
    private static final int[] cnt = new int[7];
    private static final Map<String, Integer> repeats = new HashMap<>();
    private static int lastLevel = 0;

    private static int countChar(String line, char c) {
        int i = 0;
        int cnt = 0;

        while (i < line.length() && line.charAt(i++) == c) {
            cnt++;
        }

        return cnt;
    }

    private static int alternativeHeader(String line) {
        if (line.isEmpty()) {
            return 0;
        }

        // заголовок первого уровня
        int equalities = countChar(line, '=');
        if (equalities == line.length() - countChar(line.substring(equalities), ' ')) {
            return 1;
        }

        // заголовок второго уровня
        int dashes = countChar(line, '-');
        if (dashes == line.length() - countChar(line.substring(dashes), ' ')) {
            return 2;
        }

        return 0;
    }

    private static String indentation(int level) {
        return " ".repeat((level - 1) * 4);
    }

    private static void refreshCnt(int level) {
        for (int i = level; i < 7; i++) {
            cnt[i] = 0;
        }
    }

    private static String headerToLink(String line, int level) {
        if (level < lastLevel) {
            refreshCnt(level + 1);
        }

        lastLevel = level;
        cnt[level]++;

        StringBuilder builder = new StringBuilder();
        char last = '!';
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_') {
                builder.append(Character.toLowerCase(c));
                last = c;
            } else if (Character.isWhitespace(c) && !Character.isWhitespace(last)) {
                builder.append('-');
                last = ' ';
            }
        }

        String link = builder.toString();
        int reps = repeats.getOrDefault(link, -1);
        if (reps == -1) {
            repeats.put(link, 1);
        } else {
            repeats.put(link, reps + 1);
            link = link + "-" + reps;
            repeats.put(link, 1);
        }

        return indentation(level) + cnt[level] + ". " + "[" + line + "]" + "(#" + link + ")";
    }

    public static void main(String[] args) {
        try (FileReader file = new FileReader("src/main/inputs/test4.md")) {
            BufferedReader reader = new BufferedReader(file);

            String line;
            String potentialHeader = "";
            while ((line = reader.readLine()) != null) {
                int spaces = countChar(line, ' ');

                // после 4 пробелов начинается код-сниппет
                if (spaces < 4) {
                    line = line.substring(spaces);
                    int sharps = countChar(line, '#');
                    if (1 <= sharps && sharps <= 6) {
                        line = line.substring(sharps).trim().strip();

                        System.out.println(headerToLink(line, sharps));
                    } else {
                        int header = alternativeHeader(line);

                        if (header != 0 && !potentialHeader.isEmpty()) {
                            System.out.println(headerToLink(potentialHeader, header));
                        } else {
                            potentialHeader = line.trim().strip();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("couldn't process file: " + e.getMessage());
        }
    }
}
