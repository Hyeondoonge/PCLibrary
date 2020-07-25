package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import Server.Protocol;
import Student.selectStuWindowController;
import Student.stuMypageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class changeDataController implements Initializable{
	
	@FXML
	PasswordField pw;
	@FXML
	PasswordField pwCheck;
	@FXML
	TextField phoneNum;
	@FXML
	Label phoneLabel;
	@FXML
	Button save;
	@FXML
	Button cancel;
	@FXML
	Button formatCheck;
	
	Boolean isChecking = false;
	private User user;
	
	String pwregExp ="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
	String phregExp = "^01[016789]\\D?\\d{3,4}\\D?\\d{4}$";
	
	InputStream is;
	OutputStream os;
	
	private selectStuWindowController pa;
	private stuMypageController pa2;
	
	public void setParent(selectStuWindowController pa, stuMypageController pa2 ) {
		this.pa = pa;
		this.pa2 = pa2;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	public void formatBtnHandler() {
		if(pw.getText().matches(pwregExp)) {
			isChecking = true;
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("알림 메시지");
	    	alert.setHeaderText(null);
			alert.setContentText("비밀번호 형식: 영문, 숫자 포함 8자 이상.");
			alert.show();	
		}
	}
	public void saveBtnHandler() {
		System.out.println("클릭");
		Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("알림 메시지");
    	alert.setHeaderText(null);
		//비밀번호 일치 확인
		if(pw.getText().equals("") && (phoneNum.getText().equals("")||phoneNum.getText().equals(user.getPhoneNum()))){
					alert.setContentText("변경 내용이 없습니다.");
				}
		else{
			if(!pw.getText().equals("")&& !isChecking) {
				alert.setContentText("비밀번호 형식확인이 필요합니다.");
			}
		else if(!pw.getText().equals("") && pwCheck.getText().equals("")) {
				alert.setContentText("비밀번호 확인이 필요합니다.");
			}
		else if(!pw.getText().equals(pwCheck.getText())) {
				if(!isChecking) {alert.setContentText("비밀번호 형식: 영문, 숫자 포함 8자 이상."); }
				else if(pwCheck.getLength() == 0) {alert.setContentText("비밀번호 확인이 필요합니다."); }
				else alert.setContentText("비밀번호가 일치하지않습니다."); 
		}
		//휴대전화번호양식확인
		else if(!phoneNum.getText().equals("") && !phoneNum.getText().matches(phregExp)) {
			alert.setContentText("정확한 휴대전화번호 입력하세요.");
		}
		//email
		else if(!pw.getText().equals(pwCheck.getText())) {
			alert.setContentText("비밀번호가 일치하지않습니다.");
			}
		
		else {
			try {
				Protocol protocol = new Protocol();
				byte[] buf = protocol.getPacket();
							    		
				protocol = new Protocol(Protocol.PT_REQ_CHANGEDATA);
				protocol.setId(user.getId());
				protocol.setPassword(pw.getText());
				protocol.setPhoneNum(phoneNum.getText());

				os.write(protocol.getPacket());
				
				program: while (true) {

			        is.read(buf); 

			        int packetType = buf[0];
			        protocol.setPacket(packetType, buf);
			            		
			        if (packetType == Protocol.PT_EXIT) {
			        	System.out.println("클라이언트 종료");
			        	
			        	break;
			        }
			            		
			         switch (packetType) {

			            	case Protocol.PT_RES_CHANGEDATA: 
			        			alert.setContentText("정보가 성공적으로 변경되었습니다.");
			        			
			        			/*FXMLLoader mypage = new FXMLLoader(getClass().getResource("/Student/stuMypage.fxml"));
			        			Parent root = mypage.load();
			        			Scene scene = new Scene(root);
			        			Stage stage = (Stage)phoneNum.getScene().getWindow();
			        			stage.setScene(scene);
			    
			        			stage.setTitle("마이페이지");
			        			stuMypageController controller = mypage.<stuMypageController>getController();
			        			controller.setStream(os, is);
			        			controller.setUser(new User(user.getId(), user.getPassword(), user.getName(), user.getNickName(),
			        					user.getCode(), phoneNum.getText(), user.getMail(), user.getSchool()));*/
			        			
			        			Stage stage = (Stage)phoneNum.getScene().getWindow();
			        			stage.close();
			        			
			        			
			        			pa2.setUser(new User(user.getId(), user.getPassword(), user.getName(), user.getNickName(),
			        					user.getCode(), phoneNum.getText(), user.getMail(), user.getSchool()));
			        			
			        			
			        			
			        			pa.setUser(new User(user.getId(), user.getPassword(), user.getName(), user.getNickName(),
			        					user.getCode(), phoneNum.getText(), user.getMail(), user.getSchool()));
			        			
			        			pw.clear(); pwCheck.clear(); phoneNum.clear();
			        			
			            		break program;
			            			}
							}
					} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 비밀번호, 휴대전화번호 전송
		}
		}
		alert.show();
	}
	
	public void cancelBtnHandler() {
		Stage stage = (Stage)cancel.getScene().getWindow();
	    stage.close();
	}

	public void setUser(User user) {
		this.user = user;
		
		if(!user.getPhoneNum().equals("null")) {
			System.out.println("널임");
			phoneNum.setText(user.getPhoneNum());
		}
		
		if(user.getCode() == 1) {
			phoneLabel.setText("휴대전화번호");
		}
		else {
			phoneLabel.setText("전화번호");
		}
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
