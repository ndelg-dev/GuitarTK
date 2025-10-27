package application;

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
	
	public RadioButton micBtn;
	
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
		
		
		
		stringBox.getChildren().addAll(e, b, g, d, a, eb);
		
		getChildren().addAll(title, stringBox, micBtn);
		getStylesheets().add(getClass().getResource("/css/panel.css").toExternalForm());
	}
}
