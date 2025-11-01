package application;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MetronomeView extends Pane {

    private boolean running = false;
    private int bpm = 140;

    private Label bpmLabel;
    private Slider bpmSlider;
    private Spinner<Integer> bpmSpinner;

    private Circle circle1, circle2, circle3, circle4;
    private int currentBeat = 0; // 0-3

    private AnimationTimer timer;
    private long lastTickTime;
    private long intervalNanos;

    private Button startStopButton;

    private Clip tickClip;
    private Clip tickHighClip;

    public MetronomeView() {
        setPrefSize(600, 600);
        
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/tick.wav"));
            tickClip = AudioSystem.getClip();
            tickClip.open(ais);

            AudioInputStream aisHigh = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/sharpTick.wav"));
            tickHighClip = AudioSystem.getClip();
            tickHighClip.open(aisHigh);

        } catch (Exception e) {
            e.printStackTrace();
        }

        bpmLabel = new Label("BPM: " + bpm);
        bpmLabel.setLayoutX(55);
        bpmLabel.setLayoutY(80);

        bpmSlider = new Slider(20, 260, bpm);
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
            if (!newVal.matches("\\d*")) {
                bpmSpinner.getEditor().setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 260, bpm, 1);
        bpmSpinner.setValueFactory(valueFactory);

        circle1 = new Circle(20, Color.RED);
        circle1.getStyleClass().add("circle");
        circle2 = new Circle(20, Color.WHITE);
        circle2.getStyleClass().add("circle");
        circle3 = new Circle(20, Color.WHITE);
        circle3.getStyleClass().add("circle");
        circle4 = new Circle(20, Color.WHITE);
        circle4.getStyleClass().add("circle");

        circle1.setLayoutX(230);
        circle2.setLayoutX(280);
        circle3.setLayoutX(330);
        circle4.setLayoutX(380);

        circle1.setLayoutY(160);
        circle2.setLayoutY(160);
        circle3.setLayoutY(160);
        circle4.setLayoutY(160);

        getChildren().addAll(circle1, circle2, circle3, circle4);

        // Listeners para slider y spinner
        bpmSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            bpm = newVal.intValue();
            bpmLabel.setText("BPM: " + bpm);
            bpmSpinner.getValueFactory().setValue(bpm);
            if (running) restartTimer();
        });

        bpmSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            bpm = newVal;
            bpmLabel.setText("BPM: " + bpm);
            bpmSlider.setValue(bpm);
            if (running) restartTimer();
        });

        // Botón start/stop
        startStopButton = new Button("Start");
        startStopButton.setLayoutX(50);
        startStopButton.setLayoutY(180);
        startStopButton.setOnAction(e -> {
            if (running) {
                stop();
                resetCircles();
                currentBeat = 0;
                circle1.setFill(Color.RED);
            } else {
                currentBeat = 0;
                start();
            }
        });

        getChildren().addAll(bpmLabel, bpmSlider, bpmSpinner, startStopButton);
        getStylesheets().add(getClass().getResource("/css/mtrne.css").toExternalForm());

    }

    private void start() {
        running = true;
        startStopButton.setText("Stop");
        startTimer();
    }

    public void stop() {
        running = false;
        startStopButton.setText("Start");
        if (timer != null) timer.stop();
    }

    private void restartTimer() {
        if (timer != null) timer.stop();
        startTimer();
    }

    private void startTimer() {
        intervalNanos = (long) ((60.0 / bpm) * 1_000_000_000L);
        lastTickTime = System.nanoTime();

        // Primer tick inmediato
        tick();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastTickTime >= intervalNanos) {
                    tick();
                    lastTickTime += intervalNanos;
                }
            }
        };

        timer.start();
    }

    private void tick() {
        resetCircles();

        Circle currentCircle = switch (currentBeat) {
            case 0 -> circle1;
            case 1 -> circle2;
            case 2 -> circle3;
            case 3 -> circle4;
            default -> circle1;
        };

        Color targetColor = (currentBeat == 0) ? Color.YELLOW : Color.GREEN;


        FillTransition ft = new FillTransition(Duration.millis(150), currentCircle, Color.WHITE, targetColor);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        ft.play();

        try {
            if (currentBeat == 0) {
                tickHighClip.stop();
                tickHighClip.setFramePosition(0);
                tickHighClip.start();
            } else {
                tickClip.stop();
                tickClip.setFramePosition(0);
                tickClip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        currentBeat = (currentBeat + 1) % 4;
    }


    private void resetCircles() {
        circle1.setFill(Color.WHITE);
        circle2.setFill(Color.WHITE);
        circle3.setFill(Color.WHITE);
        circle4.setFill(Color.WHITE);
    }
    
    public boolean isRunning() {
        return running;
    }

}
