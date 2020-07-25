package PCManager;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MG_PCRegistration extends Application {
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("MG_PCRegistration.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("PC정보 등록");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}