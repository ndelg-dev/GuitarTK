package application;

import application.audio.MicInput;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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
		e.setSelected(true);
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
		
		micBtn = new RadioButton();
		micBtn.getStyleClass().add("mic_btn");
		micBtn.setLayoutX(54);
		micBtn.setLayoutY(180);
		micBtn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
		    if (isSelected) {
		        if (mic == null) {
		            mic = new MicInput(freq -> {
		                Platform.runLater(() -> {
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
		
		getChildren().addAll(title, stringBox, micBtn, tunerLabel);
		getStylesheets().add(getClass().getResource("/css/panel.css").toExternalForm());
	}
}
