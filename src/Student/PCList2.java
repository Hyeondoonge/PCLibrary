package Student;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PCList2 extends Application{
	public void start(Stage primaryStage) throws IOException {
		
		Parent root =  FXMLLoader.load(getClass().getResource("PCList2.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("PC ¸ñ·Ï");
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
}
}