import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class Parser {
    ArrayList<String> instructions;
    int lineCount = 1;

    public Parser() {
        instructions = new ArrayList<>();
    }

    public void processInput(String[] args) throws IOException {
        File input = new File(args[0]);

        if (!input.exists()) {
            System.out.println("Input does not exist.");
            return;
        }

        if (input.isDirectory()) {
            File[] files = input.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("Directory is empty.");
                return;
            }
            for (File file : files) {
                if (file.isFile()) {
                    this.read(file);
                }
            }
        } else if (input.isFile()) {
            this.read(input);
        } else {
            System.out.println("Input is neither a file nor a directory.");
        }
    }

    public void read(File fileName) throws IOException {
        instructions.add("File: " + fileName.getName());

        List<String> lines = Files.readAllLines(fileName.toPath());
        for (String line : lines) {
            if (hasMoreCommands()) {
                advance(line);
            }
        }
    }

    public void advance(String line) {
        if (!line.startsWith("//") && !line.isEmpty()) {
            String parsedLine= line.split("//")[0].strip();
            instructions.add(parsedLine);
        }
        this.lineCount++;
    }

    public Boolean hasMoreCommands() {
        return lineCount >= instructions.size();
    }
}
