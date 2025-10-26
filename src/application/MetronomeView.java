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
import javafx.util.Duration;

public class MetronomeView extends Pane {

    private Timeline timeline;
    private boolean running = false;

    private int bpm = 120;

    private Label bpmLabel;
    private Slider bpmSlider;
    private Spinner<Integer> bpmSpinner;
    private Button startStopButton;

    private AudioClip tickSound;

    public MetronomeView() {
        setPrefSize(600, 600);
        
        tickSound = new AudioClip(getClass().getResource("/sounds/tick.wav").toExternalForm());

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
            if (!newVal.matches("\\d*")) { // si no son solo dígitos
                bpmSpinner.getEditor().setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 280, bpm, 1);
        bpmSpinner.setValueFactory(valueFactory);

        
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

        // Botón Start/Stop
        startStopButton = new Button("Start");
        startStopButton.setLayoutX(50);
        startStopButton.setLayoutY(220);
        startStopButton.setOnAction(e -> {
            if (running) stop();
            else start();
        });

        // Añadir elementos al Pane
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
        double interval = 60000.0 / bpm; // intervalo en ms
        timeline = new Timeline(new KeyFrame(Duration.millis(interval), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void tick() {
        System.out.println("TICK");
        tickSound.play(); // reproduce el sonido
    }
}
