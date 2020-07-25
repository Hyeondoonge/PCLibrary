package PCManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.mysql.cj.protocol.Resultset;

import Main.User;
import Server.MessageSender;
import Server.Protocol;
import Server.SQL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MG_PCLenderListController{
	@FXML
	private TableView<MG_PCLenderContents> myTableView;
	@FXML
	private TableColumn<MG_PCLenderContents, String> lenderIdColumn;
	@FXML
	private TableColumn<MG_PCLenderContents, String> nameColumn;
	@FXML
	private TableColumn<MG_PCLenderContents, String> ipColumn;
	@FXML
	private TableColumn<MG_PCLenderContents, String> pcColumn;
	@FXML
	private TableColumn<MG_PCLenderContents, String> pcIdColumn;
	@FXML
	private TableColumn<MG_PCLenderContents, String> passwordColumn;
	@FXML
	Button message;

	private OutputStream os = null;
	private InputStream is = null;
	private Protocol protocol = null;
	private byte[] buf = null;
	
	MG_SAV_PCLenderTable table;
	private User user;
	
	public MG_PCLenderListController() {
		table = new MG_SAV_PCLenderTable();
	}
	
	public void sendMessage() throws IOException {
		protocol = new Protocol();
		buf = protocol.getPacket();

		protocol = new Protocol(Protocol.PT_REQ_TARGET_PHONENUM);
		os.write(protocol.getPacket());
	
		while(true) {
			is.read(buf);

			int packetType = buf[0];
			protocol.setPacket(packetType, buf);
			
			switch(packetType) {
			case Protocol.PT_RES_TARGET_PHONENUM:
				ArrayList<String> phoneNumList = new ArrayList();
				String info = protocol.getTargetPhoneNum();
				StringTokenizer st = new StringTokenizer(info, "#");
			
				String managerNum = st.nextToken();
				System.out.println("������ ��ȣ�� : " + managerNum);
				
				while(st.hasMoreTokens()) {
					phoneNumList.add(st.nextToken());
				}
				
				int cnt = phoneNumList.size();
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("�˸� �޽���");
				alert.setHeaderText(null);
				alert.setContentText("�˸� �޽��� ���� ���(7�� �� PC �ݳ�) �л��� �� "+ cnt +"���Դϴ�. �޽����� �����Ͻðڽ��ϱ�?");
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK) {
					MessageSender sender = new MessageSender();
					sender.sendMessage(phoneNumList, managerNum);
//					for(int i = 0; i < phoneNumList.size(); i++) {
//						System.out.println(phoneNumList.get(i));
//					}
					System.out.println("�޽��� ���´�");
					Alert confirm = new Alert(AlertType.INFORMATION);
					confirm.setTitle("�˸� �޽���");
					confirm.setHeaderText(null);
					confirm.setContentText("�޽��� ������ �Ϸ�Ǿ����ϴ�.");
					confirm.show();
				}
				return;
			}
		}
		
//		try {
//			ArrayList<String> phoneNumList = new ArrayList();
//			while(res.next()) {
//				phoneNumList.add(res.getString(1));
//				System.out.println("���ϱ�");
//			}
//			int colNum = phoneNumList.size();
//			
//			Alert alert = new Alert(AlertType.CONFIRMATION);
//			alert.setTitle("�˸� �޽���");
//			alert.setHeaderText(null);
//			alert.setContentText("�˸� �޽��� ���� ���(7�� �� PC �ݳ�) �л��� �� "+ colNum +"���Դϴ�. �޽����� �����Ͻðڽ��ϱ�?");
//			Optional<ButtonType> result = alert.showAndWait();
//			if(result.get() == ButtonType.OK) {
//				for(int i = 0; i < phoneNumList.size(); i++) {
//					System.out.println(phoneNumList.get(i));
//				}
//				System.out.println("�޽��� ���´�");
//				Alert confirm = new Alert(AlertType.INFORMATION);
//				confirm.setTitle("�˸� �޽���");
//				confirm.setHeaderText(null);
//				confirm.setContentText("�޽��� ������ �Ϸ�Ǿ����ϴ�.");
//				confirm.show();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	public void setTable(String info) {
		table.setMyList(info);
	}
	
	public void showTable() { //�ٵ� ���ݾ� �ڵ��ݳ��ϴ� ������ �־�� ���� ������?
		//table.setMyList(user.getSchool());
		lenderIdColumn.setCellValueFactory(cellData -> cellData.getValue().getLenderIdProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		ipColumn.setCellValueFactory(cellData -> cellData.getValue().getIpProperty());
		pcColumn.setCellValueFactory(cellData -> cellData.getValue().getPcProperty());
		pcIdColumn.setCellValueFactory(cellData -> cellData.getValue().getPcIdProperty());
		passwordColumn.setCellValueFactory(cellData -> cellData.getValue().getPasswordProperty());
		myTableView.setItems(table.getMyList());
	}

	public void setInform(User user) {
		this.user = user;
		
	}

	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
