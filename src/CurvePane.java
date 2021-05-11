// Object for a collection of Ccurves


import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;


public class CurvePane extends Pane {
    private final int FPS = RecursiveFractal.FPS;
    
    protected int paneLevel = -1;
    public int getLevel() {return paneLevel;}
    public void setLevel(int lvl) {paneLevel = lvl; setCorrectLevels();}
    public void incrementLevel() {paneLevel++; setCorrectLevels();}
    public void decrementLevel() {paneLevel--; setCorrectLevels();}
    private boolean allvis = true;
    
    private final CCurve[] curves = {
        new CCurve(Color.BLACK, true, false),    // PRIMARY CURVE  lvl-1 -> lvl 0
        new CCurve(Color.YELLOW, allvis, true),  // NEXT CURVE     lvl+1        1
        new CCurve(Color.LIME, allvis, true),    // TARGET CURVE   lvl          2
        new CCurve(Color.MAGENTA, allvis, true), // STARTING CURVE lvl-1        3
        new CCurve(Color.CYAN, allvis, true)     // PREV CURVE     lvl-2        4
    };
    private CCurve tarAlt = new CCurve(Color.RED, allvis, true);
    public CCurve mainCurve() {return curves[0];}
    
    // TARGET CURVE   = A
    // STARTING CURVE = B
    // PRIMARY CURVE  = C
    // Forward:  C = B -> A
    // Backward: C = A -> B
    
    //ANIMATING:
    private int framecounter = 0;
    private double[] mvmntFrames;
    private boolean animating = false;
    public boolean forward = true;
    public int prevStack = 0;
    public int getFrame() {return framecounter;}
    public void resetFrames() {framecounter = 0;}
    public double getMvmntFrame(int index) {return mvmntFrames[index];}
    public boolean isAnimating() {return animating;}
    public String getStatus() {
        if(animating) 
            return "Animating...";
        else 
            return "Ready.";
    }
    
    public CurvePane() {
        for(CCurve curve : curves) {
            this.getChildren().add(curve.getCurve()); 
        }
        this.getChildren().add(tarAlt.getCurve());
        allCurves();
        tarAlt.setPoints(curves[2].getPoints());
    }
    
    public void fixCurves() {
        for(CCurve curve : curves) {
            curve.fullCurve();
        }
    }
    
    public final void nextAllCurves() {
        if(!forward) fixCurves();
        //curves[2].setPoints(tarAlt.getPoints());
        forward = true;
        incrementLevel();
        for(int i=curves.length-1; i > 1; i--) {
            curves[i].setPoints(curves[i-1].getPoints());
        }
        curves[1].fullCurve();
        //curves[0].setPoints(curves[2].getPoints()); 
        System.out.println("No Midpointify, " + (curves[0].getPoints().size() == curves[2].getPoints().size()));
        curves[0].Midpointify();
        System.out.println("Ye Midpointify, " + (curves[0].getPoints().size() == curves[2].getPoints().size()));
        tarAlt.setPoints(curves[2].getPoints());
        
    }
    public final void nextAllCurves2() {
        incrementLevel();
        for(int i=curves.length-1; i > 1; i--) {
            curves[i].setPoints(curves[i-1].getPoints());
        }
        curves[1].fullCurve();
        tarAlt.setPoints(curves[2].getPoints()); 
        curves[0].setPoints(curves[2].getPoints()); 
        //curves[0].Midpointify();
        //animate(true);
        
    }
    
    
    public final void prevAllCurves() {
        prevStack++;
        if(forward) fixCurves();
        tarAlt.setPoints(curves[3].getPoints());
        //curves[2].setPoints(tarAlt.getPoints());
        forward = false;
        decrementLevel();
        for(int i=1; i < curves.length-1; i++) {
            curves[i].setPoints(curves[i+1].getPoints());
        }
        curves[4].fullCurve();
        //curves[0].setPoints(curves[2].getPoints());
        //tarAlt.setPoints(curves[2].getPoints());
        for(int i=0;i<prevStack;i++) {
            curves[2].Midpointify();
        }
    }
    
    public final void allCurves() {
        for(int i=0; i<curves.length-1; i++) {
            curves[i].fullCurve(getCorrectLevel(i));
        }
    }
    
    public int getCorrectLevel(int i) {
        System.out.print("Correct level for curves[" + i + "] is ");
        switch(i) {
            case 0:
                System.out.println(paneLevel);
                return(paneLevel);
            case 1:
                System.out.println(paneLevel + 1);
                return(paneLevel + 1);
            case 2:
                System.out.println(paneLevel);
                return(paneLevel);
            case 3:
                System.out.println(paneLevel - 1);
                return(paneLevel - 1);
            case 4:
                System.out.println(paneLevel - 2);
                return(paneLevel - 2);
            default:
                System.out.println(i + " is out of curves[]'s index.");
                System.out.println(curves[i]);
                return(-1);
        }
    }
    public void setCorrectLevel(int i) {
        curves[i].setLevel(getCorrectLevel(i));
    }
    public void setCorrectLevels() {
        for(int i=0; i<curves.length; i++) {
            setCorrectLevel(i);
        }
    }
    
    public void doMidpoint(int index) {
        curves[index].Midpointify();
    }
    public void doMidpointAll() {
        for(CCurve curve : curves) {
            curve.Midpointify();
        }
    }

    
    public CCurve get(int index) {return curves[index];}
    
    public void getMovementFrames() {
        mvmntFrames = new double[curves[0].getPoints().size()];
        if(!forward && paneLevel == 0) {
            curves[2].setLevel(1);
            for(int i=0;i<prevStack;i++) {
                curves[2].Midpointify();
            }
        }
        //else {
            for(int i=0; i<mvmntFrames.length-1; i++) {
                mvmntFrames[i] = ((double)curves[0].getPoints().get(i) -
                                  (double)curves[2].getPoints().get(i)) / FPS;
            }
            if(!forward && paneLevel == 0) {mvmntFrames[2] /= 2;
                                            mvmntFrames[3] /= 2;}
        //}
    }
    
    public void animate(boolean forward) {
        animating = true;
        framecounter++;
        if(paneLevel == 0) {
            curves[0].getPoints().set(2, (double)curves[0].getPoints().get(2) - mvmntFrames[2]);
            curves[0].getPoints().set(3, (double)curves[0].getPoints().get(3) - mvmntFrames[3]);
        }
        for(int i=0; i<curves[0].getPoints().size(); i++) {
            curves[0].getPoints().set(i, (double)curves[0].getPoints().get(i) - mvmntFrames[i]);
        }
        if(framecounter == FPS) {
            wrapUp();
        }
    }
    public void wrapUp() {
        animating = false;
        if(!forward) {
            dbgl("Wrapping up...");
            curves[0].setPoints(tarAlt.getPoints());
            prevStack = 0;
        }
    }
    
    public void dbg(String s) {
        System.out.print(s);
    }
    public void dbgl(String s) {
        dbg(s + '\n');
    }
}
