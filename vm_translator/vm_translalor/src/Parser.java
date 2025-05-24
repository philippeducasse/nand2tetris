import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    String fileName;
    ArrayList<String> instructions;
    int lineCount = 0;

    public Parser(String[] fileName){
        this.fileName = fileName[0];
        instructions = new ArrayList<>();
    }

     public void readFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));

        for (String line : lines){
            if (hasMoreCommands()){
                advance(line);
            }
        }
    }

    public void advance(String line){
        if(!line.startsWith("//") && !line.isEmpty()){
            instructions.add(line.strip());
        }
        this.lineCount++;
    }

    public Boolean hasMoreCommands(){
        return lineCount >= instructions.size();
    }
}
