package Main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class guser_Note extends Application{
	public void start(Stage primaryStage) throws IOException {
		
		Parent root =  FXMLLoader.load(getClass().getResource("guser_Note.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("공지사항");
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
}
}