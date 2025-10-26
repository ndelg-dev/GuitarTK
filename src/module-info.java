module GuitarTK {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.media;
	
	opens application to javafx.graphics, javafx.fxml;
}
