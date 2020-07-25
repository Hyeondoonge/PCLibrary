package PCManager;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MG_MyPage extends Application {
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("MG_MyPage.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("마이페이지");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

