package net.devopskb.hack.cmd;

/**
 * Created by chenchuk on 10/14/17.
 */
public class L_Command extends Command {
    public L_Command(String command){
        this.type = "L_COMMAND";
        this.text = command;
    }
}
