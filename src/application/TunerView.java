package application;

import application.audio.MicInput;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TunerView extends Pane{
	
	private Label title;
	
	private HBox stringBox;
	private ToggleGroup toggle;
	private RadioButton e;
	private RadioButton b;
	private RadioButton g;
	private RadioButton d;
	private RadioButton a;
	private RadioButton eb;
	
	private MicInput mic;
	public RadioButton micBtn;
	private Label tunerLabel = new Label("0 Hz");

	private Gauge fqGauge;
	
	public TunerView(){
		setPrefSize(600, 600);
		
		title = new Label("Guitar tuner");
		title.getStyleClass().add("title");
		title.setLayoutX(55);
		title.setLayoutY(80);
		
		stringBox = new HBox(15);
		stringBox.getStyleClass().add("string_box");
		stringBox.setPrefWidth(415);
		stringBox.setLayoutX(20);
		stringBox.setLayoutY(120);
		
		toggle = new ToggleGroup();
		
		e = new RadioButton("#1 E");
		e.setToggleGroup(toggle);
		b = new RadioButton("#2 B");
		b.setToggleGroup(toggle);
		g = new RadioButton("#3 G");
		g.setToggleGroup(toggle);
		d = new RadioButton("#4 D");
		d.setToggleGroup(toggle);
		a = new RadioButton("#5 A");
		a.setToggleGroup(toggle);
		eb = new RadioButton("#6 E");
		eb.setToggleGroup(toggle);
		
		toggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
		    if (newToggle != null) {
		        RadioButton selected = (RadioButton) newToggle;
		        switch(selected.getText()) {
		            case "#1 E" -> { fqGauge.setMinValue(300); fqGauge.setMaxValue(360); }
		            case "#2 B" -> { fqGauge.setMinValue(220); fqGauge.setMaxValue(270); }
		            case "#3 G" -> { fqGauge.setMinValue(180); fqGauge.setMaxValue(210); }
		            case "#4 D" -> { fqGauge.setMinValue(120); fqGauge.setMaxValue(170); }
		            case "#5 A" -> { fqGauge.setMinValue(90);  fqGauge.setMaxValue(130); }
		            case "#6 E" -> { fqGauge.setMinValue(60);  fqGauge.setMaxValue(100); }
		        }

		        fqGauge.setValue((fqGauge.getMinValue() + fqGauge.getMaxValue())/2);
		    }
		});
		
		micBtn = new RadioButton();
		micBtn.getStyleClass().add("mic_btn");
		micBtn.setLayoutX(54);
		micBtn.setLayoutY(195 + 70);
		micBtn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
		    if (isSelected) {
		        if (mic == null) {
		            mic = new MicInput(freq -> {
		                Platform.runLater(() -> {
		                	fqGauge.setValue(freq);
		                    tunerLabel.setText(String.format("%.2f Hz", freq));
		                });
		            });
		        }
		        mic.start();
		    } else {
		        if (mic != null) {
		            mic.stop();
		        }
		    }
		});
		
		tunerLabel.getStyleClass().add("tunerLabel");
		tunerLabel.setLayoutX(80);
		tunerLabel.setLayoutY(180);
		
		stringBox.getChildren().addAll(e, b, g, d, a, eb);
	
		GaugeBuilder builder = GaugeBuilder.create().skinType(SkinType.PLAIN_AMP);
		
		fqGauge = builder.decimals(2).maxValue(200).unit("Hz")
				.title("Frequency").titleColor(Color.BLACK).unitColor(Color.BLACK)
				.valueColor(Color.BLACK).valueVisible(true)
				.backgroundPaint(Color.LIGHTGRAY).ledVisible(false).build();
		fqGauge.setPrefSize(400, 400);
		fqGauge.setLayoutX(32.5);
		fqGauge.setLayoutY(150);

		getChildren().addAll(title, stringBox, fqGauge, micBtn);
		getStylesheets().add(getClass().getResource("/css/tuner.css").toExternalForm());
		sceneProperty().addListener((obs, oldScene, newScene) -> {
		    if (oldScene != null && newScene == null) {
		       if(micBtn.isSelected()) {
		    	   
		        mic.stop();
		        micBtn.setSelected(false);
		       } else {
		    	   return;
		       }
		    }
		});
	}
	
}
