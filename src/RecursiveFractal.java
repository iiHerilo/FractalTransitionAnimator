// Superclass for all fractals.

public class RecursiveFractal {
    public static final int SIZE = 1000;
    public static final int HALF = SIZE / 2;
    public static final int FPS = 60;
    
    public void dbg(String s) {
        System.out.print(s);
    }
    public void dbgl(String s) {
        dbg(s + '\n');
    }
}
