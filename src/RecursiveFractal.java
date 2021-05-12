// Superclass for all fractals.

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public class RecursiveFractal {
    public static final int SIZE = 1000;
    public static final int HALF = SIZE / 2;
    public static final int FPS = 30;
    
    public void dbg(String s) {
        System.out.print(s);
    }
    public void dbgl(String s) {
        dbg(s + '\n');
    }
}
