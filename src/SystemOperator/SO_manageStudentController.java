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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class SO_manageStudentController{

	@FXML
	Label id, name, nickName, school, grouping, phoneNum, webMail;
	
	private String op_id, op_name, op_nickName, op_phoneNum;
	private int op_code;
	private OutputStream os;
	private InputStream is;
	private SO_manageUserController parent;
	
	public void setUser(User user) {
		this.id.setText(user.getId());
		this.name.setText(user.getName());
		
		if(user.getNickName().equals("null")) {
			this.nickName.setText("��ϵ� �г����� �����ϴ�. ");
		}
		else this.nickName.setText(user.getNickName());
		
		this.school.setText(user.getSchool());
		this.grouping.setText(user.getStrCode());
		
		if(user.getPhoneNum().equals("null")) {
			this.phoneNum.setText("��ϵ� �޴���ȭ��ȣ�� �����ϴ�. ");
		}
		else this.phoneNum.setText(user.getPhoneNum());
		
		if(user.getMail().equals("null")) {
			this.webMail.setText("��ϵ� �������� �����ϴ�. ");
		}
		else this.webMail.setText(user.getMail());
	}

	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
		}
	
	public void setParent(SO_manageUserController parent) {
		this.parent = parent;
		
	}
	
	public void setOperator(Operator op) {
		op_id = op.getId();
		op_name = op.getName();
		op_nickName = op.getNickName();
		op_code = op.getCode();
		op_phoneNum = op.getPhoneNum();
	}
	
	public void stopBtnHandler() throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("�˸� �޽���");
		alert.setHeaderText(null);
		alert.setContentText("�� ȸ���� ���� �������� ��Ű�ðڽ��ϱ�?");
		alert.showAndWait();
		
		if(alert.getResult().equals(ButtonType.OK)) {
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
			        	System.out.println("Ŭ���̾�Ʈ ����");
			        	break;
			        }
			            		
			         switch (packetType) {

			            	case Protocol.PT_RES_DROPUSER: 

			        			alert = new Alert(AlertType.CONFIRMATION);
			        			alert.setContentText("ó���Ǿ����ϴ�.");
			        			alert.setTitle("�˸� �޽���");
			        			alert.setHeaderText(null);
			        			alert.show();
			        			
			        			Stage primaryStage =(Stage)id.getScene().getWindow();
			        			primaryStage.close();
			        			
			        			this.parent.setTable("�л�");
			        			
			        			break program;
			            		}
			         }
		}
	}
}
