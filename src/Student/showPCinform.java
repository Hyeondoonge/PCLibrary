package Student;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class showPCinform extends Application{
	public void start(Stage primaryStage) throws IOException {
		
		Parent root =  FXMLLoader.load(getClass().getResource("showPCinform.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("대여 PC 정보");
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
}
}