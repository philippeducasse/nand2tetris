import java.io.IOException;

public class Trs {
    public static void main (String[] args) throws IOException {
        Parser parser = new Parser(args);
        parser.readFile();

        // System.out.println("Instructions: " + parser.instructions);

        Translator translator = new Translator(parser.instructions);

        System.out.println(translator.asmInstructions);
    }
}
