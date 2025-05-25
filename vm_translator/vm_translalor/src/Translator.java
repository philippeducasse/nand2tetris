import java.util.*;

public class Translator {
    ArrayList<String> vmInstructions;
    ArrayList<String> asmInstructions;

    public Translator(ArrayList<String> vmInstructions){
        this.vmInstructions = vmInstructions;
        this.asmInstructions = new ArrayList<>();

        for(String instruction : this.vmInstructions){
            this.asmInstructions.add("// "+instruction);
            if(instruction.startsWith("p")){
                this.writePushPop(instruction);
            } else {
                this.writeArithmetic(instruction);
            }
        }
    }

    public void writeArithmetic(String arithmeticInstruction){
        System.out.println("Arithmetic inst: " + arithmeticInstruction);
        switch (arithmeticInstruction){
            case "add":
                ArrayList<String> add = new ArrayList<>(Arrays.asList("@SP", "AM=M-1", "D=M", "AM=M-1", "D=D+M", "M=D"));
                this.asmInstructions.addAll(add);
                break;

                case "sub":
                ArrayList<String> sub = new ArrayList<>(Arrays.asList("@SP", "AM=M-1", "D=M", "AM=M-1", "D=M-D", "M=D"));
                this.asmInstructions.addAll(sub);
                break;
        }
    }

    public void writePushPop(String pushPopInstruction){
        // System.out.println("Push/pop inst: " + pushPopInstruction);
        String[] instArr= pushPopInstruction.split(" ");
        String seg = instArr[1];
        int val = Integer.parseInt(instArr[2]);

        if(pushPopInstruction.startsWith("push")){
            this.translatePush(seg, val);

        } else {
            this.translatePop(seg, val);
        }
    }

    public void translatePush(String segment, int value){
        System.out.println("SEGMENT: "+ segment);

        ArrayList<String> pushInst = new ArrayList<>();
        String index = "@" + value;
        Collections.addAll(pushInst,index, "D=A");

        List<String> assign = Arrays.asList("A=M", "D=D+A");

        switch (segment){
            case "constant":
                break;

            case "local":
                pushInst.add("@LCL");
                pushInst.addAll(assign);
                break;

            case "temp":
                pushInst.add("@TEMP");
                pushInst.addAll(assign);
                break;

            case "argument":
                pushInst.add("@ARGUMENT");
                pushInst.addAll(assign);
                break;

            case "this":
                pushInst.add("@THIS");
                pushInst.addAll(assign);
                break;

            case "that":
                pushInst.add("@THAT");
                pushInst.addAll(assign);
                break;

        }
        Collections.addAll(pushInst,"@SP", "AM=M+1", "A=A-1", "M=D");
        this.asmInstructions.addAll(pushInst);
        // System.out.println("ASM: " + pushInst);
    }
    public void translatePop(String segment, int value){
        // System.out.println("seg: " + segment + "\nval: "+value);
        ArrayList<String> popInst = new ArrayList<>();
        String index = "@" + value;
        Collections.addAll(popInst,index, "D=A");

        // store pop destination address in tmp R13
        List<String> assign = Arrays.asList("A=M", "D=D+A", "@R13", "M=D");

        switch (segment){
            case "constant":
                break;

            case "local":
                popInst.add("@LCL");
                popInst.addAll(assign);
                break;

            case "temp":
                popInst.add("@TEMP");
                popInst.addAll(assign);
                break;

            case "argument":
                popInst.add("@ARGUMENT");
                popInst.addAll(assign);
                break;

            case "this":
                popInst.add("@THIS");
                popInst.addAll(assign);
                break;

            case "that":
                popInst.add("@THAT");
                popInst.addAll(assign);
                break;

        }
        Collections.addAll(popInst,"@SP", "AM=M-1", "D=M", "@R13", "A=M", "M=D");
        this.asmInstructions.addAll(popInst);

        // System.out.println("ASM: " + popInst);
    }
}
