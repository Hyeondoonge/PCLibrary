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
	    	alert.setTitle("�˸� �޽���");
	    	alert.setHeaderText(null);
			alert.setContentText("��й�ȣ ����: ����, ���� ���� 8�� �̻�.");
			alert.show();	
		}
	}
	public void saveBtnHandler() {
		System.out.println("Ŭ��");
		Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("�˸� �޽���");
    	alert.setHeaderText(null);
		//��й�ȣ ��ġ Ȯ��
		if(pw.getText().equals("") && (phoneNum.getText().equals("")||phoneNum.getText().equals(user.getPhoneNum()))){
					alert.setContentText("���� ������ �����ϴ�.");
				}
		else{
			if(!pw.getText().equals("")&& !isChecking) {
				alert.setContentText("��й�ȣ ����Ȯ���� �ʿ��մϴ�.");
			}
		else if(!pw.getText().equals("") && pwCheck.getText().equals("")) {
				alert.setContentText("��й�ȣ Ȯ���� �ʿ��մϴ�.");
			}
		else if(!pw.getText().equals(pwCheck.getText())) {
				if(!isChecking) {alert.setContentText("��й�ȣ ����: ����, ���� ���� 8�� �̻�."); }
				else if(pwCheck.getLength() == 0) {alert.setContentText("��й�ȣ Ȯ���� �ʿ��մϴ�."); }
				else alert.setContentText("��й�ȣ�� ��ġ�����ʽ��ϴ�."); 
		}
		//�޴���ȭ��ȣ���Ȯ��
		else if(!phoneNum.getText().equals("") && !phoneNum.getText().matches(phregExp)) {
			alert.setContentText("��Ȯ�� �޴���ȭ��ȣ �Է��ϼ���.");
		}
		//email
		else if(!pw.getText().equals(pwCheck.getText())) {
			alert.setContentText("��й�ȣ�� ��ġ�����ʽ��ϴ�.");
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
			        	System.out.println("Ŭ���̾�Ʈ ����");
			        	
			        	break;
			        }
			            		
			         switch (packetType) {

			            	case Protocol.PT_RES_CHANGEDATA: 
			        			alert.setContentText("������ ���������� ����Ǿ����ϴ�.");
			        			
			        			/*FXMLLoader mypage = new FXMLLoader(getClass().getResource("/Student/stuMypage.fxml"));
			        			Parent root = mypage.load();
			        			Scene scene = new Scene(root);
			        			Stage stage = (Stage)phoneNum.getScene().getWindow();
			        			stage.setScene(scene);
			    
			        			stage.setTitle("����������");
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
			} // ��й�ȣ, �޴���ȭ��ȣ ����
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
			System.out.println("����");
			phoneNum.setText(user.getPhoneNum());
		}
		
		if(user.getCode() == 1) {
			phoneLabel.setText("�޴���ȭ��ȣ");
		}
		else {
			phoneLabel.setText("��ȭ��ȣ");
		}
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
