package SystemOperator;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class selectOpWindow  extends Application{
	public void start(Stage primaryStage) throws IOException {
		
		Parent root =  FXMLLoader.load(getClass().getResource("selectOpWindow.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("PC�뿩 ��û");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
