package PCManager;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MG_SelectManWindow extends Application {
	public void start(Stage primaryStage) throws IOException {
			//FXMLLoader loader = new FXMLLoader(getClass().getResource("SO_selectManWindow.fxml")); // fxml���� �̸��� �����ϰ�..
		//	Parent root = (Parent) loader.load();
			//
//			primaryStage.setScene(new Scene(root, 300, 275));
//			primaryStage.setTitle("���� ����");
			//
//			primaryStage.show();
			
			Parent root =  FXMLLoader.load(getClass().getResource("MG_selectManWindow.fxml"));
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}