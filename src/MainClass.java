// TO DO: Enable/Disable Debug
//        Animate
//        Animation stats in debug

import java.util.Arrays;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import javafx.geometry.Pos;
import javafx.scene.control.Button;
//import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class MainClass extends Application {
    public final int SIZE = RecursiveFractal.SIZE;
    public final int HALF = RecursiveFractal.HALF;
    public BorderPane mainPane = new BorderPane();
    public CurvePane curvPane = new CurvePane();
    public DebugBox dbgBox = new DebugBox();
    public HBox buttonBox = new HBox(15);
    public Timeline animation;
    
    
    @Override
    public void start(Stage primaryStage) {
        buttonBox.setPadding(new Insets(15, 15, 15, 15));
        Button btNext = new Button("Next Iteration");
        Button btPrev = new Button("Prev Iteration");
        btNext.setOnAction(new NextCCurve());
        btPrev.setOnAction(new PrevCCurve());
        buttonBox.getChildren().add(btNext);
        buttonBox.getChildren().add(btPrev);
        
        mainPane.setCenter(curvPane);
        mainPane.setBottom(buttonBox);
        mainPane.setRight(dbgBox);
        
        dbgBox.update();
        
        Scene scene = new Scene(mainPane, SIZE, SIZE);
        primaryStage.setTitle("Ccurve Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
    public class DebugBox extends TextFlow {
        private final int max = 26;
        protected Text[] txts = 
        {
/*0*/       new Text(txt(hr('='))), // bold
/*1*/       new Text(txt("DEBUG")), // bold
/*2*/       new Text(txt(hr('='))), // bold
/*3*/       new Text(txt("PANE:")), // bold
/*4*/       new Text(txt(" SIZE:     " + SIZE)),
/*5*/       new Text(txt(" HALF:     " + HALF)),
/*6*/       new Text(txt(" POINT1:  (" + CCurve.x1() + ", " + CCurve.y1() + ")")),
/*7*/       new Text(txt(" POINT2:  (" + CCurve.x2() + ", " + CCurve.y2() + ")")),
/*8*/       new Text(txt(hr('-'))),
/*9*/       new Text(txt("LEVELS:")), // bold
/*10*/      new Text(txt(" Global:   " + curvPane.getLevel())),
/*11*/      new Text(txt(" Primary:  " + curvPane.get(0).getLevel())),
/*12*/      new Text(txt(" Next:     " + curvPane.get(1).getLevel())),
/*13*/      new Text(txt(" Target:   " + curvPane.get(2).getLevel())),
/*14*/      new Text(txt(" Starting: " + curvPane.get(3).getLevel())),
/*15*/      new Text(txt(" Previous: " + curvPane.get(4).getLevel())),
/*16*/      new Text(txt(hr('-'))),
/*17*/      new Text(txt("VISIBILITY:")),
/*18*/      new Text(txt(" Primary:  " + curvPane.get(0).getCurve().visibleProperty().getValue())),
/*19*/      new Text(txt(" Next:     " + curvPane.get(1).getCurve().visibleProperty().getValue())),
/*20*/      new Text(txt(" Target:   " + curvPane.get(2).getCurve().visibleProperty().getValue())),
/*21*/      new Text(txt(" Starting: " + curvPane.get(3).getCurve().visibleProperty().getValue())),
/*22*/      new Text(txt(" Previous: " + curvPane.get(4).getCurve().visibleProperty().getValue())),
/*23*/      new Text(txt(hr('-'))),
/*24*/      new Text(txt("ANIMATION:")),
/*25*/      new Text(txt(" Status: " + curvPane.getStatus())),
/*26*/      new Text(txt(" Frame: " + curvPane.getFrame())),
/*27*/      new Text(txt(" Target FPS: " + RecursiveFractal.FPS)),
/*28*/      new Text(txt(" ")),
/*29*/      new Text(txt("")),
/*30*/      new Text(txt("")),
/*31*/      new Text(txt("")),
/*32*/      new Text(txt("")),
/*33*/      new Text(txt("")),
/*34*/      new Text(txt("")),
/*35*/      new Text(txt("")),
/*36*/      new Text(txt("")),
/*37*/      new Text(txt("")),
/*38*/      new Text(txt(hr('='))),
/*39*/      new Text(txt("BUILD:")),
/*40*/      new Text(txt(" v0.1.2")),
/*41*/      new Text(txt(" On 10 May, 2021")),
/*42*/      new Text(txt("")),
/*43*/      new Text(txt("")),
/*44*/      new Text(txt("   Coded by Aaron Rogers")),
/*45*/      new Text(txt("                \u00A9 Herilo")),
/*46*/      new Text(txt(hr('='))),


        };
        public DebugBox() {
            for(Text txt : txts) {
                txt.setFont(Font.font("Courier New", 12));
            }
            txts[1].setStyle("-fx-font-weight: bold");
            txts[3].setStyle("-fx-font-weight: bold");
            txts[9].setStyle("-fx-font-weight: bold");
            txts[17].setStyle("-fx-font-weight: bold");
            txts[24].setStyle("-fx-font-weight: bold");
            txts[39].setStyle("-fx-font-weight: bold");
            
            getChildren().addAll(Arrays.asList(txts));
            
        }
        public void update() {
            txts[10].setText(txt(" Global:   " + curvPane.getLevel()));
            txts[11].setText(txt(" Primary:  " + curvPane.get(0).getLevel()));
            txts[12].setText(txt(" Next:     " + curvPane.get(1).getLevel()));
            txts[13].setText(txt(" Target:   " + curvPane.get(2).getLevel()));
            txts[14].setText(txt(" Starting: " + curvPane.get(3).getLevel()));
            txts[15].setText(txt(" Previous: " + curvPane.get(4).getLevel()));
            txts[25].setText(txt(" Status: " + curvPane.getStatus()));
            txts[26].setText(txt(" Frame: " + curvPane.getFrame()));
        }
        private String txt(String s) {
            String end = "|" + s;
            for(int i=end.length(); i<=max; i++) {
                end+=" ";
            }
            end = end.length()>max ? end.substring(0,max) : end;
            return end + "|\n";
        }
        private String hr(char c) {
            String rule = "";
            for(int i=0; i<max; i++) {
                rule += c;
            }
            return rule;
        }
    }
    
    public static void main(String[] args) {
        // Lunch
        Application.launch(args);
    }
    public void animate() {
            EventHandler < ActionEvent > eventHandler = e -> {
                try {
                    curvPane.animate(true);
                    dbgBox.update();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            };
            curvPane.resetFrames();
            curvPane.getMovementFrames();
            animation = new Timeline(new KeyFrame(Duration.millis(500 
                    / RecursiveFractal.FPS), eventHandler));
            animation.setCycleCount(RecursiveFractal.FPS);
            animation.play();
        }
    
    class NextCCurve implements EventHandler < ActionEvent > {
        @Override
        public void handle(ActionEvent e) {
            if(animation != null) {animation.stop();}
            curvPane.nextAllCurves();
            dbgBox.update();
            animate();
        }
    }
    
    class PrevCCurve implements EventHandler < ActionEvent > {
        @Override
        public void handle(ActionEvent e) {
            curvPane.prevAllCurves();
            dbgBox.update();
        }
    }
}
