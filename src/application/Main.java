package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {
	
	@Override
	public void start(Stage stage) {
		BorderPane root = new BorderPane();
		
		VBox dashboard = new VBox(25);
		dashboard.getStyleClass().add("dashboard");
        dashboard.setPrefWidth(200);

        Text title = new Text("Dashboard");
        title.getStyleClass().add("dashboard-title");

        Button btnMetronome = new Button("Metronome");
        btnMetronome.getStyleClass().add("menu-button");
        btnMetronome.setPrefWidth(150);
        btnMetronome.setPrefHeight(45);
        
        Button btnTuner = new Button("Tuner");
        btnTuner.getStyleClass().add("menu-button");
        btnTuner.setPrefWidth(150);
        btnTuner.setPrefHeight(45);

        Button btnLibrary = new Button("Library");
        btnLibrary.getStyleClass().add("menu-button");
        btnLibrary.setPrefWidth(150);
        btnLibrary.setPrefHeight(45);
        
        dashboard.getChildren().addAll(title, btnMetronome, btnTuner, btnLibrary);

        
        root.setLeft(dashboard);

        Pane mainArea = new Pane();
        
        Text hola = new Text("ejemplo");
        hola.setX(1);
        hola.setY(300);

        mainArea.getChildren().add(hola);
        
        root.setCenter(mainArea);
        
		Scene scene = new Scene(root, 1000, 700); 
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stage.setResizable(false);
	    stage.setTitle("GuitarTk"); 
	    stage.setScene(scene);
	    stage.show(); 
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
