// TO DO: Enable/Disable Debug

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
//import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    public DebugMenu dbgBox = new DebugMenu();
    public HBox buttonBox = new HBox(15);
    public Timeline animation;
    
    
    @Override
    public void start(Stage primaryStage) {
        buttonBox.setPadding(new Insets(15, 15, 15, 15));
        Button btNext = new Button("Next Iteration"),
               btPrev = new Button("Prev Iteration");
        btNext.setOnAction(new NextCCurve());
        btPrev.setOnAction(new PrevCCurve());
        buttonBox.getChildren().addAll(btNext, btPrev);
        
        mainPane.setCenter(curvPane);
        mainPane.setBottom(buttonBox);
        mainPane.setRight(dbgBox);
        
        dbgBox.update();
        
        Scene scene = new Scene(mainPane, SIZE, SIZE);
        primaryStage.setTitle("Ccurve Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
    // There are probably 1 million better ways to make a debug menu
    public class DebugMenu extends VBox {
        private final String BOLD = "-fx-font-weight: bold";
        private final int max = 26;
        private final CheckBox chkBox = new CheckBox();
        private final Label lblChkBox = new Label("Show Debug");
        private final TextFlow txtHeader = new TextFlow();
        private final TextFlow txtBody = new TextFlow();
        private final List<Text> body = new ArrayList<>();
        public boolean bodyVisible = false;
        private final HBox chkandlbl = new HBox();
        
    
        public DebugMenu() {
            super();
            chkandlbl.getChildren().addAll(lblChkBox, chkBox);
            getChildren().addAll(txtHeader, txtBody, chkandlbl);
            setHeader();
            EventHandler<ActionEvent> setorhide_body = e -> {
                if(!bodyVisible) setBody();
                else txtBody.getChildren().clear();
                bodyVisible = !bodyVisible;
            };
            chkBox.setOnAction(setorhide_body);
        }
        
        public final void setHeader() {
            txtHeader.getChildren().clear();
            Text[] items = {
                new Text(lBORD + hr('=') + rBORD), // 0
                new Text(lBORD), 
                new Text(spo("FRACTAL TRANSITION ANIMATOR")), 
                new Text(rBORD),
                new Text(lBORD + spo("v0.2-beta.1") + rBORD),
                new Text(lBORD + spo("on 13 May 2021") + rBORD), // 5
                new Text(lBORD + spo("") + rBORD),
                new Text(lBORD + spoR("Coded by Aaron Rogers") + rBORD),
                new Text(lBORD + spoR("\u00A9 Herilo") + rBORD),
                new Text(lBORD + hr('=') + " |")
            };
            items[2].setStyle(BOLD);
            for(Text txt : items) {
                txt.setFont(Font.font("Courier New", 12));
            }
            txtHeader.getChildren().addAll(Arrays.asList(items));
        }
        
        public final void setBody() {
            body.clear();
            body.add(new Text(lBORD)); // 0
            body.add(bold(new Text(spo("DEBUG"))));
            body.add(new Text(rBORD));
            body.add(new Text(lBORD + hr('=') + rBORD));
            body.add(new Text(lBORD));
            body.add(bold(new Text(spo("PANE:")))); // 5
            body.add(new Text(rBORD));
            body.add(new Text(lBORD + spo("SIZE:     " + SIZE) + rBORD));
            body.add(new Text(lBORD + spo("HALF:     " + HALF) + rBORD));
            body.add(new Text(lBORD + spo("POINT1:  (" + CCurve.x1() + ", " + CCurve.y1() + ")") + rBORD));
            body.add(new Text(lBORD + spo("POINT2:  (" + CCurve.x2() + ", " + CCurve.y2() + ")") + rBORD)); // 10
            body.add(new Text(lBORD + hr('-') + rBORD));
            body.add(new Text(lBORD));
            body.add(bold(new Text(spo("LEVELS:"))));
            body.add(new Text(rBORD));
            body.add(new Text(lBORD + spo("global") + rBORD)); // 15
            body.add(new Text(lBORD + spo("primary") + rBORD));
            body.add(new Text(lBORD + spo("next") + rBORD));
            body.add(new Text(lBORD + spo("target") + rBORD)); 
            body.add(new Text(lBORD + spo("starting") + rBORD));
            body.add(new Text(lBORD + spo("prev") + rBORD)); // 20
            body.add(new Text(lBORD + hr('-') + rBORD));
            body.add(new Text(lBORD));
            body.add(bold(new Text(spo("VISIBILITY:"))));
            body.add(new Text(rBORD));
            body.add(new Text(lBORD + spo("primary") + rBORD)); // 25
            body.add(new Text(lBORD + spo("next") + rBORD));
            body.add(new Text(lBORD + spo("target") + rBORD));
            body.add(new Text(lBORD + spo("starting") + rBORD));
            body.add(new Text(lBORD + spo("prev") + rBORD));
            body.add(new Text(lBORD + hr('-') + rBORD)); // 30
            body.add(new Text(lBORD));
            body.add(bold(new Text(spo("ANIMATION:"))));
            body.add(new Text(rBORD));
            body.add(new Text(lBORD + spo("status") + rBORD));
            body.add(new Text(lBORD + spo("frame") + rBORD));//35
            body.add(new Text(lBORD + spo("Target FPS: " + RecursiveFractal.FPS) + rBORD));
            body.add(new Text(lBORD + spo("forward") + rBORD));
            body.add(new Text(lBORD + spo("stack") + rBORD));
            body.add(new Text(lBORD + hr('=') + rBORD));
            body.forEach((txt) -> {
                txt.setFont(Font.font("Courier New", 12));
            });
            txtBody.getChildren().addAll(body);
        }
        
        public final void update() {
            if(bodyVisible) {
                body.set(15, new Text(lBORD + spo("Global:   " + curvPane.getLevel()) + rBORD));
                body.set(16, new Text(lBORD + spo("Primary:  " + curvPane.get(0).getLevel()) + rBORD));
                body.set(17, new Text(lBORD + spo("Next:     " + curvPane.get(1).getLevel()) + rBORD));
                body.set(18, new Text(lBORD + spo("Target:   " + curvPane.get(2).getLevel()) + rBORD)); 
                body.set(19, new Text(lBORD + spo("Starting: " + curvPane.get(3).getLevel()) + rBORD));
                body.set(20, new Text(lBORD + spo("Previous: " + curvPane.get(4).getLevel()) + rBORD));
                body.set(25, new Text(lBORD + spo("Primary:  " + curvPane.get(0).getCurve().visibleProperty().getValue()) + rBORD));
                body.set(26, new Text(lBORD + spo("Next:     " + curvPane.get(0).getCurve().visibleProperty().getValue()) + rBORD));
                body.set(27, new Text(lBORD + spo("Target:   " + curvPane.get(0).getCurve().visibleProperty().getValue()) + rBORD));
                body.set(28, new Text(lBORD + spo("Starting: " + curvPane.get(0).getCurve().visibleProperty().getValue()) + rBORD));
                body.set(29, new Text(lBORD + spo("Previous: " + curvPane.get(0).getCurve().visibleProperty().getValue()) + rBORD));
                body.set(34, new Text(lBORD + spo("Status: " + curvPane.getStatus()) + rBORD));
                body.set(35, new Text(lBORD + spo("Frame: " + curvPane.getFrame()) + rBORD));
                body.set(37, new Text(lBORD + spo("Direction: " + (curvPane.forward ? "Forward" : "Backward") ) + rBORD));
                body.set(38, new Text(lBORD + spo("Stack: " + curvPane.prevStack) + rBORD));
                txtBody.getChildren().clear();
                body.forEach((txt) -> {
                    txt.setFont(Font.font("Courier New", 12));
                });
                txtBody.getChildren().addAll(body);
            }
        }
        
        // Bold Text
        private Text bold(Text txt) {
            txt.setStyle(BOLD);
            return txt;
        }
        // Space out, right alligned
        private String spoR(String s) {
            String indent = " ";
            for(int i=0; i<max - s.length(); i++) {
                indent += " ";
            }
            return indent + s;
        }
        // Space out
        private String spo(String s) {
            for(int i=s.length(); i<=max; i++) {
                s+=" ";
            }
            return s;
        }
        // Horizontal Rule
        private String hr(char c) {
            String rule = "" + c;
            for(int i=0; i<max; i++) {
                rule += c;
            }
            return rule;
        }
        // Left Border
        private final String lBORD = "| ";
        // Right Border
        private final String rBORD = " |\n";
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
            curvPane.nextAllCurves();
            dbgBox.update();
            if(animation != null) {animation.stop();}
            dbgBox.update();
            animate();
        }
    }
    
    class PrevCCurve implements EventHandler < ActionEvent > {
        @Override
        public void handle(ActionEvent e) {
            if(curvPane.getLevel() > 0) {
                if(curvPane.forward == true) {
                    animation.stop();
                }
                curvPane.prevAllCurves();
                dbgBox.update();
                if(animation != null) {animation.stop();}
                    dbgBox.update();
                animate();
            }
        }
    }
}
