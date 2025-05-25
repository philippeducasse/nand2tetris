package constants;

import java.util.HashMap;
import java.util.Map;

public final class SymbolTable {
    public static final Map<String, String> table = new HashMap<>();

    static {
        // Predefined pointers
        table.put("SP", "0");
        table.put("LCL", "1");
        table.put("ARG", "2");
        table.put("THIS", "3");
        table.put("THAT", "4");

        // Predefined registers R0 to R15
        table.put("R0", "0");
        table.put("R1", "1");
        table.put("R2", "2");
        table.put("R3", "3");
        table.put("R4", "4");
        table.put("R5", "5");
        table.put("R6", "6");
        table.put("R7", "7");
        table.put("R8", "8");
        table.put("R9", "9");
        table.put("R10", "10");
        table.put("R11", "11");
        table.put("R12", "12");
        table.put("R13", "13");
        table.put("R14", "14");
        table.put("R15", "15");

        // Screen and keyboard memory addresses
        table.put("SCREEN", "16384");
        table.put("KBD", "24576");
    }

    private SymbolTable() {
        // Prevent instantiation
    }
}
