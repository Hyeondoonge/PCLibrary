package PCManager;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MG_PCLenderList extends Application {
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("MG_PCLenderList.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("PC 대여자 목록");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
