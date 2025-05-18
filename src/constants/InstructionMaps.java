package constants;

import java.util.Map;

public final class InstructionMaps {

    private InstructionMaps() {
        // Private constructor to prevent instantiation
    }

    public static final Map<String, String> DEST_BIN_MAP = Map.of(
            "M", "001",
            "D", "010",
            "MD", "011",
            "A", "100",
            "AM", "101",
            "AD", "110",
            "AMD", "111"
    );

    public static final Map<String, String> JMP_BIN_MAP = Map.of(
            "JGT",  "001",
            "JEQ",  "010",
            "JGE",  "011",
            "JLT",  "100",
            "JNE",  "101",
            "JLE",  "110",
            "JMP",  "111"
    );

    public static final Map<String, String> COMP_BIN_MAP = Map.ofEntries(
            Map.entry("0",   "0101010"),
            Map.entry("1",   "0111111"),
            Map.entry("-1",  "0111010"),
            Map.entry("D",   "0001100"),
            Map.entry("A",   "0110000"),
            Map.entry("!D",  "0001101"),
            Map.entry("!A",  "0110001"),
            Map.entry("-D",  "0001111"),
            Map.entry("-A",  "0110011"),
            Map.entry("D+1", "0011111"),
            Map.entry("A+1", "0110111"),
            Map.entry("D-1", "0001110"),
            Map.entry("A-1", "0110010"),
            Map.entry("D+A", "0000010"),
            Map.entry("D-A", "0010011"),
            Map.entry("A-D", "0000111"),
            Map.entry("D&A", "0000000"),
            Map.entry("D|A", "0010101"),
            Map.entry("M",   "1110000"),
            Map.entry("!M",  "1110001"),
            Map.entry("-M",  "1110011"),
            Map.entry("M+1", "1110111"),
            Map.entry("M-1", "1110010"),
            Map.entry("D+M", "1000010"),
            Map.entry("D-M", "1010011"),
            Map.entry("M-D", "1000111"),
            Map.entry("D&M", "1000000"),
            Map.entry("D|M", "1010101")
    );
}
