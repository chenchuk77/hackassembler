package net.devopskb.hack.cmd;

/**
 * Created by chenchuk on 10/14/17.
 */
public class Command {
    String type;
    String text;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Command{" +
                "type='" + type + '\'' +
                '}';
    }
}
