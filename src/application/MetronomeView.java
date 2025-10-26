package application;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

public class MetronomeView extends Pane {

    private boolean running = false;
    private int bpm = 120;

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

        // Cargar sonidos usando Clip
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

        // Círculos indicadores
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
        startStopButton.setLayoutY(220);
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

        switch (currentBeat) {
            case 0 -> circle1.setFill(Color.GREEN);
            case 1 -> circle2.setFill(Color.GREEN);
            case 2 -> circle3.setFill(Color.GREEN);
            case 3 -> circle4.setFill(Color.GREEN);
        }

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
