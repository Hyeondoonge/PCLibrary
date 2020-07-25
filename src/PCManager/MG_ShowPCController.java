package PCManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import Main.User;
import Server.Protocol;
import Server.SQL;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class MG_ShowPCController {
	@FXML
	private TableView<MG_PCTableContents> myTableView;
	@FXML
	private TableColumn<MG_PCTableContents, String> ipColumn;
	@FXML
	private TableColumn<MG_PCTableContents, String> companyColumn; // �ʿ� ���� �� ���� �̹� ���� ȸ��� ����Ǿ��ִ°�???
	@FXML
	private TableColumn<MG_PCTableContents, String> modelColumn;
	@FXML
	private TableColumn<MG_PCTableContents, String> cpuColumn;
	@FXML
	private TableColumn<MG_PCTableContents, String> gpuColumn;
	@FXML
	private TableColumn<MG_PCTableContents, Integer> ramColumn;
	@FXML
	private TableColumn<MG_PCTableContents, String> idColumn;
	@FXML
	private TableColumn<MG_PCTableContents, String> passwordColumn;
	@FXML
	private TableColumn<MG_PCTableContents, String> stateColumn;

	private User user;
	private MG_SelectManWindowController parent;
	private MG_SAV_PCTable table;
	private OutputStream os;
	private InputStream is;
	private Protocol protocol = null;
	private byte[] buf = null;

	public MG_ShowPCController() {
		table = new MG_SAV_PCTable();
	}

	public void registerPC() throws IOException {
		protocol = new Protocol();
		buf = protocol.getPacket();
		
		protocol = new Protocol(Protocol.PT_REQ_WINDOW);
		protocol.setCode("2");
		os.write(protocol.getPacket());

		while (true) {
			is.read(buf);

			int packetType = buf[0];
			protocol.setPacket(packetType, buf);

			switch (packetType) {
			case Protocol.PT_RES_WINDOW:
				if ((protocol.getCode()).equals("2")) {
					Stage primaryStage = new Stage();
					FXMLLoader mypage = new FXMLLoader(getClass().getResource("MG_PCRegistration.fxml"));
					primaryStage.setScene(new Scene(mypage.load()));
					primaryStage.setTitle("PC ���");
					primaryStage.show();
					MG_PCRegistrationController controller = mypage.<MG_PCRegistrationController>getController();
					//controller.setInform(user);
					controller.setParent(this); // ���߿� ��ȸ�ϴ°Ŵ� ��� ��� ���� �����غ���
					controller.setStream(os, is);
					return;
				}
			}
		}
	}

	public void deletePC() throws IOException {
		MG_PCTableContents selectItemContents = myTableView.getSelectionModel().getSelectedItem();
		Alert alert;
		if (selectItemContents == null) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�˸� �޽���");
			alert.setHeaderText(null);
			alert.setContentText("���õ� �׸��� �����ϴ�.");
			alert.show();
		} else {		
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("�˸� �޽���");
			alert.setHeaderText(null);
			alert.setContentText("���õ� �׸��� �����Ͻðڽ��ϱ�?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				protocol = new Protocol();
				buf = protocol.getPacket();
				
				protocol = new Protocol(Protocol.PT_REQ_DELETEPC);
				protocol.setDeletePCInfo(selectItemContents.getIpProperty().getValue());
				os.write(protocol.getPacket());
				
				while(true) {
					is.read(buf);
					
					int packetType = buf[0];
					protocol.setPacket(packetType, buf);
					System.out.println("��ŶŸ�� " + packetType);
					
					switch(packetType) {
					case Protocol.PT_RES_DELETEPC:
						Alert confirm = new Alert(AlertType.INFORMATION);
						confirm.setTitle("�˸� �޽���");
						confirm.setHeaderText(null);
						if(protocol.getCode().equals("1")) {
							confirm.setContentText("������ �Ϸ�Ǿ����ϴ�.");
							confirm.show();
							
							protocol = new Protocol();
							buf = protocol.getPacket();

							protocol = new Protocol(Protocol.PT_REQ_PCLIST);
							os.write(protocol.getPacket());
							String res = "";
							
							while(true) {
								is.read(buf);
								
								packetType = buf[0];
								protocol.setPacket(packetType, buf);
								
								switch(packetType) {
								case Protocol.PT_RES_PCLIST:
									this.setTable(protocol.getDataAboutPC());
									this.showPCList();
									return;
								}
							}
							
							//myTableView.getItems().remove(selectItemContents); // �̷��� ���� ���� SQL ���ô�
						}else {
							confirm.setContentText("�̹� ������ �׸��Դϴ�.");
							confirm.show();
						}
						return;
					}
				}
				
				//SQL sql = new SQL();
				//sql.deletePC(selectItemContents.getIpProperty().getValue());		
			}
		}
	} 

	public void adjustPC() throws IOException {
		MG_PCTableContents selectItemContents = myTableView.getSelectionModel().getSelectedItem();
		if (selectItemContents == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�˸� �޽���");
			alert.setHeaderText(null);
			alert.setContentText("���õ� �׸��� �����ϴ�.");
			alert.show();
		} else {
			protocol = new Protocol();
			buf = protocol.getPacket();
			protocol = new Protocol(Protocol.PT_REQ_WINDOW);
			protocol.setWindowNum("3");
			//protocol.setSelectedPC(ip+"#"+company+"#"+model+"#"+cpu+"#"+gpu+"#"+ram+"#"+id+"#"+password+"#"+state+"#");
			os.write(protocol.getPacket());
			
			while(true) {
				is.read(buf);
				int packetType = buf[0];
				protocol.setPacket(packetType, buf);
				
				switch(packetType) {
				case Protocol.PT_RES_WINDOW:
					if(protocol.getCode().equals("1")) {
						String ip = selectItemContents.getIpProperty().getValue();
						String company = selectItemContents.getCompanyProperty().getValue();
						String model = selectItemContents.getModelProperty().getValue();
						String cpu = selectItemContents.getCPUProperty().getValue();
						String gpu = selectItemContents.getGPUProperty().getValue();
						int ram = selectItemContents.getRAMSizeProperty().getValue();
						String id = selectItemContents.getIdProperty().getValue();
						String password = selectItemContents.getPasswordProperty().getValue();
						String state = selectItemContents.getStateProperty().getValue();
						
						//System.out.println(ip+"#"+company+"#"+model+"#"+cpu+"#"+gpu+"#"+ram+"#"+id+"#"+password+"#"+state+"#");
						
						Stage primaryStage = new Stage();
						FXMLLoader mypage = new FXMLLoader(getClass().getResource("MG_PCModification.fxml"));
						primaryStage.setScene(new Scene(mypage.load()));
						primaryStage.setTitle("PC ����");
						primaryStage.show();

						MG_PCModificationController controller = mypage.<MG_PCModificationController>getController();
						controller.setInform(user, ip, company, model, cpu, gpu, ram, id, password, state);
						controller.setParent(this);
						controller.setStream(os, is);
						return;
					}
				}
			}
		}
	} // ����̶� â ��縸 �Ȱ��� ���� ������ �����´ٴ� ���̰� �ִ�.

	public void setInform(User user) {
		this.user = user;
	}

	public void showPCList() {
		ipColumn.setCellValueFactory(cellData -> cellData.getValue().getIpProperty());
		companyColumn.setCellValueFactory(cellData -> cellData.getValue().getCompanyProperty());
		modelColumn.setCellValueFactory(cellData -> cellData.getValue().getModelProperty());
		cpuColumn.setCellValueFactory(cellData -> cellData.getValue().getCPUProperty());
		gpuColumn.setCellValueFactory(cellData -> cellData.getValue().getGPUProperty());
		ramColumn.setCellValueFactory(cellData -> cellData.getValue().getRAMSizeProperty().asObject());
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		passwordColumn.setCellValueFactory(cellData -> cellData.getValue().getPasswordProperty());
		stateColumn.setCellValueFactory(cellData -> cellData.getValue().getStateProperty());
		myTableView.setItems(table.getMyList());
	}

	public void setTable(String data) {
		table.setMyList(data);
	}

	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}

	public void setParent(MG_SelectManWindowController mg_SelectManWindowController) {
		System.out.println("parent ����");
		this.parent = mg_SelectManWindowController;
	}
}
