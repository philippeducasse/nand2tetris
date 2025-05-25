import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Trs {
    public static void main (String[] args) throws IOException {
        Parser parser = new Parser(args);
        parser.readFile();

        // System.out.println("Instructions: " + parser.instructions);

        Translator translator = new Translator(parser.instructions);

        System.out.println("ASM:"+translator.asmInstructions);

        Path path = generateBinFileName(args);

        writeToFile(translator.asmInstructions, path);
    }
    private static Path generateBinFileName(String[] incomingFileName){
        String inputPath = incomingFileName[0];
        Path path = Paths.get(inputPath);
        String fileName = path.getFileName().toString();

        String baseName = fileName.replaceFirst("\\.\\w+$", "");
        Path outputPath = Paths.get("..", "asm", baseName + ".asm");

        return outputPath;
    }

    private static void writeToFile(List<String> binaryInstructions, Path outputFileName){
        try{
            Files.write(outputFileName, binaryInstructions, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Binary instructions written to " + outputFileName);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
