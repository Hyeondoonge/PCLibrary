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
	
	@FXML //������̼� �ۼ�������� ��Ʈ�� ��ü�� ���Ե�
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
	
	// ��й�ȣ, �̸���, �޴���ȭ��ȣ 
	String pwregExp ="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

	public void checkbtnHandler() throws IOException { // ���̵� �ߺ�Ȯ��
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("�˸� �޽���");
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
		        System.out.println("Ŭ���̾�Ʈ ����");
		        	break;
		        }
		            		
		      switch (packetType) {

		         case Protocol.PT_RES_CHECKDUPLICATE_ID: 
		            			
		        	 if(protocol.getCode().equals("0")) { // �ߺ� X
		        		 dupcheck = true;
			     			checkedID = id.getText();
			     			alert.setContentText("����Ͻ� �� �ִ� ID�Դϴ�.");
		        	 }
		     		else {
		     			dupcheck = false;
		     			alert.setContentText("�̹� ��� ���� ID�Դϴ�.");
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
			alert.setTitle("�˸� �޽���");
			alert.setHeaderText(null);
			alert.setContentText("�˻������ �Է��ϼ���");
			alert.show();
		}
		else {	
			//�Էµ� �ܾ�κ��� �б����� db�� ���� �޾ƿͼ� comboBox�� add
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
	
	public void schoolBoxHandler() { // �б��� ���õǾ��� ��
		stuSchool.setText(school.getValue());
	}
	
	
	public void applybtnHandler() throws IOException {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("�˸� �޽���");
		alert.setHeaderText(null);
		
		if(name.getText().equals("")) {
				alert.setContentText("�̸��� �Է��ϼ���."); alert.show();
			}
		else if(name2.getText().equals("")) {
			alert.setContentText("�г����� �Է��ϼ���."); alert.show();
		}
			else if(stuSchool.getText().equals("")) { // ??
				alert.setContentText("�б��� �������ּ���."); alert.show();
			}
			else if(!(student.isSelected() || manager.isSelected())) { 
				alert.setContentText("������ �����ϼ���."); alert.show();
			}
			else if(id.getText().equals("")) {
				alert.setContentText("���̵� �Է��ϼ���."); alert.show();
			}
			else if(!dupcheck) {
					alert.setContentText("���̵� �ߺ�üũ �ϼ���."); alert.show();
			}
			else if(pw.getText().equals("")){
				alert.setContentText("��й�ȣ�� �Է��ϼ���."); alert.show();
			}
			else if(!pw.getText().matches(pwregExp)) {
				alert.setContentText("��й�ȣ�� ����, ���� ���� 8�� �̻��̿��� �մϴ�."); alert.show();
			}
			else if(checkPW.getText().equals("")){
				alert.setContentText("��й�ȣ�� Ȯ���ϼ���."); alert.show();
			}
			else if(!pw.getText().equals(checkPW.getText())) {
				alert.setContentText("��й�ȣ�� ��ġ���� �ʽ��ϴ�."); alert.show();
			}
		
			else {
				int code;

				Protocol protocol = new Protocol();
			    byte[] buf = protocol.getPacket();
			    		
			    protocol = new Protocol(Protocol.PT_REQ_SIGNFORUSER);
			    
				if(student.isSelected()) {

	            	protocol.setData(name.getText()+"#"+name2.getText()+"#"+stuSchool.getText()+"#"
	            			+"1#"+id.getText()+"#"+pw.getText()+"#");

					System.out.println("�Էº��: "+pw.getText());
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

	                       	case Protocol.PT_RES_SIGNFORUSER: 
	                       		
	        					alert.setContentText("ȸ�������� �Ϸ�Ǿ����ϴ�.\n"
	        							+"ȯ���մϴ�."); 
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
					alert.setContentText("ȸ�������� �Ϸ�Ǿ����ϴ�.\n"
							+"ȯ���մϴ�."); 
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
	                   	System.out.println("Ŭ���̾�Ʈ ����");
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
