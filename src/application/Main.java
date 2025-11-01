package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {
	
	private MetronomeView currentMetronome;
	private TunerView tunerView;
	
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
        btnMetronome.setOnAction(e -> {
        	if (currentMetronome != null && currentMetronome.isRunning()) {
                currentMetronome.stop();
            }

            currentMetronome = new MetronomeView();
            root.setCenter(currentMetronome);
        });
        
        Button btnTuner = new Button("Tuner");
        btnTuner.getStyleClass().add("menu-button");
        btnTuner.setPrefWidth(150);
        btnTuner.setPrefHeight(45);
        btnTuner.setOnAction(e -> {
        	if (currentMetronome != null && currentMetronome.isRunning()) {
                currentMetronome.stop();
            }
        	tunerView = new TunerView();
        	
            root.setCenter(tunerView);
        });

        Button btnLibrary = new Button("Library");
        btnLibrary.getStyleClass().add("menu-button");
        btnLibrary.setPrefWidth(150);
        btnLibrary.setPrefHeight(45);
        
        dashboard.getChildren().addAll(title, btnMetronome, btnTuner, btnLibrary);

        
        root.setLeft(dashboard);

        root.setCenter(new TunerView());
        
		Scene scene = new Scene(root, 720, 480); 
		scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());

		stage.setResizable(false);
	    stage.setTitle("GuitarTk"); 
	    stage.setScene(scene);
	    stage.show(); 
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
