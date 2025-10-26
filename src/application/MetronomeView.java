package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MetronomeView extends Pane {

    private Timeline timeline;
    private boolean running = false;

    private int bpm = 120;

    private Label bpmLabel;
    private Slider bpmSlider;
    private Spinner<Integer> bpmSpinner;
    
    private Circle circle1;
    private Circle circle2;
    private Circle circle3;
    private Circle circle4;
    
    private int currentBeat = 0; //0 - 3
    
    private Button startStopButton;

    private AudioClip tickSound;
    private AudioClip tickSoundHigh;

    public MetronomeView() {
        setPrefSize(600, 600);
        
        tickSound = new AudioClip(getClass().getResource("/sounds/tick.wav").toExternalForm());
        tickSoundHigh = new AudioClip(getClass().getResource("/sounds/sharpTick.wav").toExternalForm());
        
        bpmLabel = new Label("BPM: " + bpm);
        bpmLabel.setLayoutX(55);
        bpmLabel.setLayoutY(80);

        bpmSlider = new Slider(20, 280, bpm);
        bpmSlider.setLayoutX(50);
        bpmSlider.setLayoutY(100);
        bpmSlider.setShowTickMarks(true);
        bpmSlider.setShowTickLabels(true);
        bpmSlider.setPrefWidth(350);
        bpmSlider.setMajorTickUnit(40);
        bpmSlider.setMinorTickCount(3);

        bpmSpinner = new Spinner<>();
        bpmSpinner.setLayoutX(50);
        bpmSpinner.setLayoutY(150);
        bpmSpinner.setEditable(true);
        bpmSpinner.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) { //solo dígitos
                bpmSpinner.getEditor().setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 280, bpm, 1);
        bpmSpinner.setValueFactory(valueFactory);

        circle1 = new Circle(20, Color.RED);
        circle2 = new Circle(20, Color.WHITE);
        circle3 = new Circle(20, Color.WHITE);
        circle4 = new Circle(20, Color.WHITE);

        circle1.setLayoutX(230);
        circle2.setLayoutX(280);
        circle3.setLayoutX(330);
        circle4.setLayoutX(380);

        circle1.setLayoutY(160);
        circle2.setLayoutY(160);
        circle3.setLayoutY(160);
        circle4.setLayoutY(160);

        getChildren().addAll(circle1, circle2, circle3, circle4);
        
        bpmSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            bpm = newVal.intValue();
            bpmLabel.setText("BPM: " + bpm);
            bpmSpinner.getValueFactory().setValue(bpm);
            if (running) restartTimeline();
        });

        bpmSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            bpm = newVal;
            bpmLabel.setText("BPM: " + bpm);
            bpmSlider.setValue(bpm);
            if (running) restartTimeline();
        });

        startStopButton = new Button("Start");
        startStopButton.setLayoutX(50);
        startStopButton.setLayoutY(220);
        startStopButton.setOnAction(e -> {
            if (running){
            	stop();
            	circle1.setFill(Color.RED);
                circle2.setFill(Color.WHITE);
                circle3.setFill(Color.WHITE);
                circle4.setFill(Color.WHITE);
                
                currentBeat = 0;
            }
            else start();
        });

        getChildren().addAll(bpmLabel, bpmSlider, bpmSpinner, startStopButton);
    }

    
    private void start() {
        running = true;
        startStopButton.setText("Stop");
        startTimeline();
    }

    private void stop() {
        running = false;
        startStopButton.setText("Start");
        if (timeline != null) timeline.stop();
    }

    private void restartTimeline() {
        if (timeline != null) timeline.stop();
        startTimeline();
    }

    private void startTimeline() {
        double interval = 60000.0 / bpm; //intervalo en ms
        timeline = new Timeline(new KeyFrame(Duration.millis(interval), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void tick() {
        circle1.setFill(Color.WHITE);
        circle2.setFill(Color.WHITE);
        circle3.setFill(Color.WHITE);
        circle4.setFill(Color.WHITE);
        
        switch(currentBeat) {
        	case 0 -> circle1.setFill(Color.GREEN);
        	case 1 -> circle2.setFill(Color.GREEN);
        	case 2 -> circle3.setFill(Color.GREEN);
        	case 3 -> circle4.setFill(Color.GREEN);
        }
        
        if(currentBeat == 0) {
        	tickSoundHigh.play();
        } else {
        	tickSound.play();
        }
        
        currentBeat = (currentBeat + 1) % 4;
    }
}
