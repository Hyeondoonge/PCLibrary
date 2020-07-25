package Main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

public class guser_showDocument extends Application{
	public void start(Stage primaryStage) throws IOException {
		
		Parent root =  FXMLLoader.load(getClass().getResource("guser_showDocument.fxml"));
		
		
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}