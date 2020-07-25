package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application{
	
	Socket socket = null;
	OutputStream os = null;
	InputStream is = null;
	
	public void start(Stage primaryStage) throws IOException {
		
		Socket socket = new Socket("localhost", 3000);

		os = socket.getOutputStream();
		is = socket.getInputStream();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		Parent root = loader.load();
		LoginController controller = loader.<LoginController>getController();
		controller.setStream(os, is); // 소켓 전달해야함
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Welcome");
		primaryStage.show();

	}

	public void stop() {
		try {
			if (is != null)
				is.close();
			if (os != null)
				os.close();
			if (socket != null)
				socket.close();
		} catch (IOException ioe) {
			System.out.println("Error closing ...");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}