package Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Main.User;
import Server.Protocol;

public class confirmNumController { // 인증번호입력시 뒤로가기 있어서 다시 창 띄울 수 있게 
	// 회원 정보 수정 버튼누르면 새창 X 창 전환으로!

	@FXML
	TextField number;
	@FXML
	Button save;
	@FXML
	Button cancel;
	
	int error = 3;
	
	String numregExp ="^[0-9]{4}"; // 숫자 형식
	private String id, pw, name, nickName, phoneNum, mail, school;
	private int code;
	
	private OutputStream os;
	private InputStream is;
	private User user;
	private selectStuWindowController pa;
	private stuMypageController pa2;
	
	public void setUser(User user, String m) throws IOException {
		id = user.getId();
		pw = user.getPassword();
		name = user.getName();
		nickName = user.getNickName();
		code = user.getCode();
		phoneNum = user.getPhoneNum();
		mail = user.getMail();
		school= user.getSchool();
		
		this.user = user;
		reqCertificateNum(m);
	}
	
	public void reqCertificateNum(String m) throws IOException {
		
		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();
					    		
		protocol = new Protocol(Protocol.PT_REQ_CERTIFICATENUM);
		protocol.setId(id);
		protocol.setMail(m);
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

		            	case Protocol.PT_RES_CERTIFICATENUM: // 서버 측에서 인증번호 전송
		            			
		            		break program;
	    }
	  }
	}
	
	
	public void saveBtnHandler() throws IOException {
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("알림 메시지");
	alert.setHeaderText(null);
	
	if(error == 1) {
		alert.setContentText("인증번호 3회 오류.");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("stuMypage.fxml"));
		Parent second =loader.load(); // 자동으로 뒤로가기
		Scene scene = new Scene(second);
		Stage primaryStage =(Stage)number.getScene().getWindow();
		primaryStage.setScene(scene);
		
		stuMypageController controller = loader.<stuMypageController>getController();
		controller.setStream(os, is);
		controller.setUser(new User(id, pw, name, nickName, code, phoneNum, mail, school));
	}
	else if(number.getText().equals("")){
		alert.setContentText("인증번호를 입력하세요.");
	}
	else if(!number.getText().matches(numregExp)) {
		alert.setContentText("인증번호는 4자리 숫자로 구성됩니다.");
	}
	else {
	
		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();
					    		
		protocol = new Protocol(Protocol.PT_REQ_CERTIFICATENUM_CHECK);
		protocol.setData(number.getText());
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

		            	case Protocol.PT_RES_CERTIFICATENUM_CHECK: 
		            			
		            		if(protocol.getCode().equals("1")) {
		            			alert.setContentText("인증번호가 확인되었습니다."); // 정보가 변경된 창 다시 띄우기. 이때 변경된 이메일 적용 + DB에 등록
		            				
			        			Stage stage = (Stage)save.getScene().getWindow();
			        			stage.close();
			        			
			        			pa2.setUser(new User(user.getId(), user.getPassword(), user.getName(), user.getNickName(),
			        					user.getCode(), user.getPhoneNum(), user.getMail(), user.getSchool()));
			        			
			        			pa.setUser(new User(user.getId(), user.getPassword(), user.getName(), user.getNickName(),
			        					user.getCode(), user.getPhoneNum(), user.getMail(), user.getSchool()));
			        				}
		            		else {
		            			alert.setContentText(error+"회 오류 시 인증이 제한됩니다."); // 3회오류시 창 끄기
		            			error--;
		            		}
		            		break program;
		            
		         	}
		        }
	}
		alert.show();
	}
	
	public void cancelBtnHandler() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("stuMypage.fxml"));
		Parent second =loader.load(); // 자동으로 뒤로가기
		Scene scene = new Scene(second);
		Stage primaryStage =(Stage)number.getScene().getWindow();
		primaryStage.setScene(scene);
		
		stuMypageController controller = loader.<stuMypageController>getController();
		controller.setStream(os, is);
		controller.setUser(new User(id, pw, name, nickName, code, phoneNum, mail, school));
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
