package Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import Main.User;
import Server.Protocol;

public class applyforPCController implements Initializable {

		@FXML
		Label model, manufacturer, cpu, gpu, ram, applyDate, left; // applydate���� ���� ��¥
		@FXML
		DatePicker returnDate;
		@FXML
		CheckBox alarm;

		private OutputStream os;
		private InputStream is;
		
		private String id, pw, name, nickName, phoneNum, mail, school, checkAlarm = "0", ramSize;
		int code;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");

		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			
			Date time = new Date();
			
			applyDate.setText(format2.format(time));
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
			
		}
		
		public void setPCInform(String model, String cpu, String ram, 
				String manufacturer, String gpu, String left) {
			
			this.left.setText(left);
			this.model.setText(model);
			this.manufacturer.setText(manufacturer);
			this.cpu.setText(cpu);
			this.gpu.setText(gpu);
			this.ram.setText(ram+" GB");
			ramSize = ram;
		}
		
		public void saveBtnHandler() throws ParseException {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�˸� �޽���");
			alert.setHeaderText(null);
			
			if(returnDate.getValue() == null) {
				alert.setContentText("�ݳ����� �����ϼ���");
				alert.show();
			}
			
			else{ // �ݳ��� ���� ��
				
				Date applyD = format2.parse(applyDate.getText()); // ��û��
				Date returnD = format.parse(returnDate.getValue().format(DateTimeFormatter.BASIC_ISO_DATE)); // �ݳ���
				
				Calendar cal = Calendar.getInstance(); // Date �����Լ������ ���� ����
				cal.setTime(applyD);
				cal.add(Calendar.DATE, 31); 
				Date after31 = cal.getTime();
				
				
				if(returnD.before(applyD)) {
					alert.setContentText("�����Ͻ� �ݳ����� ���� �ʽ��ϴ�");
					alert.show();
				}
				else if(returnD.after(after31)){
					alert.setContentText("������ �뿩�Ⱓ�� �� �� �̳��Դϴ�");
					alert.show();
				}
				else {
					// DB�� �뿩�� ���� ���, ���ᳯ¥�� 2020-05-10�� �ٷ� �����ϸ� ��
					// ���ᳯ¥�˸����� ����
					if(alarm.isSelected()) {
						System.out.println("���ᳯ¥�˸��� �޽��ϴ�");
						checkAlarm = "1";
					}
					try {
						
						Protocol protocol = new Protocol();
						byte[] buf = protocol.getPacket();
									    		
						protocol = new Protocol(Protocol.PT_REQ_PCAPPLY);
						String applyData = id+"#"+manufacturer.getText()+"#"+model.getText()+"#"+
								cpu.getText()+"#"+gpu.getText()+"#"+ramSize+"#"+returnDate.getValue()+"#"+checkAlarm+"#";
						
						protocol.setPCApplyData(applyData);
						os.write(protocol.getPacket());

						program: while (true) {

						     is.read(buf); 

						        int packetType = buf[0];
						        protocol.setPacket(packetType, buf);
						            		
						        if (packetType == Protocol.PT_EXIT) {
						        	System.out.println("Ŭ���̾�Ʈ ����");
						        	break;
						        }
						            		
						         switch (packetType) { // �̹� �뿩�ߴ��� ���޴��� Ȯ��..
						         		 
						            	case Protocol.PT_RES_PCAPPLY : 

											alert.setContentText("PC�뿩 ��û�� ���������� �Ǿ����ϴ�");
											alert.show();
						            		break program;
						            			}
						                    }
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		
		public void cancelBtnHandler() throws IOException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PCList2.fxml"));
			Parent second = loader.load();
			Scene scene = new Scene(second);
			Stage primaryStage =(Stage)model.getScene().getWindow();
			primaryStage.setScene(scene);
			
			PCList2Controller controller = loader.<PCList2Controller>getController();
			controller.setStream(os, is);
			controller.setUser(new User(id, pw, name, nickName, code, phoneNum, mail, school));
		}
		
		public void setStream(OutputStream os, InputStream is) {
			this.os = os;
			this.is = is;
		}
}
