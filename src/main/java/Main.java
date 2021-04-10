import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
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

    public static void main(String[] args) {
        try (FileReader file = new FileReader("src/main/inputs/test.md")) {
            BufferedReader reader = new BufferedReader(file);

            String line;
            String potentialHeader = "----";
            while ((line = reader.readLine()) != null) {
                int spaces = countChar(line, ' ');
                // после 4 пробелов начинается код-сниппет
                if (spaces < 4) {
                    line = line.substring(spaces);
                    int sharps = countChar(line, '#');
                    if (1 <= sharps && sharps <= 6) {
                        System.out.println("#" + sharps);
//                        processHeader(line);
                    } else {
                        int header = alternativeHeader(line);
                        if (header != 0) {
//                            processHeader(potentialHeader);
                            System.out.println("=- " + header);
                            System.out.println(potentialHeader);
                        } else {
                            potentialHeader = line;
                            System.out.println(-1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("errorrororor");
        }
    }
}
