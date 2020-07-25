package SystemOperator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import Main.User;
import Student.stuMypageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class selectOpWindowController {
	
	@FXML
	Label id;
	@FXML
	Button userControl;
	@FXML
	Button note;
	@FXML
	Button schoolInform;

	private String ID, pw, name, nickName, phoneNum, mail, school;
	private int code;

	private OutputStream os;
	private InputStream is;
	
	private Operator op = null;
	
	public void userControlBtnHandler() throws IOException {
		
		Stage primaryStage = new Stage();
		FXMLLoader mypage = new FXMLLoader(getClass().getResource("SO_manageUser.fxml"));
		primaryStage.setScene(new Scene(mypage.load()));
		primaryStage.setTitle("회원 관리");
		primaryStage.show();
		
		SO_manageUserController controller = mypage.<SO_manageUserController>getController();
		controller.setStream(os, is);
		controller.setOperator(op);
		
	}
	
	public void noteBtnHandler() throws IOException {
		
		FXMLLoader mypage = new FXMLLoader(getClass().getResource("SO_Note.fxml"));

		Stage primaryStage = new Stage();
		primaryStage.setScene(new Scene(mypage.load()));
		primaryStage.setTitle("공지 사항");
		primaryStage.show();
		
		SO_NoteController controller = mypage.<SO_NoteController>getController();
		controller.setStream(os, is);
		controller.setOperator(op);
		
		}
	
	public void schoolInformBtnHandler() throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader mypage = new FXMLLoader(getClass().getResource("SO_searchSchool.fxml"));
		primaryStage.setScene(new Scene(mypage.load()));
		primaryStage.setTitle("학교 정보 검색");
		primaryStage.show();
		SO_searchSchoolController controller = mypage.<SO_searchSchoolController>getController();
		controller.setStream(os, is);
		controller.setOperator(op);
	}

	public void setOperator(Operator op) {
		this.ID = op.getId();
		this.name = op.getName();
		this.nickName = op.getNickName();
		this.code = op.getCode();
		this.phoneNum = op.getPhoneNum();
		
		this.op =op;
		id.setText(this.ID + "(" + this.nickName +")");
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
