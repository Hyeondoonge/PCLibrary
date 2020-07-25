package Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Main.insertPasswordController;
import Server.Protocol;
import Main.User;

public class stuMypageController{

	@FXML
	private Label id, school, name, phoneNum;
	@FXML
	private TextField front;
	@FXML
	private ChoiceBox<String> hide;
	@FXML
	private Button changeData;
	@FXML
	private Button lookPC;
	@FXML
	private Label alarm;
	@FXML
	private Button certify;

	private String ID, pw, stname, nickName, stphoneNum, mail, stschool;
	private int code;
	
	boolean borrower = false; // �뿩 ����

	private OutputStream os;
	private InputStream is;
	private selectStuWindowController pa;
	
	public void setParent(selectStuWindowController pa) {
		this.pa = pa;
	}
	
	public void setUser(User user) {
		
		ID = user.getId();
		pw = user.getPassword();
		name.setText(stname = user.getName());
		nickName = user.getNickName();
		code = user.getCode();
		stphoneNum = user.getPhoneNum();
		
		if(stphoneNum == null || stphoneNum.equals("null")) {
			phoneNum.setText("��ϵ� �޴���ȭ��ȣ�� �����ϴ�.");
			phoneNum.setTextFill(Color.web("#828282"));
		}
		else {
			phoneNum.setText(stphoneNum);
		}
		
		mail = user.getMail();
		school.setText(stschool= user.getSchool());
		
		id.setText(ID+"("+nickName+")");
		
		try {
			Protocol protocol = new Protocol();
			byte[] buf = protocol.getPacket();
						    		
			protocol = new Protocol(Protocol.PT_REQ_CERTIFICATESTUDENT); // ���������� �뿩�������ƴ��� ���� ��������
			
			protocol.setId(ID);
			protocol.setSchool(stschool);
			
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

	            	case Protocol.PT_RES_CERTIFICATESTUDENT: 
	            			
	            		if(protocol.getCode().equals("1")) { // ���� O
	            			front.setDisable(true);
	            			hide.setDisable(true);
	            			certify.setDisable(true);
	            			
	            			if(protocol.getBorrowCode().equals("0")){ // �뿩 O
	            				System.out.println("�뿩�� X");
		            			lookPC.setDisable(true);
	            			}
	            		}
	            		else {
	            			hide.getItems().add(protocol.getHidemail()); 
	            			alarm.setVisible(false);
	            			lookPC.setDisable(true);
	            		}
	            	break program;
	            	}
	           }
		}
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public void changeBtnHandler() throws IOException { // new ����
		FXMLLoader mypage = new FXMLLoader(getClass().getResource("/Main/insertPassword.fxml"));
		Parent root = mypage.load();
		Stage primaryStage = (Stage)changeData.getScene().getWindow(); // ����������?
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("��й�ȣ Ȯ��");
		insertPasswordController controller = mypage.<insertPasswordController>getController();
		
		controller.setStream(os, is);
		controller.setParent(pa, this);
		controller.setUser(new User(ID, pw, stname, nickName, code, stphoneNum, mail, stschool));
	}
	
	public void lookPCBtnHandler() throws IOException {
		
		Stage primaryStage = new Stage();
		FXMLLoader mypage = new FXMLLoader(getClass().getResource("showPCinform.fxml"));
		primaryStage.setScene(new Scene(mypage.load()));
		primaryStage.setTitle("�뿩 ���� PC");
		primaryStage.show();
		
		showPCinformController controller = mypage.<showPCinformController>getController();
		controller.setStream(os, is);
		controller.setUser(new User(ID, pw, stname, nickName, code, stphoneNum, mail, stschool));
		
		System.out.println("mypage: "+ ID);
	}
	
	public void certifyBtnHandler() throws IOException { // ������ ����
		
		if(front.getText().equals("") || hide.getValue()==null) { //!front.getText().equals("") || 
			Parent second;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�˸� �޽���");
			alert.setHeaderText(null);
			alert.setContentText("�̸��� �Է¶��� �ٽ� Ȯ���ϼ���");
			alert.show();
		}
		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("confirmNum.fxml"));
			Parent second = loader.load();
			Scene scene = new Scene(second);
			Stage primaryStage =(Stage)lookPC.getScene().getWindow();
			primaryStage.setScene(scene);
			
			confirmNumController controller = loader.<confirmNumController>getController();
			controller.setStream(os, is);
			controller.setParent(pa, this);
			controller.setUser(new User(ID, pw, stname, nickName, code, stphoneNum, mail, stschool), front.getText()+"@"+hide.getValue());
		}
	}

	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
