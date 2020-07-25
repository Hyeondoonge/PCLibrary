package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import PCManager.MG_SelectManWindowController;
import Student.selectStuWindowController;
import Student.stuMypageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class insertPasswordController {
		
	@FXML
	PasswordField inputPW;
	@FXML
	Button check;
	
	User user;
	
	String realpw;
	
	private OutputStream os;
	private InputStream is;
	private selectStuWindowController pa;
	private stuMypageController pa2;
	
	public void checkBtnHandler() throws IOException {
		 
		Parent second;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("�˸� �޽���");
		alert.setHeaderText(null);
		if(inputPW.getText().equals("")) {
			alert.setContentText("��й�ȣ�� �Է��ϼ���.");
		}
		else {
			if(!inputPW.getText().equals(realpw)) {
				alert.setContentText("��й�ȣ�� Ʋ�Ƚ��ϴ�.");
			}
			else {
				alert.setContentText("��й�ȣ�� Ȯ�εǾ����ϴ�.");
				
				//Ȯ�� �Ǹ� ������������â����
	            if(inputPW.getText().contentEquals(realpw)) {
	            	FXMLLoader loader = new FXMLLoader(getClass().getResource("changeData.fxml"));
					second = loader.load();
        			Scene scene = new Scene(second);
        			Stage primaryStage =(Stage) check.getScene().getWindow();
        			primaryStage.setScene(scene);
        			
					changeDataController controller = loader.<changeDataController>getController();

					controller.setStream(os, is);
					controller.setUser(user);
					controller.setParent(pa, pa2);
	            }
				// �л� mypage
				// �������� mypage
				// ������ mypage
			}
			//�������� ����â ��ȯ
		}
		alert.show();
	}
	
	public void setUser(User user) {
		this.user = user;
		realpw = user.getPassword();
		System.out.println(realpw);
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}

	public void setParent(selectStuWindowController pa, stuMypageController pa2 ) {
		this.pa = pa;
		this.pa2 = pa2;
	}
}
