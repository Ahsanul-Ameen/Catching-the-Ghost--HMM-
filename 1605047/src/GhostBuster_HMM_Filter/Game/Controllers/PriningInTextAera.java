package GhostBuster_HMM_Filter.Game.Controllers;

import javafx.scene.control.TextArea;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;

public class PriningInTextAera extends PrintStream implements Serializable {
    public static final int WHITE_PLAYER = 1, BLACK_PLAYER = 2, EMPTY_PLAYER = 0;

    //The TextArea to which the output stream will be redirected.
    //A very beautiful OverRidden has taken place here
    private final TextArea status;

    public PriningInTextAera(TextArea area, OutputStream out) {
        super(out);
        status = area;
    }

    public synchronized void println(String string) {
        //status.appendText(string + "\n");
        javafx.application.Platform.runLater(()->status.appendText(string + "\n"));
    }


    public synchronized void print(String string) {
        //status.appendText(string);
	    javafx.application.Platform.runLater(()->status.appendText(string));
    }
}
