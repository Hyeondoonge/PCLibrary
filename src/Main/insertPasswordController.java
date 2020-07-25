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
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
		if(inputPW.getText().equals("")) {
			alert.setContentText("비밀번호를 입력하세요.");
		}
		else {
			if(!inputPW.getText().equals(realpw)) {
				alert.setContentText("비밀번호가 틀렸습니다.");
			}
			else {
				alert.setContentText("비밀번호가 확인되었습니다.");
				
				//확인 되면 개인정보변경창으로
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
				// 학생 mypage
				// 관리직원 mypage
				// 관리자 mypage
			}
			//개인정보 수정창 전환
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
