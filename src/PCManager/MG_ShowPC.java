package PCManager;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MG_ShowPC extends Application{

	public void start(Stage primaryStage) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("MGshowPC.fxml"));

		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("pc대여신청");
		primaryStage.show();


	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

