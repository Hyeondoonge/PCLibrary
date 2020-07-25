package SystemOperator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import Main.Post;
import Main.User;
import Main.guser_NoteController;
import Server.Protocol;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SO_showDocumentController {
	
	@FXML
	Label title1, title2, title3, publisher, date, view;
	@FXML
	TextArea content;
	
	private String op_id, op_name, op_nickName, op_phoneNum;
	private int op_code;
	
	private SO_NoteController parent;
	private OutputStream os;
	private InputStream is;

	private int num;
	private Post post;
	private Operator op;
	
	public void setOperator(Operator op) {
		op_id = op.getId();
		op_name = op.getName();
		op_nickName = op.getNickName();
		op_code = op.getCode();
		op_phoneNum = op.getPhoneNum();
	}
	
	public void setDocument(int num, String title, String publisher, String date, int view, String content) {
		this.num = num;
		
		if(title.length()>30) {
			title1.setText(title.substring(0, 30));
			title2.setText(title.substring(30, title.length()));
		}
		else {
			title3.setText(title);
		}
		this.publisher.setText(publisher);
		this.date.setText(date);
		
		content = content.replaceAll("\\s\\s\\s\\s", "\n"); 
		this.content.setText(content);
		
		System.out.print(view);
		this.view.setText(Integer.toString(view));
	}

	public void setParent(SO_NoteController parent) {
		this.parent = parent;
		
	}
	
	public void deleteBtnHandler() throws IOException {

		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
		alert.setContentText("현재 게시물을 정말 삭제하시겠습니까?");
		alert.showAndWait();
		
		if(alert.getResult().equals(ButtonType.OK)) {
			Protocol protocol = new Protocol();
			byte[] buf = protocol.getPacket();
						    		
			protocol = new Protocol(Protocol.PT_REQ_DELETENOTE);
			protocol.setData(Integer.toString(num));
			System.out.println("삭제할번호: "+protocol.getData());
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
	
			      case Protocol.PT_RES_DELETENOTE: 

			       System.out.println("삭제 완료");
			       Stage primaryStage =(Stage)content.getScene().getWindow();
			       primaryStage.close();
			       
			       this.parent.setNotetable();
			       
			       break program;
			     }
			}
		}
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}