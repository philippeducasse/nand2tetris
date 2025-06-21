import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class VMTranslator {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java VMTranslator path/to/file.vm");
            return;
        }
        // TODO: HANDLE DIRS ASWELL AS SINGLE FILES
        Parser parser = new Parser();
        parser.processInput(args);

        String inputPath = args[0];
        Path path = Paths.get(inputPath);
        String fileName = path.getFileName().toString();
        String baseName = fileName.replaceFirst("\\.\\w+$", "");

        CodeWriter codeWriter = new CodeWriter(parser.instructions, args);

        String vmFileName = generateASMFileName(path, baseName);  // Pass full path
        writeToFile(codeWriter.asmInstructions, vmFileName);
    }

    private static String generateASMFileName(Path inputPath, String baseName) {
        Path parentDir = inputPath.getParent();
        if (parentDir == null) parentDir = Paths.get("."); // default to current dir
        Path outputPath = parentDir.resolve(baseName + ".asm");
        return outputPath.toString();
    }

    private static void writeToFile(List<String> asmInstructions, String outputFileName) {
        try {
            Path path = Paths.get(outputFileName);
            Files.createDirectories(path.getParent());  // ensures dirs like dir/dir/ exist
            Files.write(path, asmInstructions, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Wrote output to " + path);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
