package SystemOperator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import Main.User;
import Server.Protocol;
import Server.SQL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SO_manageManagerController {
	
	@FXML
	Label id, name, nickName, school, grouping, phoneNum, email;
	private String op_id, op_name, op_nickName, op_phoneNum;
	private int op_code;
	
	private OutputStream os;
	private InputStream is;
	private SO_manageUserController parent;
	
	public void setUser(User user) {
		this.id.setText(user.getId());
		this.name.setText(user.getName());
		
		if(user.getNickName().equals("null")) {
			this.nickName.setText("등록된 닉네임이 없습니다. ");
		}
		else this.nickName.setText(user.getNickName());
		
		this.school.setText(user.getSchool());
		this.grouping.setText(user.getStrCode());
		
		if(user.getPhoneNum().equals("null")) {
			this.phoneNum.setText("등록된 휴대전화번호가 없습니다. ");
		}
		else this.phoneNum.setText(user.getPhoneNum());
		
		if(user.getMail().equals("null")) {
			this.email.setText("등록된 웹메일이 없습니다. ");
		}
		else this.email.setText(user.getMail());
		}
	
	public void setParent(SO_manageUserController parent) {
		this.parent = parent;
		
	}
	
	public void stopBtnHandler() throws IOException {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
		alert.setContentText("이 회원을 정말 영구정지 시키시겠습니까?");
		alert.showAndWait();
		
		if(alert.getResult().equals(ButtonType.OK)) {
			// OK일 때
			Protocol protocol = new Protocol();
			byte[] buf = protocol.getPacket();
						    		
			protocol = new Protocol(Protocol.PT_REQ_DROPUSER);
			protocol.setId(id.getText());
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

			            	case Protocol.PT_RES_DROPUSER: 

			        			alert = new Alert(AlertType.CONFIRMATION);
			        			alert.setContentText("처리되었습니다.");
			        			alert.setTitle("알림 메시지");
			        			alert.setHeaderText(null);
			        			alert.show();
			        			
			        			Stage primaryStage =(Stage)id.getScene().getWindow();
			        			primaryStage.close();
			        			this.parent.setTable("관리직원");
			        			
			        			break program;
			            		}
			         }
				}
			
	}
	
	public void certifyBtnHandler() throws IOException { // sql
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("이 회원에 대한 인증을 진행할까요?");
		alert.setHeaderText(null);
		alert.showAndWait();
		
		if(alert.getResult()==ButtonType.OK) { // DB에서 인증상태 바꾸기 // id.getText()
			// OK일 때
			Protocol protocol = new Protocol();
			byte[] buf = protocol.getPacket();
						    		
			protocol = new Protocol(Protocol.PT_REQ_CERTIFICATEMG);
			protocol.setId(id.getText());
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

			            	case Protocol.PT_RES_CERTIFICATEMG: 

			        			Alert alert2 = new Alert(AlertType.CONFIRMATION);
			        			alert2.setContentText("처리되었습니다.");
			        			alert2.setTitle("알림 메시지");
			        			alert2.setHeaderText(null);
			        			alert2.show();
			        			
			        			this.parent.setTable("관리직원");
			        			Stage primaryStage =(Stage)id.getScene().getWindow();
			        			primaryStage.close();
			        			
			        			break program;
			            		}
			              }
				}
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
	
	public void setOperator(Operator op) {
		op_id = op.getId();
		op_name = op.getName();
		op_nickName = op.getNickName();
		op_code = op.getCode();
		op_phoneNum = op.getPhoneNum();
	}
}
