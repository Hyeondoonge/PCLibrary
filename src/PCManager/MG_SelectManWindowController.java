package PCManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import Main.User;
import Main.guser_NoteController;
import Server.Protocol;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MG_SelectManWindowController {

	@FXML
	Button myPage;

	@FXML
	Button serverManagement;

	@FXML
	Button showLenderList;

	@FXML
	Button showStudentList,showLenderList1;

	@FXML
	Label manager;

	private User user;
	private OutputStream os = null;
	private InputStream is = null;
	private Protocol protocol = null;
	private byte[] buf = null;

	public void loadMyPage() throws IOException {
		protocol = new Protocol();
		buf = protocol.getPacket();

		protocol = new Protocol(Protocol.PT_REQ_MAG_MYPAGE);
		os.write(protocol.getPacket());

		while (true) {
			is.read(buf);

			int packetType = buf[0];
			protocol.setPacket(packetType, buf);

			switch (packetType) {
			case Protocol.PT_RES_MAG_MYPAGE:
				System.out.println(protocol.getMyPage());
				Stage primaryStage = new Stage();
				FXMLLoader mypage = new FXMLLoader(getClass().getResource("MG_MyPage.fxml"));
				primaryStage.setScene(new Scene(mypage.load()));
				primaryStage.setTitle("����������");
				primaryStage.show();
				// â ����ִ� �� �ٷ� �ؿ�
				MG_MyPageController controller = mypage.<MG_MyPageController>getController();
				// controller.setInform(user);
				controller.setMyPage(protocol.getMyPage());
				controller.setStream(os, is);
				return;
			}
		}
	}

	public void loadServerManagement() throws IOException {
		protocol = new Protocol();
		buf = protocol.getPacket();

		protocol = new Protocol(Protocol.PT_REQ_PCLIST);
		os.write(protocol.getPacket());
		String res = "";

		while (true) {
			is.read(buf);

			int packetType = buf[0];
			protocol.setPacket(packetType, buf);

			switch (packetType) {
			case Protocol.PT_RES_PCLIST:
				Stage primaryStage = new Stage();
				FXMLLoader mypage = new FXMLLoader(getClass().getResource("MG_showPC.fxml"));
				primaryStage.setScene(new Scene(mypage.load()));
				primaryStage.setTitle("PC ����");
				primaryStage.show();
				MG_ShowPCController controller = mypage.<MG_ShowPCController>getController();
				controller.setStream(os, is);
				controller.setTable(protocol.getDataAboutPC());
				// controller.setInform(user);
				controller.showPCList();
				controller.setParent(this);

				return;
			}
		}
	} // PC ����

	public void loadPCLenderList() throws IOException {
		protocol = new Protocol();
		buf = protocol.getPacket();

		protocol = new Protocol(Protocol.PT_REQ_LENDERLIST);
		os.write(protocol.getPacket());
		String res = "";

		while (true) {
			is.read(buf);

			int packetType = buf[0];
			protocol.setPacket(packetType, buf);

			switch (packetType) {
			case Protocol.PT_RES_LENDERLIST:
				String info = protocol.getPCLenderList();
				Stage primaryStage = new Stage();
				FXMLLoader mypage = new FXMLLoader(getClass().getResource("MG_PCLenderList.fxml"));
				System.out.println(mypage);
				primaryStage.setScene(new Scene(mypage.load()));
				primaryStage.setTitle("�뿩�� ���");
				primaryStage.show();
				MG_PCLenderListController controller = mypage.<MG_PCLenderListController>getController();
				controller.setStream(os, is);
				controller.setTable(info);
				controller.showTable();
				return;
			}
		}		
	} // �뿩�� ���

	public void loadNotice() throws IOException {
		protocol = new Protocol();
		buf = protocol.getPacket();

		protocol = new Protocol(Protocol.PT_REQ_WINDOW);
		os.write(protocol.getPacket());

		while (true) {
			is.read(buf);

			int packetType = buf[0];
			protocol.setPacket(packetType, buf);
			
			switch(packetType) {
			case Protocol.PT_RES_WINDOW:
				Stage primaryStage = new Stage();
				FXMLLoader mypage = new FXMLLoader(getClass().getResource("/Main/guser_Note.fxml"));
				primaryStage.setScene(new Scene(mypage.load()));
				primaryStage.setTitle("��������");
				primaryStage.show();
				guser_NoteController controller = mypage.<guser_NoteController>getController();
				controller.setStream(os, is);
				controller.setUser(user);
				//controller.setNotetable();
				return;
			}
		}

	} // ��������

	public void loadStudentList() throws IOException {
		protocol = new Protocol();
		buf = protocol.getPacket();

		protocol = new Protocol(Protocol.PT_REQ_STUDENTLIST);
		os.write(protocol.getPacket());

		while (true) {
			is.read(buf);

			int packetType = buf[0];
			protocol.setPacket(packetType, buf);
			System.out.println("��Ŷ Ÿ�� : " + packetType);

			switch (packetType) {
			case Protocol.PT_RES_STUDENTLIST:
				Stage primaryStage = new Stage();
				FXMLLoader mypage = new FXMLLoader(getClass().getResource("MG_StudentList.fxml"));
				primaryStage.setScene(new Scene(mypage.load()));
				primaryStage.setTitle("�л� ȸ�� ���");
				primaryStage.show();
				MG_StudentListController controller = mypage.<MG_StudentListController>getController();
				controller.setStream(os, is);
				controller.setTable(protocol.getStudentList());
				// controller.setInform(user);
				controller.showStudentList();
				return;
			}
		}
	} // �л� ���

	public void setSelectwindow() throws IOException {
		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();
					    		
		protocol = new Protocol(Protocol.PT_REQ_CERTIFICATE_MANAGER);
		protocol.setId(user.getId());
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

		            	case Protocol.PT_RES_CERTIFICATE_MANAGER: 
		            		
		            		String code = protocol.getCode();
		            		
		            		if(code.equals("1")) {
		            			serverManagement.setDisable(true);
		            			showStudentList.setDisable(true);
		            			showLenderList1.setDisable(true);
		            		}
		            		
		            		break program;
		            			}
		                    }
			}
	
	public void setUser(User user) throws IOException {
		this.user = user;
		manager.setText(user.getId() + "(" + user.getNickName() + ")");
		
		setSelectwindow();
	}

	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
