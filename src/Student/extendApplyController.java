package Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import Main.User;
import Server.Protocol;

public class extendApplyController implements Initializable{
	
	@FXML
	Label currentDate;
	@FXML
	Label alarm1;
	@FXML
	Label alarm2;
	@FXML
	DatePicker date;
	
	private String id, pw, name, nickName, phoneNum, mail, school;
	private int code;

	private OutputStream os;
	private InputStream is;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		alarm1.setVisible(false);
		alarm2.setVisible(false);
	}
	
	public void setUser(User user) {
		id = user.getId();
		pw = user.getPassword();
		name = user.getName();
		nickName = user.getNickName();
		code = user.getCode();
		phoneNum = user.getPhoneNum();
		mail = user.getMail();
		school= user.getSchool();

		try {
			Protocol protocol = new Protocol();
			byte[] buf = protocol.getPacket();
						    		
			protocol = new Protocol(Protocol.PT_REQ_MYPC_RETURNDATE);
			protocol.setId(id);
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

		            	case Protocol.PT_RES_MYPC_RETURNDATE: 
		            			
		            		currentDate.setText(protocol.getData());
		            		
		            		break program;
		            			}
		                    }
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void applyBtnHandler() throws IOException, ParseException{ // 날짜 확인
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
	
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // 주의!
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
		
		if(date.getValue()!=null) {
			Date currentD = format.parse(currentDate.getText());
			
			Calendar cal = Calendar.getInstance(); // Date 연산함수사용을 위해 선언
			cal.setTime(currentD);
			cal.add(Calendar.DATE, 14); 
			
			Date after14 = cal.getTime();//14일 후의 값을 date타입의 변수에다 저장
			System.out.print(date.getValue());
			Date selectD = format2.parse(date.getValue().format(DateTimeFormatter.BASIC_ISO_DATE));
			//LocalDate 타입을 date타입으로 변환
			//date타입 변수끼리 비교가능
			if(selectD.after(after14)){ // 2주후의 날짜 < 선택한 날짜
				alarm1.setVisible(true);
				alarm2.setVisible(true);
			}
			else if(selectD.before(currentD)){ // 선택한 날짜가 현재날짜보다 앞의 날짜일  경우
				alert.setContentText("올바른 날짜를 선택하세요");
				alert.show();
			}
			else {
				
				Protocol protocol = new Protocol();
				byte[] buf = protocol.getPacket();
							    		
				protocol = new Protocol(Protocol.PT_REQ_EXTENDAPPLY); //  선택한 날짜 전송
				protocol.setId(id);
				protocol.setPCExtendDate(""+date.getValue());
				
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

				            	case Protocol.PT_RES_EXTENDAPPLY: 

				    				alert.setContentText("성공적으로 연장처리 되었습니다");
				    				alert.show();
				    				
				    				FXMLLoader loader = new FXMLLoader(getClass().getResource("showPCinform.fxml"));
				    				Parent second = loader.load();// 자동으로 뒤로가기
				    				Scene scene = new Scene(second);
				    				Stage primaryStage =(Stage)date.getScene().getWindow();
				    				primaryStage.setTitle("대여 중인 PC");
				    				primaryStage.setScene(scene);
				    				
				    				showPCinformController controller = loader.<showPCinformController>getController();
				    				controller.setStream(os, is);
				    				controller.setUser(new User(id, pw, name, nickName, code, phoneNum, mail, school));
				            		
				    				break program;
				            			}
				                    }
			}
		}
		else {
			alert.setContentText("원하시는 만료날짜를 선택하세요");
			alert.show();
		}
	}
	
	public void cancelHandler() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("showPCinform.fxml"));
		Parent second = loader.load();// 자동으로 뒤로가기
		Scene scene = new Scene(second);
		Stage primaryStage =(Stage)date.getScene().getWindow();
		primaryStage.setTitle("대여 중인 PC");
		primaryStage.setScene(scene);
		
		showPCinformController controller = loader.<showPCinformController>getController();
		controller.setStream(os, is);
		controller.setUser(new User(id, pw, name, nickName, code, phoneNum, mail, school));
		
	}
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
