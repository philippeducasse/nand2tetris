import constants.SymbolTable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Asm {
    public static void main(String[] args) {
        System.out.print("Starting assembler..." + args[0]);
        List<String> instructions = Parser.parse(args);
        if (instructions != null) {
            List<String> binaryInstructions = Translator.translateToBinary(instructions);

            // System.out.println("Binaries:");
            Path hackFileName = generateBinFileName(args);
            writeToFile(binaryInstructions, hackFileName);
        }
    }

    private static Path generateBinFileName(String[] incomingFileName){
        String inputPath = incomingFileName[0];
        Path path = Paths.get(inputPath);
        String fileName = path.getFileName().toString();

        String baseName = fileName.replaceFirst("\\.\\w+$", "");
        Path outputPath = Paths.get("..", "bin", baseName + ".hack");

        return outputPath;
    }

    private static void writeToFile(List <String> binaryInstructions, Path outputFileName){
        try{
            Files.write(outputFileName, binaryInstructions, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Binary instructions written to " + outputFileName);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

}