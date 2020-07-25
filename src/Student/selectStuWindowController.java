package  Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Main.User;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import Main.guser_NoteController;
public class selectStuWindowController implements Initializable{
	@FXML //어노테이션 작성해줘야지 컨트롤 객체가 주입됨
	private Button mypage;
	@FXML
	private Button showList;
	@FXML
	private Button note;
	@FXML
	private Label id;
	
	private String ID, pw, name, nickName, phoneNum, mail, school;
	private int code;

	private OutputStream os;
	private InputStream is;
	
	private User user;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	public void setUser(User user) {
		
		ID = user.getId();
		pw = user.getPassword();
		name = user.getName();
		nickName = user.getNickName();
		code = user.getCode();
		phoneNum = user.getPhoneNum();
		mail = user.getMail();
		school= user.getSchool();
		
		this.user = user;
		
		id.setText(ID+"("+nickName+")");
	}
	
	public User getUser() {
		return user;
	}
	
	public void mypageBtnHandler() throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader mypage = new FXMLLoader(getClass().getResource("stuMypage.fxml"));
		primaryStage.setScene(new Scene(mypage.load()));

		stuMypageController controller = mypage.<stuMypageController>getController();

		controller.setStream(os, is);
		controller.setUser(user);
		controller.setParent(this);
		primaryStage.setTitle("마이페이지");
		primaryStage.show();
	}
	
	public void showListBtnHandler() throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader mypage = new FXMLLoader(getClass().getResource("PCList2.fxml"));
		primaryStage.setScene(new Scene(mypage.load()));
		PCList2Controller controller = mypage.<PCList2Controller>getController();
		controller.setStream(os, is);
		controller.setUser(new User(ID, pw, name, nickName, code, phoneNum, mail, school));
		primaryStage.setTitle("PC목록");
		primaryStage.show();
	}
	
	public void noteBtnHandler() throws IOException {
		Stage primaryStage = new Stage();
		FXMLLoader mypage = new FXMLLoader(getClass().getResource("/Main/guser_Note.fxml"));
		primaryStage.setScene(new Scene(mypage.load()));

		guser_NoteController controller = mypage.<guser_NoteController>getController();

		controller.setStream(os, is);
		controller.setUser(new User(ID, pw, name, nickName, code, phoneNum, mail, school));
		primaryStage.setTitle("공지사항");
		primaryStage.show();	
	}

	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
