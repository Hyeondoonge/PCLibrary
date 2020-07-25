package Student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class applyforPC extends Application{
	public void start(Stage primaryStage) throws IOException {
		
		Parent root =  FXMLLoader.load(getClass().getResource("applyforPC.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("PC대여 신청");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
