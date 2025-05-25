import constants.InstructionMaps;
import constants.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class Translator {

    public static List<String> translateToBinary(List<String> instructions){
        List<String> binaryInstructions = new ArrayList<>();
        Integer variableIndex = 16;
        for (String instruction : instructions){
            if(instruction.startsWith("@")){
                String val = instruction.substring(1);
                    if(!SymbolTable.table.containsKey(val) && !Parser.isNumeric(val)) {
                        parseSymbol(val, variableIndex);
                        variableIndex++;
                    }

                    String binary = getAInstruction(instruction);
                    binaryInstructions.add(binary);

            } else {
                String base = "111";
                String destBin = "000";
                String jmpBin = "000";
                String compSym = "";

                int eqIndex = instruction.indexOf('=');

                if (eqIndex != -1) {
                    String destSym = instruction.substring(0,eqIndex);
                    destBin = getDestBin(destSym);
                }

                Integer semicolonIndex = instruction.indexOf(";");

                if (semicolonIndex != -1) {
                    String jumpSym = instruction.substring(semicolonIndex + 1);
                    jmpBin = getJumpBin(jumpSym);
                }

                if (eqIndex != -1 && semicolonIndex != -1) {
                    compSym = instruction.substring(eqIndex + 1, semicolonIndex);

                }

                else if (eqIndex != -1) {
                    compSym = instruction.substring(eqIndex + 1);
                }

                else if (semicolonIndex != -1) {
                    compSym = instruction.substring(0, semicolonIndex);
                }

                String compBin = getCompBin(compSym);


                String cInstructionBin = base + compBin + destBin + jmpBin;

                binaryInstructions.add(cInstructionBin);
            }
        }
        return binaryInstructions;
    }

    private static String getCompBin(String comp) {
        return InstructionMaps.COMP_BIN_MAP.getOrDefault(comp, "0000000");
    }

    private static String getJumpBin(String jump) {
        return InstructionMaps.JMP_BIN_MAP.getOrDefault(jump, "000");
    }

    private static String getDestBin(String dSymbol){
        return InstructionMaps.DEST_BIN_MAP.getOrDefault(dSymbol, "000");
    }

    private static String convertToBinary (String val){
        int value = Integer.parseInt(val);       // convert to int
        String binary = Integer.toBinaryString(value);   // convert to binary string
        while (binary.length() < 16) {
            binary = "0" + binary;
        }
        return binary;
    }

    private static String getAInstruction(String instruction) {
        String stringValue = instruction.substring(1);
        if(SymbolTable.table.containsKey(stringValue)){
            String memoryVal = SymbolTable.table.get(stringValue);
            return convertToBinary(memoryVal);
        } else {
           return convertToBinary(stringValue);
        }
    }

    private static void parseSymbol (String sym, Integer variableIndex) {
        System.out.println("parsing..." + sym+ " IDX: "+variableIndex);

        SymbolTable.table.put(sym, String.valueOf(variableIndex));
        System.out.println("UPDATED TABLE : "+SymbolTable.table);

    }

}
