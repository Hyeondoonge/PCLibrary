package  Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import Main.User;
import Server.Protocol;

public class showPCinformController {
	
	@FXML
	Label manufacturer;
	@FXML
	Label ip;
	@FXML
	Label id;
	@FXML
	Label pw;
	@FXML
	Label pc;
	@FXML
	Label cpu;
	@FXML
	Label gpu;
	@FXML
	Label ram;
	@FXML
	Label rDate;
	@FXML
	Label bDate;
	
	@FXML
	Label extendOX;
	
	boolean userState = true;

	private String ID, PW, name, nickName, phoneNum, mail, school;
	private int code;
	
	private OutputStream os;
	private InputStream is;
	private StringTokenizer st;
	
	public void setUser(User user) {
		ID = user.getId();
		PW = user.getPassword();
		name = user.getName();
		nickName = user.getNickName();
		code = user.getCode();
		phoneNum = user.getPhoneNum();
		mail = user.getMail();
		school= user.getSchool();
		
		System.out.println("showPCInform: "+user.getId());
		windowInitialize();
	}
	
	public void windowInitialize() {
		try {
			Protocol protocol = new Protocol();
			byte[] buf = protocol.getPacket();
						    		
			protocol = new Protocol(Protocol.PT_REQ_MYPCINFORM);
			protocol.setId(ID);
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

			            	case Protocol.PT_RES_MYPCINFORM: 
			            			
			            		st = new StringTokenizer(protocol.getData(), "#");

			            		pc.setText(st.nextToken());
			            		manufacturer.setText(st.nextToken());
			            		ip.setText(st.nextToken());
			            		id.setText(st.nextToken());
			            		pw.setText(st.nextToken());
			            		cpu.setText(st.nextToken());
			            		gpu.setText(st.nextToken());
			            		ram.setText(st.nextToken()+" GB");
			            		extendOX.setText(st.nextToken());
			            		bDate.setText(st.nextToken());
			            		rDate.setText(st.nextToken());
			            		
			            		break program;
			            			}
			                    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void extendBtnHandler() throws IOException {
		
		if(extendOX.getText().equals("O")) { // 가능
			FXMLLoader loader =  new FXMLLoader(getClass().getResource("extendApply.fxml"));
			Parent second = loader.load(); //연장신청 창
			Scene scene = new Scene(second);
			Stage primaryStage =(Stage)cpu.getScene().getWindow();
			primaryStage.setTitle("연장신청");
			primaryStage.setScene(scene);
			
			extendApplyController controller = loader.<extendApplyController>getController();
			controller.setStream(os, is);
			controller.setUser(new User(ID, PW, name, nickName, code, phoneNum, mail, school));
		}
		else { // 불가능
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("알림 메시지");
			alert.setHeaderText(null);
			alert.setContentText("연장신청이 불가능합니다");
			alert.show();
		}
	}
	
	public void returnBtnHandler() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
		alert.setContentText("사용 중인 PC를 반납하시겠습니까?");
		alert.showAndWait();
		
		if(alert.getResult().equals(ButtonType.OK)) {
		
			try {
			Protocol protocol = new Protocol();
			byte[] buf = protocol.getPacket();
						    		
			protocol = new Protocol(Protocol.PT_REQ_PCRETURN);
			protocol.setData(ip.getText());
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

			            	case Protocol.PT_RES_PCRETURN: 
			            		
			            		Stage stage = (Stage)(ip.getScene().getWindow());
			            		stage.close();
			            		
			            		break program;
			            		  }
			                    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
