package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import PCManager.MG_SelectManWindowController;
import Server.Protocol;
import Server.SQL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Student.selectStuWindowController;
import SystemOperator.Operator;
import SystemOperator.selectOpWindowController;

public class LoginController implements Initializable {
	@FXML
	private TextField id;
	@FXML
	private PasswordField pw;
	@FXML
	private Button loginBtn;
	@FXML
	private Hyperlink sign;
	
	private OutputStream os;
	private InputStream is;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("�˸� �޽���");
    	alert.setHeaderText(null);
    	
    	
    	
		loginBtn.setOnAction(event -> {
	    	try {
			    
	    		Protocol protocol = new Protocol();
			    byte[] buf = protocol.getPacket();
			    		
			    protocol = new Protocol(Protocol.PT_REQ_LOGIN);
            	protocol.setLogin(id.getText()+"#"+pw.getText()+"#");
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

            		case Protocol.PT_RES_LOGIN: 
            			
            			
            			if(protocol.getCode().equals("1")) { // �α��� ����
            				
            				FXMLLoader loader = null;
                			Parent root = null;
                			
                			StringTokenizer st = new StringTokenizer(protocol.getUserInform(), "#");

                			String id = st.nextToken();
        					String password = st.nextToken();
        					String name = st.nextToken();
        					String nickName = st.nextToken();
        					int code = Integer.parseInt(st.nextToken());
        					String phoneNum = st.nextToken();
        					String mail = st.nextToken();
        					String school = st.nextToken();
                			
        					if(code == 1) {
        						
        						User user = new User(id, password, name, nickName, code, phoneNum,  mail, school);
        						
        	        			alert.setContentText("* "+school+" "+nickName+"("+name+")���� ������ ȯ���մϴ� *");
        	        			alert.show();
        	        			
        	        			loader = new FXMLLoader(getClass().getResource("/Student/selectStuWindow.fxml"));
        	        			root = loader.load();
        	        			
        						selectStuWindowController controller = loader.<selectStuWindowController>getController();
        						controller.setStream(os, is);
        						controller.setUser(user);
        						
                			}
                			else if(code == 2) { // pc��������
                				
                				User user = new User(id, password, name, nickName, code, phoneNum,  mail, school);
                				
                				alert.setContentText("* " + school + " " + nickName + "(" + name + ")���� ������ ȯ���մϴ� *");
        						alert.show();
        						loader = new FXMLLoader(getClass().getResource("/PCManager/MG_selectManWindow.fxml"));
        						root = loader.load();
        						
        						MG_SelectManWindowController controller = loader.<MG_SelectManWindowController>getController();
        						controller.setStream(os, is);
        						controller.setUser(user);
                			}
        					
                			else { // ���
                				alert.setContentText("* "+nickName+"_"+name+"���� ������ ȯ���մϴ� *");
                    			alert.show();            			
                    			loader = new FXMLLoader(getClass().getResource("/SystemOperator/selectOpWindow.fxml"));
                    			root = loader.load();
                    			
                    			selectOpWindowController controller = loader.<selectOpWindowController>getController();
                    			controller.setStream(os, is);
                    			controller.setOperator(new Operator(id, name, nickName, code, phoneNum));
                			}

        					Scene scene = new Scene(root);
        					Stage primaryStage = (Stage) loginBtn.getScene().getWindow();
        					primaryStage.setScene(scene);
            			}
            			else if(protocol.getCode().equals("3")) {
            				alert.setContentText("�������� ������� ������ �Ұ����մϴ�.");
        					alert.show();
            			}
            			else { // �α��� ����
            				alert.setContentText("���̵� �Ǵ� ��й�ȣ�� Ʋ�Ƚ��ϴ�.");
        					alert.show();
            				}
            			break program;
            			}
            		}
			}
            catch (IOException e) {
                // TODO Auto-generated catch bloc"r
                e.printStackTrace();
            }
        });
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
	
	public void signLinkHandler() throws IOException{ // ȸ������

		FXMLLoader loader = new FXMLLoader(getClass().getResource("signforUser.fxml"));
		Parent second = loader.load();

		signforUserController controller = loader.<signforUserController>getController();
		controller.setStream(os, is);
		
		Scene scene = new Scene(second);
		Stage primaryStage =(Stage) loginBtn.getScene().getWindow();
		primaryStage.setScene(scene);
	}
}
