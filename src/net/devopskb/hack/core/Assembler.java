package net.devopskb.hack.core;

import net.devopskb.hack.core.net.devopskb.hack.commands.Command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Assembler {

    //public static final String SOURCE_ASM= "AddWithLabel.asm";
//    public static final String SOURCE_ASM= "Pong.asm";
    private static String sourceAsmFile;
    private static String destinationHackFile;
//    public static final String SOURCE_ASM= "1.asm";
//    public static final String DEST_HACK= "Pong.hack";
//    public static final String SOURCE_ASM= "Mult.asm";
//    public static final String DEST_HACK= "Mult.hack";

    private Parser parser;
    private Code code;
    private SymbolTable symbolTable;
    Integer pc;             // program counter
    String outfile = "";
    public Assembler() {
        pc = 0;
        parser = new Parser(sourceAsmFile);
        symbolTable = new SymbolTable();
        code = new Code(symbolTable);

        // scanning for labels '(Xxx)' to populate symbols table
        firstPass();
        symbolTable.show();

        parser.init();

        // scanning for variables (new)
        // replacing symbols with address values
        // and generate binary code

        secondPass();
        writeOutputFile(destinationHackFile);
        //System.out.println("------------");
        //System.out.println(outfile);
        //symbolTable.show();

        //System.out.println(parser);
        //System.out.println(parser.getSourceSize());


    }
    private void firstPass(){
        System.out.println("starting 1st pass");
        while (parser.hasMoreCommands()){
            parser.advance();
            Command cmd = parser.commandType();
            if (cmd.getType() == "L_COMMAND"){
                symbolTable.addEntry(parser.getLabel(), pc);
                System.out.println("added entry : " + parser.getLabel() + " = " + (pc));
            } else {
                pc++;
            }
        }
    }
    private void secondPass(){
        System.out.println("starting 2nd pass");
        StringBuilder sb = new StringBuilder();
        while (parser.hasMoreCommands()) {
            parser.advance();
            Command cmd = parser.commandType();
            if (cmd.getType() == "A_COMMAND") {
                //System.out.println(code.generateAcommand(parser.symbol()));
                sb.append(code.generateAcommand(parser.symbol()));
                sb.append("\n");
            }
            if (cmd.getType() == "C_COMMAND") {
                //System.out.println(code.generateCcommand(parser.dest(), parser.comp(), parser.jump()));
                sb.append(code.generateCcommand(parser.dest(), parser.comp(), parser.jump()));
                sb.append("\n");
            }
        }
        outfile = sb.toString();
    }

    private void writeOutputFile(String filename){
        Path path = Paths.get(filename);
        try (BufferedWriter writer = Files.newBufferedWriter(path))
        {
            writer.write(outfile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].contains(".asm")){
            sourceAsmFile = args[0];
            destinationHackFile = sourceAsmFile.substring(0, sourceAsmFile.indexOf('.')) + ".hack";

//            System.out.println(sourceAsmFile);
//            System.out.println(destinationHackFile);

            new Assembler();
        } else {
            System.out.println();
            System.out.println("usage: \nAssembler {xxx.asm}\n");
            System.out.println();
        }
    }
}
