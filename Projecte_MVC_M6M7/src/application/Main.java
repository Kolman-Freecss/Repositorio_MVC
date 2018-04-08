package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import resources.ControlErrores;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("VistaLogin.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.getIcons().add(new Image("/resources/logo.png"));
			primaryStage.setScene(scene);
			primaryStage.show();



		} catch(Exception e) {
			ControlErrores.mostrarError("No s'ha pogut iniciar l'aplicació", "Torna a intentar reiniciar l'aplicació");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
