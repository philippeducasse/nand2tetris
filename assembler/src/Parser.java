import constants.SymbolTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static List<String> parse(String[] args){
        if (args.length == 0) {
            System.out.println("Error: No input file specified.");
            return null;
        }
        String fileName = args[0];

        try{
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            List<String> instructions = new ArrayList<>();
            Integer lineCount = 0;

            for (String line: lines){
                String parsedLine = line.strip();
                if(!parsedLine.isEmpty() && !parsedLine.startsWith("//")){
                    if(parsedLine.startsWith("(")){
                        parseLabel(parsedLine, lineCount);
                     }
                    else {
                        instructions.add(parsedLine);
                        lineCount++;
                    }
                }
            }
            System.out.println(SymbolTable.table);
            return instructions;

        }
        catch (IOException e){
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static void parseLabel(String label, Integer lineCount){
        String labelName = label.substring(1, label.indexOf(")"));
        String strCount = String.valueOf(lineCount);
        SymbolTable.table.put(labelName, strCount);
    }


    public static boolean isNumeric(String str){
        try{
            Integer.parseInt(str);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }
}
