import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private final static String HELP_MESSAGE = "%n%n- Arguments usage: input_file [--file [output_file]]%n";

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            System.err.printf("Invalid number of arguments.%n" + HELP_MESSAGE);
            return;
        }

        String inputFile = args[0];
        String outputFile = inputFile + ".out";

        boolean isPrintToFile = args.length > 1;
        boolean isPrintToInputFile = isPrintToFile;

        if (inputFile == null || Files.notExists(Paths.get(inputFile))) {
            System.err.printf("Invalid input file path. Unable to open file: %s%n", inputFile);
            return;
        }

        if (isPrintToFile) {
            String flag = args[1];

            if (flag == null || !flag.equals("--file")) {
                System.err.printf("Incorrect flag. Expected: '--file', found: '%s'.%n%s", flag, HELP_MESSAGE);
                return;
            }

            if (args.length == 3) {
                outputFile = args[2];

                if (outputFile == null) {
                    System.err.printf("Unable to create result file: <null>%n");
                    return;
                }

                isPrintToInputFile = false;
            }

            Path path = Paths.get(outputFile);

            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (FileAlreadyExistsException ignored) {
                // everything is fine than
            } catch (IOException e) {
                System.err.printf("Unable to create result file: %s%n", e.getMessage());
                return;
            }
        }

        ContentsTableGenerator generator = new ContentsTableGenerator();
        generator.generate(inputFile, outputFile, isPrintToFile);

        if (isPrintToInputFile) {
            try {
                Files.delete(Paths.get(inputFile));
                Files.move(Paths.get(outputFile), Paths.get(inputFile));
            } catch (IOException e) {
                System.err.printf("Unable to move files: %s%n", e.getMessage());
            }
        }
    }
}
