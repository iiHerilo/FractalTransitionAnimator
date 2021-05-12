// Object for individual Ccurves

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;


public class CCurve extends RecursiveFractal {
    protected static double[] startPoints = {
        250,
        -250,
        250,
        250
    };
    public final static double x1() {return startPoints[0];}
    public final static double y1() {return startPoints[1];}
    public final static double x2() {return startPoints[2];}
    public final static double y2() {return startPoints[3];}
    public final static void setPoints(double x1, double y1, double x2, double y2) {
        startPoints[0] = x1 + HALF;
        startPoints[1] = y1 + HALF;
        startPoints[2] = x2 + HALF;
        startPoints[3] = y2 + HALF;
    }
    public final static void setPoints(int x1, int y1, int x2, int y2) {
        setPoints((double) x1, (double) y1, (double) x2, (double) y2);
    } 
    
    
    // Main Parameters
    private Polyline curve = null;
    private ObservableList points = null;
    private int level = 0;
    
    // Parameter accessors
    public Polyline getCurve() {return curve;}
    public ObservableList getPoints() {return points;}
    public void setPoints(ObservableList refps) {
        points.clear();
        refps.forEach((refp) -> {
            points.add(refp);
        });
    }
    
    // Constructors
    public CCurve() {
        curve = new Polyline();
        points = curve.getPoints();
        setPoints(50,-150,50,150);
    }
    public CCurve(Color clr, boolean visible) {
        this();
        setStyle(clr, visible);
    }
    public CCurve(Color clr, boolean visible, boolean faded) {
        this();
        setStyle(clr, visible, faded);
    }
    public CCurve(Color clr) {
        this();
        setStyle(clr, true);
    }
    public CCurve(boolean faded) {
        this();
        if(faded) {fadedStyle();} else {defaultStyle();}
    }
    
    // Style mutators
    public final void setStyle(Color clr, boolean visible) {
        curve.setStroke(clr);
        curve.setVisible(visible);
    }
    public final void setStyle(Color clr, boolean visible, boolean faded) {
        setStyle(clr, visible);
        if(faded) {fadedStyle();} else {defaultStyle();}
    }
    public final void setStyle(Color clr) {setStyle(clr, true);}
    public final void setStyle(boolean visible) {curve.setVisible(visible);}
    public final void defaultStyle() {
        curve.setStrokeWidth(1);
        curve.setOpacity(1);
    }
    public final void fadedStyle() {
        curve.setStrokeWidth(5);
        curve.setOpacity(.3);
    }
    
    // Level Accessor/Mutator/Methods
    public void setLevel(int level) {this.level = level;}
    public int getLevel() {return level;}
    public void incrementLevel() {level++;}
    public void incrementLevel(int by) {level += by;}
    
    // First position, first x and y values only
    public void firstPosition() {
        points.clear();
        points.add(x1());
        points.add(y1());
    }
    // Perform the CCurve function from 4 points and a level.
    public void curve(double x1, double y1, double x2, double y2, int level) {
        if (level <= 0) {
            points.add(x2);
            points.add(y2);
        } else {
            double xm = (x1 + x2 + y1 - y2) / 2;
            double ym = (x2 + y1 + y2 - x1) / 2;
            curve(x1, y1, xm, ym, level - 1);
            curve(xm, ym, x2, y2, level - 1);
        }
    }
    // Set the polyline to first position
    public void fullCurve() {
        firstPosition();
        // If the level is less than 0, the function cannot be performed
        // and thus the polyline will remian at first position
        if(level >= 0) {
            curve(x1(),y1(),x2(),y2(),level);
        }
    }
    public void fullCurve(int level) {
        setLevel(level);
        fullCurve();
    }
    
    // Mostly copied from "obslistDoubler" of the older program.
    // Takes the midpoints in between each point in a polyline
    public void Midpointify() {
        dbgl("midpointifyign");
        if(level <= 0) {
            dbgl("level is <= 0");
            firstPosition();
            dbgl("1, " + points.size());
            points.add(x1());
            points.add(y1());
            dbgl("2, " + points.size());
        }
        else {
            dbgl("level is over 0");
            Midpointify(points);
        }
        
    }
    public static CCurve Midpointify(CCurve curve) {
        CCurve temp = curve;
        Midpointify(temp.getPoints());
        return temp;
    }
    public static ObservableList getMidpointified(ObservableList points) {
        ObservableList newlist = points;
        Midpointify(newlist);
        return newlist;
    }
    public static void Midpointify(ObservableList points) {
        Object[] oldpoints = points.toArray();
        points.clear();
        points.add(x1());
        points.add(y1());
        if (oldpoints.length == 4) {
            points.add((x1() + x2()) / 2);
            points.add((y1() + y2()) / 2);
            points.add(x2());
            points.add(y2());
        } else {
            for (int i = 2; i < oldpoints.length - 1; i += 2) {
                double x1 = (double) oldpoints[i - 2];
                double y1 = (double) oldpoints[i - 1];
                double x2 = (double) oldpoints[i];
                double y2 = (double) oldpoints[i + 1];
                points.add((x1 + x2) / 2);
                points.add((y1 + y2) / 2);
                points.add(x2);
                points.add(y2);
            }
        }
        points.set(0, (double) x1());
        points.set(1, (double) y1());

        points.set(points.size() - 2, (double) x2());
        points.set(points.size() - 1, (double) y2());
    }
    
    
}
