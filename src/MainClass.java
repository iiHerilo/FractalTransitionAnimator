// TO DO: Change Mode

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    public Integer speed = 500;
    public int stackTally = 0;
    private int mode = 1;
    Button btNext = new Button("Next Iteration"),
           btPrev = new Button("Prev Iteration");
    
    
    @Override
    public void start(Stage primaryStage) {
        buttonBox.setPadding(new Insets(15, 15, 15, 15));
        
        btNext.setOnAction(new NextCCurve());
        btPrev.setOnAction(new PrevCCurve());
        buttonBox.getChildren().addAll(btNext, btPrev);
        
        Slider slider = new Slider(100, 1000, 500);
        
        VBox slider1 = new VBox();
        slider1.getChildren().add(slider);
        HBox sliderLabels = new HBox();
        Label min = new Label("Fast                           "),
              max = new Label("Slow");
        min.setAlignment(Pos.TOP_LEFT);
        max.setAlignment(Pos.TOP_RIGHT);
        sliderLabels.getChildren().addAll(min, max);
        slider1.getChildren().add(sliderLabels);
        
        slider.setBlockIncrement(100);
        slider.setMajorTickUnit(100);
        slider.setMinorTickCount(1000);
        slider.setBlockIncrement(100);
        slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            speed = new_val.intValue();
            dbgBox.update();
        });
        
        System.out.print(slider.getLabelFormatter());
        
        buttonBox.getChildren().add(slider1);
        
        VBox modeBox = new VBox();
        Button btnMode = new Button("Change Mode");
        Label lblModeA = new Label("Current Mode:"); 
        //lblModeA.setAlignment(Pos.TOP_CENTER);
        Label lblModeB = new Label(mode == 0 ? "One at a time" : "Stackable"); 
        //lblModeB.setAlignment(Pos.TOP_CENTER);
        EventHandler < ActionEvent > changeMode = e -> {
                mode = mode == 0 ? 1 : mode == 1 ? 2 : 0;
                lblModeB.setText(getMode());
                dbgBox.update();
            };
        btnMode.setOnAction(changeMode);
        modeBox.getChildren().addAll(lblModeA, lblModeB);
        buttonBox.getChildren().addAll(btnMode, modeBox);
        
        mainPane.setCenter(curvPane);
        mainPane.setBottom(buttonBox);
        mainPane.setRight(dbgBox);
        
        dbgBox.update();
        
        Scene scene = new Scene(mainPane, SIZE, SIZE);
        primaryStage.setTitle("Ccurve Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
    private String getMode() {
        switch(mode) {
            case 0: return "One at a time";
            case 1: return "Stack Multiple";
            case 2: return "Continuous";
            default: return "Unknown";
        }
    }
    
    // There are probably 1 million better ways to make a debug menu
    public class DebugMenu extends VBox {
        private final String BOLD = "-fx-font-weight: bold";
        private final int max = 26;
        private final CheckBox chkBox = new CheckBox();
        private final Label lblChkBox = new Label("Show Debug ");
        private final TextFlow txtHeader = new TextFlow();
        private final TextFlow txtBody = new TextFlow();
        private final List<Text> body = new ArrayList<>();
        public boolean bodyVisible = false;
        private final HBox chkandlbl = new HBox();
        private final VBox optionBox = new VBox();
        
        
    
        public DebugMenu() {
            super();
            chkandlbl.setAlignment(Pos.TOP_RIGHT);
            chkandlbl.getChildren().addAll(lblChkBox, chkBox);
            getChildren().addAll(txtHeader, txtBody, optionBox, chkandlbl);
            setHeader();
            EventHandler<ActionEvent> setorhide_body = e -> {
                if(!bodyVisible) {setBody(); setOptions();}
                else {txtBody.getChildren().clear(); optionBox.getChildren().clear();}
                bodyVisible = !bodyVisible;
                update();
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
                new Text(lBORD + spo("v1.0") + rBORD),
                new Text(lBORD + spo("on 21 May 2021") + rBORD), // 5
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
            body.add(new Text(lBORD + spo("Speed: " + speed) + rBORD));
            body.add(new Text(lBORD + spo("forward") + rBORD));
            body.add(new Text(lBORD + spo("stack") + rBORD));
            body.add(new Text(lBORD + spo("tally") + rBORD));//40
            body.add(new Text(lBORD + spo("true statis") + rBORD));
            body.add(new Text(lBORD + spo("mode") + rBORD));
            body.add(new Text(lBORD + hr('=') + rBORD));
            body.forEach((txt) -> {
                txt.setFont(Font.font("Courier New", 12));
            });
            txtBody.getChildren().addAll(body);
        }
        public final void setOptions() {
            rightAlignedCheck[] options = new rightAlignedCheck[5];
            for (int i=0; i<options.length; i++) {
                options[i] = new rightAlignedCheck(curvPane.get(i).getName() + " Curve Visible? ");
                if(curvPane.get(i).getCurve().visibleProperty().getValue())
                    options[i].getCheck().fire();
                options[i].getCheck().setOnAction(new SetCurveVisibility(i));
            }
            optionBox.getChildren().addAll(options);
        }
        
        class SetCurveVisibility implements EventHandler < ActionEvent > {
            int index;
            
            public SetCurveVisibility(int i) {
                index = i;
            }
            
            @Override
            public void handle(ActionEvent e) {
                curvPane.get(index).setStyle(!curvPane.get(index).getCurve().visibleProperty().getValue());
                dbgBox.update();
            }
        }
        
        public class rightAlignedCheck extends HBox {
            private final Label lbl;
            private final CheckBox chk;
            
            public CheckBox getCheck() {return chk;}
            public Label getLabel() {return lbl;}
            
            public rightAlignedCheck(String text) {
                lbl = new Label(text);
                chk = new CheckBox();
                setAlignment(Pos.TOP_RIGHT);
                getChildren().addAll(lbl, chk);
                update();
            }
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
                body.set(26, new Text(lBORD + spo("Next:     " + curvPane.get(1).getCurve().visibleProperty().getValue()) + rBORD));
                body.set(27, new Text(lBORD + spo("Target:   " + curvPane.get(2).getCurve().visibleProperty().getValue()) + rBORD));
                body.set(28, new Text(lBORD + spo("Starting: " + curvPane.get(3).getCurve().visibleProperty().getValue()) + rBORD));
                body.set(29, new Text(lBORD + spo("Previous: " + curvPane.get(4).getCurve().visibleProperty().getValue()) + rBORD));
                body.set(34, new Text(lBORD + spo("Status (Pane): " + curvPane.getStatus()) + rBORD));
                body.set(35, new Text(lBORD + spo("Frame: " + curvPane.getFrame() + (animation != null ? " / " + animation.getCycleCount() : "")) + rBORD));
                body.set(37, new Text(lBORD + spo("Speed: " + speed) + rBORD));
                body.set(38, new Text(lBORD + spo("Direction: " + (curvPane.forward ? "Forward" : "Backward") ) + rBORD));
                body.set(39, new Text(lBORD + spo("Stack: " + curvPane.prevStack) + rBORD));
                body.set(40, new Text(lBORD + spo("Tally: " + stackTally) + rBORD));
                body.set(41, new Text(lBORD + spo("Status (True): " + (animation != null ? animation.getStatus() : "null")) + rBORD));
                body.set(42, new Text(lBORD + spo("Mode: " + (mode == 0 ? "Linear" : mode == 1 ? "Exponential" : mode == 2 ? "Continuous" : "Unknown") + " (" + mode + ")") + rBORD));
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
            animation = new Timeline(new KeyFrame(Duration.millis(speed
                    / RecursiveFractal.FPS), eventHandler));
            animation.setCycleCount(RecursiveFractal.FPS);
            animation.setOnFinished(new Continuer());
            animation.play();
        }
    
    boolean keepup = false;
    class Continuer implements EventHandler < ActionEvent > {
        @Override
        public void handle(ActionEvent e) {
            dbgBox.update();
                if(stackTally != 0) {
                    if (stackTally > 0) btNext.fire();
                    else btPrev.fire();
                }
                if(stackTally != 0 && mode != 2) {
                    if(stackTally > 0) {
                        stackTally--;
                    }
                    else if(stackTally < 0) {
                        stackTally++;
                    }
                }
                if(curvPane.getLevel() >= 17 || curvPane.getLevel() <= 0) {
                    stackTally = 0;
                }
            dbgBox.update();
        }
    }
    
    class NextCCurve implements EventHandler < ActionEvent > {
        @Override
        public void handle(ActionEvent e) {
            if(mode == 2)
                stackTally = 1;
            if(curvPane.isAnimating() && (mode == 0 || mode == 2)) {
                if(mode == 0) {
                    stackTally++;
                    System.out.print("Already animating! Adding to queue (" + stackTally + ")...");
                }
                else {
                    stackTally = 0;
                    System.out.print("Stopping continuous continuation...");
                }
            }
            else if(curvPane.getLevel() < 17) {
                if(!(!curvPane.forward && curvPane.isAnimating())) {
                    curvPane.nextAllCurves();
                    dbgBox.update();
                    if(animation != null) {animation.stop();}
                    dbgBox.update();
                    animate();
                }
                else
                    System.out.println("Already animating backward! Unable to continue.");
            }
            dbgBox.update();
        }
    }
    
    class PrevCCurve implements EventHandler < ActionEvent > {
        @Override
        public void handle(ActionEvent e) {
            if(mode == 2)
                stackTally = -1;
            if(curvPane.isAnimating() && (mode == 0 || mode == 2)) {
                if(mode == 0) {
                    stackTally--;
                    System.out.print("Already animating! Adding to queue (" + stackTally + ")...");
                }
                else {
                    stackTally = 0;
                    System.out.print("Stopping continuous continuation...");
                }
            }
            else if(curvPane.getLevel() > 0) {
                if(!(curvPane.forward && curvPane.isAnimating())) {
                    if(curvPane.forward == true) {
                        animation.stop();
                    }
                    curvPane.prevAllCurves();
                    dbgBox.update();
                    if(animation != null) {animation.stop();}
                    dbgBox.update();
                    animate();
                }
                else
                    System.out.println("Already animating forward! Unable to continue.");
            }
            else stackTally = 0;
            dbgBox.update();
        }
    }
}
