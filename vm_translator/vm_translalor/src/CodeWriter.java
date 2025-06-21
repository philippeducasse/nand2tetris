import java.util.*;

public class CodeWriter {
    ArrayList<String> vmInstructions;
    ArrayList<String> asmInstructions;
    String[] fileNames;
    String currentFileName;
    int counter = 0;

    public CodeWriter(ArrayList<String> vmInstructions, String[] fileNames) {
        this.vmInstructions = vmInstructions;
        this.asmInstructions = new ArrayList<>();
        this.fileNames = fileNames;
        this.currentFileName = "";

        System.out.println("VM INSTRUCTIONS:" + this.vmInstructions);
        System.out.println("ASM INSTRUCTIONS:" + this.asmInstructions);


        // Sets SP to 256 and calls sys.init;
        this.writeInit();

        for (String instruction : this.vmInstructions) {
            String command = instruction.split(" ")[0];
            switch (command) {
                case "push":
                case "pop":
                    writePushPop(instruction);
                    break;
                case "label":
                    writeLabel(instruction);
                    break;
                case "if-goto":
                    writeIf(instruction);
                    break;
                case "goto":
                    writeGoTo(instruction);
                    break;
                case "call":
                    writeCall(instruction);
                    break;
                case "function":
                    writeFunction(instruction);
                    break;
                case "return":
                    writeReturn();
                    break;
                default:
                    writeArithmetic(instruction);
                    break;
            }
        }
    }

    public void setFileName(String fileName) {
        this.currentFileName = fileName;
        System.out.println("SETTING FILE NAME: " + this.currentFileName);
        // TODO: what is this for??
    }

    public void writeInit() {
        ArrayList<String> initSP = new ArrayList<>(Arrays.asList("@256", "D=A", "@SP", "M=D"));
        this.asmInstructions.addAll(initSP);
        // TODO: add call Sys.init
    }

    public void writeLabel(String label) {
        String loopName = "(" + label.split(" ")[1].toUpperCase() + ")";
        this.asmInstructions.add(loopName);
    }

    public void writeGoTo(String label) {
        System.out.println("WRITING GOTO");
        String labelName = label.split(" ")[1];
        String asmLabel = "@" + labelName;

        this.asmInstructions.add(asmLabel);
        this.asmInstructions.add("0;JMP");
    }

    public void writeIf(String label) {
        System.out.println("WRITE IF: " + label);
        String labelName = label.split(" ")[1];
        String asmLabel = "@" + labelName;
        ArrayList<String> ifCmd = new ArrayList<>(Arrays.asList(
                "@SP", "AM=M-1", "D=M", asmLabel, "D;JNE"
        ));
        this.asmInstructions.addAll(ifCmd);

    }

    public void writeFunction(String label) {

        String labelName = label.split(" ")[1];
        Integer n = Integer.valueOf(label.split(" ")[2]);
        String asmFunctionLabel = "(" + labelName + ")";

        String comment = "// " + label;

        ArrayList<String> asmFunctionInstruction = new ArrayList<>(Arrays.asList(
                comment
        ));

        asmFunctionInstruction.add(asmFunctionLabel);
        // assign LCL to current SP
        asmFunctionInstruction.addAll(Arrays.asList(
                 "@SP", "D=M", "@LCL", "M=D"
        ));

        // initiate n LCL registers to 0
        for (int i = 0; i < n; i++) {
            asmFunctionInstruction.addAll(Arrays.asList(
                    "@0" , "D=A", "@SP", "AM=M+1", "A=A-1", "M=D"
            ));
        }
        this.asmInstructions.addAll(asmFunctionInstruction);
    }

    public void writeCall(String label) {
        // CALLER
        // call foo n
            // 1. set ARG to base address of n
            // 2. save the callers frame:
                    // return address
                        // (Foo$ret.1)
                    // saved LCL
                        // push LCL
                    // push ARG
                    // push THIS
                    // push THAT
            // ARG = SP - 5 - n -> repositions the the ARG pointer
            // LCL = SP -> reposition LCL
        // JUMP to the called function
        // insert (Foo$ret.1) return label
        System.out.println("WRITE CALL: " + label);
    }

    public void writeReturn() {
        String comment = "// return" ;

        // endFrame = LCL -> endFrame is a temp variable and refers to everything that should be deleted after return
        ArrayList<String> asmReturnInstruction = new ArrayList<>(Arrays.asList(
               comment, "@LCL", "D=M", "@R13" , "M=D"
        ));

        // push last value of the SP of the callee and overwrite the arg[0] on the stack
        asmReturnInstruction.addAll(Arrays.asList(
                // *ARG = pop()
                "@SP", "AM=M-1", "D=M", "@ARG", "A=M","M=D",
                // SP = ARG +1
                "@ARG", "D=M+1", "@SP", "M=D"
        ));

        // reinstate callers stack and memory segments
        asmReturnInstruction.addAll(Arrays.asList(
                // THAt = *(endFrame -1)
                "@R13", "A=M-1", "D=M", "@THAT", "M=D",
                // THIS = *(endFrame -2)
                "@2", "D=A","@R13", "A=M-D", "D=M", "@THIS", "M=D",
                // ARG = *(endFrame -3)
                "@3", "D=A","@R13", "A=M-D", "D=M", "@ARG", "M=D",
                // LCL = *(endFrame -4)
                "@4", "D=A","@R13", "A=M-D", "D=M", "@LCL", "M=D"
        ));

        // get return address
        // returnAddres = *(endFrame-5)
        // jump to return address (Foo$ret.1)
        asmReturnInstruction.addAll(Arrays.asList(
                "@5", "D=A", "@R13", "A=M-D", "D=M", "@R14", "AM=D", "0;JMP"
        ));

        this.asmInstructions.addAll(asmReturnInstruction);
    }

    public void writeArithmetic(String arithmeticInstruction) {
        switch (arithmeticInstruction) {
            case "add":
                ArrayList<String> add = new ArrayList<>(Arrays.asList("@SP", "AM=M-1", "D=M", "A=A-1", "M=D+M"));
                this.asmInstructions.addAll(add);
                break;

            case "sub":
                ArrayList<String> sub = new ArrayList<>(Arrays.asList("@SP", "AM=M-1", "D=M", "A=A-1", "M=M-D"));
                this.asmInstructions.addAll(sub);
                break;

            case "eq":
                ArrayList<String> eq = new ArrayList<>(
                        Arrays.asList("@SP", "AM=M-1", "D=M", "A=A-1", "D=M-D",  // x-y
                                "@SET_TRUE_" + counter, "D;JEQ",   // if value is 0, set to true (-1)
                                "@SP", "A=M-1", "M=0", "@END_" + counter, "0;JMP", // if not, sent to 0 (false)
                                "(SET_TRUE_" + counter + ")", "@SP", "A=M-1", "M=-1", "@END_" + counter, "0;JMP",
                                "(END_" + counter + ")"
                        ));
                counter++;
                this.asmInstructions.addAll(eq);
                break;

            case "lt":
                ArrayList<String> lt = new ArrayList<>(
                        Arrays.asList("@SP", "AM=M-1", "D=M", "A=A-1", "D=M-D",
                                "@SET_TRUE_" + counter, "D;JLT",
                                "@SP", "A=M-1", "M=0", "@END_" + counter, "0;JMP",
                                "(SET_TRUE_" + counter + ")", "@SP", "A=M-1", "M=-1", "@END_" + counter, "0;JMP",
                                "(END_" + counter + ")"
                        ));
                counter++;
                this.asmInstructions.addAll(lt);
                break;

            case "gt":
                ArrayList<String> gt = new ArrayList<>(
                        Arrays.asList("@SP", "AM=M-1", "D=M", "A=A-1", "D=M-D",
                                "@SET_TRUE_" + counter, "D;JGT",
                                "@SP", "A=M-1", "M=0", "@END_" + counter, "0;JMP",
                                "(SET_TRUE_" + counter + ")", "@SP", "A=M-1", "M=-1", "@END_" + counter, "0;JMP",
                                "(END_" + counter + ")"
                        ));
                counter++;
                this.asmInstructions.addAll(gt);
                break;

            case "neg":
                ArrayList<String> neg = new ArrayList<>(
                        Arrays.asList("@SP", "A=M-1", "D=M", "@0", "D=A-D", "@SP", "A=M-1", "M=D"));
                this.asmInstructions.addAll(neg);
                break;

            case "and":
                ArrayList<String> and = new ArrayList<>(Arrays.asList(
                        "@SP", "AM=M-1", "D=M", "A=A-1", "D=D&M", "M=D"
                ));
                this.asmInstructions.addAll(and);
                break;

            case "or":
                ArrayList<String> or = new ArrayList<>(Arrays.asList(
                        "@SP", "AM=M-1", "D=M", "A=A-1", "D=D|M", "M=D"
                ));
                this.asmInstructions.addAll(or);
                break;

            case "not":
                ArrayList<String> not = new ArrayList<>(Arrays.asList(
                        "@SP", "A=M-1", "D=M", "M=!D"
                ));
                this.asmInstructions.addAll(not);
                break;
        }
    }

    public void writePushPop(String pushPopInstruction) {
        // System.out.println("Push/pop inst: " + pushPopInstruction);
        String[] instArr = pushPopInstruction.split(" ");
        String seg = instArr[1];
        int val = Integer.parseInt(instArr[2]);

        if (pushPopInstruction.startsWith("push")) {
            this.translatePush(seg, val);
        } else {
            this.translatePop(seg, val);
        }
    }

    public void translatePush(String segment, int value) {
        ArrayList<String> pushInst = new ArrayList<>();

        String index = "@" + value;
        List<String> assign = Arrays.asList("D=M", index, "A=D+A", "D=M");

        switch (segment) {
            case "constant":
                Collections.addAll(pushInst, index, "D=A");
                break;

            case "local":
                pushInst.add("@LCL");
                pushInst.addAll(assign);
                break;

            case "temp":
                String tempIndex = "@" + (5 + value);
                pushInst.add(tempIndex);
                pushInst.add("D=M");
                break;

            case "argument":
                pushInst.add("@ARG");
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

            case "pointer":
                if (value == 0) {
                    Collections.addAll(pushInst, "@THIS", "D=M");
                } else if (value == 1) {
                    Collections.addAll(pushInst, "@THAT", "D=M");
                } else {
                    throw new Error("pointer can only reference values 0 or 1");
                }
                break;

            case "static":
                String staticName = "@" + currentFileName + "." + value;
                Collections.addAll(pushInst, staticName, "D=M");
                break;

        }
        Collections.addAll(pushInst, "@SP", "AM=M+1", "A=A-1", "M=D");

        this.asmInstructions.addAll(pushInst);
        // System.out.println("ASM: " + pushInst);
    }

    public void translatePop(String segment, int value) {
        // System.out.println("seg: " + segment + "\nval: "+value);
        ArrayList<String> popInst = new ArrayList<>();
        String index = "@" + value;

        if (!segment.equals("temp") && !segment.equals("pointer") && !segment.equals("static")) {
            Collections.addAll(popInst, index, "D=A");
        }
        // store pop destination address in tmp R13
        List<String> assign = Arrays.asList("D=D+M", "@R13", "M=D");

        switch (segment) {

            case "local":
                popInst.add("@LCL");
                popInst.addAll(assign);
                break;

            case "temp":
                String tempIndex = "@" + (5 + value);
                Collections.addAll(popInst, "@SP", "AM=M-1", "D=M");
                popInst.add(tempIndex);
                popInst.add("M=D");
                break;

            case "argument":
                popInst.add("@ARG");
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

            case "pointer":
                // 0 is THIS, 1 is THAT
                if (value == 0) {
                    Collections.addAll(popInst, "@SP", "AM=M-1", "D=M", "@THIS", "M=D");
                } else if (value == 1) {
                    Collections.addAll(popInst, "@SP", "AM=M-1", "D=M", "@THAT", "M=D");
                } else {
                    throw new Error("pointer can only reference values 0 or 1");
                }
                break;

            case "static":
                String staticName = "@" + currentFileName + "." + value;
                Collections.addAll(popInst, "@SP", "AM=M-1", "D=M", staticName, "M=D");
                break;
        }

        if (!segment.equals("temp") && !segment.equals("pointer") && !segment.equals("static")) {
            Collections.addAll(popInst, "@SP", "AM=M-1", "D=M", "@R13", "A=M", "M=D");
        }

        this.asmInstructions.addAll(popInst);

        // System.out.println("ASM: " + popInst);
    }
}
