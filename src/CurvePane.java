// Object for a collection of Ccurves

import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;


public class CurvePane extends Pane {
    protected int paneLevel = -1;
    public int getLevel() {return paneLevel;}
    public void setLevel(int lvl) {paneLevel = lvl; setCorrectLevels();}
    public void incrementLevel() {paneLevel++; setCorrectLevels();}
    public void decrementLevel() {paneLevel--; setCorrectLevels();}
    private boolean allvis = false;
    
    private final CCurve[] curves = {
        new CCurve(Color.BLACK, true, false),    // PRIMARY CURVE  lvl-1 -> lvl 0
        new CCurve(Color.YELLOW, allvis, true),  // NEXT CURVE     lvl+1        1
        new CCurve(Color.LIME, allvis, true),    // TARGET CURVE   lvl          2
        new CCurve(Color.MAGENTA, allvis, true), // STARTING CURVE lvl-1        3
        new CCurve(Color.CYAN, allvis, true)     // PREV CURVE     lvl-2        4
    };
    
    // TARGET CURVE   = A
    // STARTING CURVE = B
    // PRIMARY CURVE  = C
    // Forward:  C = B -> A
    // Backward: C = A -> B
    
    public CurvePane() {
        for(CCurve curve : curves) {
            this.getChildren().add(curve.getCurve()); 
        }
        allCurves();
        //nextAllCurves();
    }
    
    public final void nextAllCurves() {
        incrementLevel();
        for(int i=curves.length-1; i > 1; i--) {
            curves[i].setPoints(curves[i-1].getPoints());
        }
        curves[1].fullCurve();
        curves[0].setPoints(curves[2].getPoints()); 
        //curves[0].Midpointify();
        
    }
    
    public final void prevAllCurves() {
        decrementLevel();
        for(int i=1; i < curves.length-1; i++) {
            curves[i].setPoints(curves[i+1].getPoints());
        }
        curves[4].fullCurve();
        curves[0].setPoints(curves[2].getPoints());
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
    
    
}
