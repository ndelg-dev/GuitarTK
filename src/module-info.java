module GuitarTK {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.media;
	requires eu.hansolo.medusa;
	requires eu.hansolo.toolbox;
	requires eu.hansolo.toolboxfx;
	
	opens application to javafx.graphics, javafx.fxml;
}
