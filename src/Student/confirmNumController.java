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

public class confirmNumController { // ������ȣ�Է½� �ڷΰ��� �־ �ٽ� â ��� �� �ְ� 
	// ȸ�� ���� ���� ��ư������ ��â X â ��ȯ����!

	@FXML
	TextField number;
	@FXML
	Button save;
	@FXML
	Button cancel;
	
	int error = 3;
	
	String numregExp ="^[0-9]{4}"; // ���� ����
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
		        	System.out.println("Ŭ���̾�Ʈ ����");
		        	break;
		        }
		            		
		         switch (packetType) {

		            	case Protocol.PT_RES_CERTIFICATENUM: // ���� ������ ������ȣ ����
		            			
		            		break program;
	    }
	  }
	}
	
	
	public void saveBtnHandler() throws IOException {
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("�˸� �޽���");
	alert.setHeaderText(null);
	
	if(error == 1) {
		alert.setContentText("������ȣ 3ȸ ����.");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("stuMypage.fxml"));
		Parent second =loader.load(); // �ڵ����� �ڷΰ���
		Scene scene = new Scene(second);
		Stage primaryStage =(Stage)number.getScene().getWindow();
		primaryStage.setScene(scene);
		
		stuMypageController controller = loader.<stuMypageController>getController();
		controller.setStream(os, is);
		controller.setUser(new User(id, pw, name, nickName, code, phoneNum, mail, school));
	}
	else if(number.getText().equals("")){
		alert.setContentText("������ȣ�� �Է��ϼ���.");
	}
	else if(!number.getText().matches(numregExp)) {
		alert.setContentText("������ȣ�� 4�ڸ� ���ڷ� �����˴ϴ�.");
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
		        	System.out.println("Ŭ���̾�Ʈ ����");
		        	break;
		        }
		            		
		         switch (packetType) {

		            	case Protocol.PT_RES_CERTIFICATENUM_CHECK: 
		            			
		            		if(protocol.getCode().equals("1")) {
		            			alert.setContentText("������ȣ�� Ȯ�εǾ����ϴ�."); // ������ ����� â �ٽ� ����. �̶� ����� �̸��� ���� + DB�� ���
		            				
			        			Stage stage = (Stage)save.getScene().getWindow();
			        			stage.close();
			        			
			        			pa2.setUser(new User(user.getId(), user.getPassword(), user.getName(), user.getNickName(),
			        					user.getCode(), user.getPhoneNum(), user.getMail(), user.getSchool()));
			        			
			        			pa.setUser(new User(user.getId(), user.getPassword(), user.getName(), user.getNickName(),
			        					user.getCode(), user.getPhoneNum(), user.getMail(), user.getSchool()));
			        				}
		            		else {
		            			alert.setContentText(error+"ȸ ���� �� ������ ���ѵ˴ϴ�."); // 3ȸ������ â ����
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
		Parent second =loader.load(); // �ڵ����� �ڷΰ���
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
