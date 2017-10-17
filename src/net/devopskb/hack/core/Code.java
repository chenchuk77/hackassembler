package net.devopskb.hack.core;

/**
 * Created by chenchuk on 10/14/17.
 */
public class Code {

    public static final String C_PREFIX="111";
    private SymbolTable symbolTable;
    Integer nextVar = 16;   // free space for variables

    public Code(SymbolTable symbolTable){
        this.symbolTable=symbolTable;
    }
    public String dest(String mnemonic){
        // Returns the binary code of the dest mnemonic.
        return "DDD-3bits";
    }

    public String comp(String mnemonic){
        // Returns the binary code of the comp mnemonic.
        return "ACCCCCC-7bits";
    }

    public String jump(String mnemonic){
        // Returns the binary code of the jump mnemonic.
        return "JJJ-3bits";
    }
    public String generateAcommand(String symbol) {
        //Integer intSymbol = Integer.parseInt(symbol);
        //intSymbol.toBinaryString(2);
        //System.out.println("A - output: " + intSymbol);
        String out;
        try {
            // if its a number :
            Integer intSymbol = Integer.parseInt(symbol);
            String instruction16bits = binaryRep(intSymbol);
            System.out.println("A - output [" +symbol+ "] : " + instruction16bits);
            return instruction16bits;
        } catch (NumberFormatException e) {
            // a new symbol will be added first
            if (!symbolTable.contains(symbol)) {
                symbolTable.addEntry(symbol, nextVar);
                System.out.println("L - new symbol (var) [" + symbol + "] : " + nextVar);
                nextVar++;
            }
            // key must exists (old or new)
            Integer address = symbolTable.getAddress(symbol);
            String address16bits = binaryRep(address);
            System.out.println("L - from symbol table [" + symbol + "] : " + address16bits);
            return  address16bits;
        }
    }
    private String binaryRep(Integer number){
        return String.format("%16s", Integer.toBinaryString(number)).replace(' ', '0');
    }
    public String generateCcommand(String dest, String comp, String jump) {
        // implementing translations of dest and jump

        // null 0 0 0       null 0 0 0
        // M    0 0 1       JGT 0 0 1
        // D    0 1 0       JEQ 0 1 0
        // MD   0 1 1       JGE 0 1 1
        // A    1 0 0       JLT 1 0 0
        // AM   1 0 1       JNE 1 0 1
        // AD   1 1 0       JLE 1 1 0
        // AMD  1 1 1       JMP 1 1 1

        // creating an integer and then convert to string of 3 binary digits of dest
        Integer destInt = 0;
        if (dest.contains("A")) destInt += 4;
        if (dest.contains("D")) destInt += 2;
        if (dest.contains("M")) destInt += 1;
        String dest3bits = String.format("%3s", Integer.toBinaryString(destInt)).replace(' ', '0');
        System.out.println("C dest - output [" +dest+ "] : " + dest3bits);

        // string of 3 binary digits of jump. 000 for no jump or null
        String jump3bits = "000" ;
        if (jump.equals("JGT")) jump3bits = "001";
        if (jump.equals("JEQ")) jump3bits = "010";
        if (jump.equals("JGE")) jump3bits = "011";
        if (jump.equals("JLT")) jump3bits = "100";
        if (jump.equals("JNE")) jump3bits = "101";
        if (jump.equals("JLE")) jump3bits = "110";
        if (jump.equals("JMP")) jump3bits = "111";
        System.out.println("C jump - output [" +jump+ "] : " + jump3bits);

        // string of 7 binary digits of comp (a,c1,c2,c3,c4,c5,c6)
        // 1 bit (a) used to switch between A/M
        // 6 bits describe ALU function
        String comp7bits = "";
        if (comp.equals("0"))   comp7bits = "0101010";
        if (comp.equals("1"))   comp7bits = "0111111";
        if (comp.equals("-1"))  comp7bits = "0111010";
        if (comp.equals("D"))   comp7bits = "0001100";
        if (comp.equals("A"))   comp7bits = "0110000";
        if (comp.equals("M"))   comp7bits = "1110000";
        if (comp.equals("!D"))  comp7bits = "0001101";
        if (comp.equals("!A"))  comp7bits = "0110001";
        if (comp.equals("!M"))  comp7bits = "1110001";
        if (comp.equals("-D"))  comp7bits = "0001111";
        if (comp.equals("-A"))  comp7bits = "0110011";
        if (comp.equals("-M"))  comp7bits = "1110011";
        if (comp.equals("D+1")) comp7bits = "0011111";
        if (comp.equals("1+D")) comp7bits = "0011111";
        if (comp.equals("A+1")) comp7bits = "0110111";
        if (comp.equals("1+A")) comp7bits = "0110111";
        if (comp.equals("M+1")) comp7bits = "1110111";
        if (comp.equals("1+M")) comp7bits = "1110111";
        if (comp.equals("D-1")) comp7bits = "0001110";
        if (comp.equals("A-1")) comp7bits = "0110010";
        if (comp.equals("M-1")) comp7bits = "1110010";
        if (comp.equals("D+A")) comp7bits = "0000010";
        if (comp.equals("A+D")) comp7bits = "0000010";
        if (comp.equals("D+M")) comp7bits = "1000010";
        if (comp.equals("M+D")) comp7bits = "1000010";
        if (comp.equals("D-A")) comp7bits = "0010011";
        if (comp.equals("D-M")) comp7bits = "1010011";
        if (comp.equals("A-D")) comp7bits = "0000111";
        if (comp.equals("M-D")) comp7bits = "1000111";
        if (comp.equals("D&A")) comp7bits = "0000000";
        if (comp.equals("A&D")) comp7bits = "0000000";
        if (comp.equals("D&M")) comp7bits = "1000000";
        if (comp.equals("M&D")) comp7bits = "1000000";
        if (comp.equals("D|A")) comp7bits = "0010101";
        if (comp.equals("A|D")) comp7bits = "0010101";
        if (comp.equals("D|M")) comp7bits = "1010101";
        if (comp.equals("M|D")) comp7bits = "1010101";
        System.out.println("C comp - output [" +comp+ "] : " + comp7bits);
        System.out.println("C : " + C_PREFIX + comp7bits + dest3bits + jump3bits);

        return C_PREFIX + comp7bits + dest3bits + jump3bits;

    }

}
