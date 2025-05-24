import java.util.ArrayList;

public class Translator {
    ArrayList<String> vmInstructions;
    ArrayList<String> asmInstructions;

    public Translator(ArrayList<String> vmInstructions){
        this.vmInstructions = vmInstructions;
        this.asmInstructions = new ArrayList<>();

        for(String instruction : this.vmInstructions){
            if(instruction.startsWith("p")){
                writePushPop(instruction);
            } else {
                this.writeArithmetic(instruction);
            }
            asmInstructions.add("// "+instruction);

        }
    }

    public void writeArithmetic(String arithmeticInstruction){
        System.out.println("Arithmetic inst: " + arithmeticInstruction);
    }

    public void writePushPop(String arithmeticInstruction){
        System.out.println("Push/pop inst: " + arithmeticInstruction);
    }
}
