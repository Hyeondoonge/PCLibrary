package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import PCManager.MG_SelectManWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class signforUser extends Application{
	
	public void start(Stage primaryStage) throws IOException {
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("signforUser.fxml"));
		Parent root = loader.load();
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("회원가입");
		primaryStage.show();
		
	}
	public static void main(String[] args) {
	launch(args);
	
	}
}
