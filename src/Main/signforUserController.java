package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import PCManager.MG_SelectManWindow;
import PCManager.MG_SelectManWindowController;
import Server.Protocol;
import Student.selectStuWindowController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class signforUserController {
	
	@FXML //어노테이션 작성해줘야지 컨트롤 객체가 주입됨
	private TextField name;
	@FXML 
	private TextField name2;
	@FXML
	private TextField stuSchool;
	@FXML
	private ComboBox<String> school;
	@FXML
	private RadioButton student;
	@FXML
	private RadioButton manager;
	@FXML
	private TextField id;
	@FXML
	private PasswordField pw;
	@FXML
	private PasswordField checkPW;
	@FXML
	private Button find;
	@FXML
	private Button apply;
	@FXML
	private TextField search;
	@FXML
	private boolean dupcheck = false;
	private boolean pwcheck = false;
	
	private String checkedID = "" ;
	
	private OutputStream os;
	private InputStream is;
	
	// 비밀번호, 이메일, 휴대전화번호 
	String pwregExp ="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

	public void checkbtnHandler() throws IOException { // 아이디 중복확인
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
		
		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();
					    		
		protocol = new Protocol(Protocol.PT_REQ_CHECKDUPLICATE_ID);
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

		         case Protocol.PT_RES_CHECKDUPLICATE_ID: 
		            			
		        	 if(protocol.getCode().equals("0")) { // 중복 X
		        		 dupcheck = true;
			     			checkedID = id.getText();
			     			alert.setContentText("사용하실 수 있는 ID입니다.");
		        	 }
		     		else {
		     			dupcheck = false;
		     			alert.setContentText("이미 사용 중인 ID입니다.");
		     		}
		        	 alert.show();	
		         }
		      break program;
		      }
		}
	
	public void findBtnHandler() throws IOException {
		school.getItems().clear();
		
		if(search.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("알림 메시지");
			alert.setHeaderText(null);
			alert.setContentText("검색대상을 입력하세요");
			alert.show();
		}
		else {	
			//입력된 단어로부터 학교들을 db로 부터 받아와서 comboBox에 add
			Protocol protocol = new Protocol();
		    byte[] buf = protocol.getPacket();
		    		
		    protocol = new Protocol(Protocol.PT_REQ_SEARCHSCHOOL);
        	protocol.setSearchschool(search.getText());
        	os.write(protocol.getPacket());
        	
        	program: while (true) {

                is.read(buf); 

                int packetType = buf[0];
               
                protocol.setPacket(packetType, buf);
                   		
                if (packetType == Protocol.PT_EXIT) {
                	break;
               }
                   		
                switch (packetType) {

                   	case Protocol.PT_RES_SEARCHSCHOOL: 
                   		
                   		StringTokenizer st = new StringTokenizer(protocol.getSchools(), "#");
                   		
                   		while(st.hasMoreTokens()) {
                   			school.getItems().add(st.nextToken());
                   		}
                   		
                  break program;
                   		
                }
        	}
		}
	}
	
	public void schoolBoxHandler() { // 학교가 선택되엇을 때
		stuSchool.setText(school.getValue());
	}
	
	
	public void applybtnHandler() throws IOException {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
		
		if(name.getText().equals("")) {
				alert.setContentText("이름을 입력하세요."); alert.show();
			}
		else if(name2.getText().equals("")) {
			alert.setContentText("닉네임을 입력하세요."); alert.show();
		}
			else if(stuSchool.getText().equals("")) { // ??
				alert.setContentText("학교를 선택해주세요."); alert.show();
			}
			else if(!(student.isSelected() || manager.isSelected())) { 
				alert.setContentText("직업을 선택하세요."); alert.show();
			}
			else if(id.getText().equals("")) {
				alert.setContentText("아이디를 입력하세요."); alert.show();
			}
			else if(!dupcheck) {
					alert.setContentText("아이디 중복체크 하세요."); alert.show();
			}
			else if(pw.getText().equals("")){
				alert.setContentText("비밀번호를 입력하세요."); alert.show();
			}
			else if(!pw.getText().matches(pwregExp)) {
				alert.setContentText("비밀번호는 영문, 숫자 포함 8자 이상이여야 합니다."); alert.show();
			}
			else if(checkPW.getText().equals("")){
				alert.setContentText("비밀번호를 확인하세요."); alert.show();
			}
			else if(!pw.getText().equals(checkPW.getText())) {
				alert.setContentText("비밀번호가 일치하지 않습니다."); alert.show();
			}
		
			else {
				int code;

				Protocol protocol = new Protocol();
			    byte[] buf = protocol.getPacket();
			    		
			    protocol = new Protocol(Protocol.PT_REQ_SIGNFORUSER);
			    
				if(student.isSelected()) {

	            	protocol.setData(name.getText()+"#"+name2.getText()+"#"+stuSchool.getText()+"#"
	            			+"1#"+id.getText()+"#"+pw.getText()+"#");

					System.out.println("입력비번: "+pw.getText());
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

	                       	case Protocol.PT_RES_SIGNFORUSER: 
	                       		
	        					alert.setContentText("회원가입이 완료되었습니다.\n"
	        							+"환영합니다."); 
	        					alert.show();
	                       		code = 1;
	                       		
	                       		
	        					FXMLLoader loader = new FXMLLoader(getClass().getResource("/Student/selectStuWindow.fxml"));
	        					Parent second = loader.load();
	        					
	        					selectStuWindowController controller = loader.<selectStuWindowController>getController();
	        					controller.setStream(os, is);
	        					controller.setUser(new User(id.getText(), pw.getText(), name.getText(), 
	        							name2.getText(), code ,"null", "null", stuSchool.getText()));
	        					Scene scene = new Scene(second);
	        					Stage primaryStage =(Stage) pw.getScene().getWindow();
	        					primaryStage.setScene(scene);
	        					
	                       		break program;
	                       		}
	            			}
						}
				else {
					alert.setContentText("회원가입이 완료되었습니다.\n"
							+"환영합니다."); 
					alert.show();
					
					code = 2;
					protocol.setData(name.getText()+"#"+name2.getText()+"#"+stuSchool.getText()+"#"
	            			+"2#"+id.getText()+"#"+pw.getText()+"#");
	            	
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

	                       	case Protocol.PT_RES_SIGNFORUSER: 
	                       		
	        					FXMLLoader loader = new FXMLLoader(getClass().getResource("/PCManager/MG_SelectManWindow.fxml"));
	        					Parent second = loader.load();
	        					
	        					MG_SelectManWindowController controller = loader.<MG_SelectManWindowController>getController();
	        					
	        					controller.setStream(os, is);
	        					controller.setUser(new User(id.getText(), pw.getText(), name.getText(), 
	        							name2.getText(), code , "null", "null", stuSchool.getText()));
	        					
	        					Scene scene = new Scene(second);
	        					Stage primaryStage =(Stage) pw.getScene().getWindow();
	        					primaryStage.setScene(scene);
	        					
	                       		break program;
	                       		
	                    		}
	            		}
				}
				
			}
	}
	
	public void cancelBtnHandler() throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		Parent second = loader.load();
		LoginController controller = loader.<LoginController>getController();
		controller.setStream(os, is);
		Scene scene = new Scene(second);
		Stage primaryStage =(Stage) pw.getScene().getWindow();
		primaryStage.setScene(scene);
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
