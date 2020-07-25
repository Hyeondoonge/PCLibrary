package SystemOperator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Server.Protocol;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SO_writeDocumentController {

	@FXML
	Button cancel;
	@FXML
	TextField title, image;
	@FXML
	HTMLEditor content;
	
	private String op_id, op_name, op_nickName, op_phoneNum;
	private int op_code;
	private OutputStream os;
	private InputStream is;

	public void setOperator(Operator op) {
			op_id = op.getId();
			op_name = op.getName();
			op_nickName = op.getNickName();
			op_code = op.getCode();
			op_phoneNum = op.getPhoneNum();
		}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}

	public void findBtnHandler() throws IOException{ // 이미지 업로드
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
		alert.setContentText("게시물이 업로드 되었습니다");
		alert.show();
	}
	
	public void attachBtnHandler() throws IOException{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
		alert.setContentText("게시물이 업로드 되었습니다");
		alert.show();
	}
	
	
	public void uploadBtnHandler() throws IOException{
		
		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();
					    		
		protocol = new Protocol(Protocol.PT_REQ_UPLOAD);
		
		String contextTemp = content.getHtmlText();
		
		if(contextTemp.equals("")) {
			contextTemp = "null";
		}
		
		protocol.setData(op_id+"#"+title.getText()+"#"+contextTemp+"#");
		
		System.out.println(protocol.getData());
		
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

		            	case Protocol.PT_RES_UPLOAD: 

		            		Alert alert = new Alert(AlertType.INFORMATION);
		            		alert.setTitle("알림 메시지");
		            		alert.setHeaderText(null);
		            		alert.setContentText("게시물이 업로드 되었습니다");
		            		alert.show();
		            		
		            		FXMLLoader loader = new FXMLLoader(getClass().getResource("SO_Note.fxml"));
		            		Parent second = loader.load();
		            		Scene scene = new Scene(second);
		            		Stage primaryStage =(Stage)cancel.getScene().getWindow();
		            		primaryStage.setScene(scene);
		            		
		            		SO_NoteController controller = loader.<SO_NoteController>getController();
		            		controller.setStream(os, is);
		            		controller.setOperator(new Operator(op_id, op_name, op_nickName, op_code, op_phoneNum));
		            		
		            		break program;
		            			}
		                    }
	}
	
	public void cancelBtnHandler() throws IOException { // SO_writeDocument
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SO_Note.fxml"));
		Parent second = loader.load();
		Scene scene = new Scene(second);
		Stage primaryStage =(Stage)cancel.getScene().getWindow();
		primaryStage.setScene(scene);
		
		SO_NoteController controller = loader.<SO_NoteController>getController();
		controller.setStream(os, is);
		controller.setOperator(new Operator(op_id, op_name, op_nickName, op_code, op_phoneNum));
	}
}
