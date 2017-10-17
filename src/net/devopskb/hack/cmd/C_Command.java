package net.devopskb.hack.cmd;

/**
 * Created by chenchuk on 10/14/17.
 */
public class C_Command extends Command {
    public C_Command(String command){
        this.text = command;
        this.type = "C_COMMAND";
    }
}
