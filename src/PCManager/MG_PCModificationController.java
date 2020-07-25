package PCManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.spi.StateFactory;

import Main.User;
import Server.Protocol;
import Server.SQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MG_PCModificationController implements Initializable {

	@FXML
	ComboBox<String> companyBox;
	@FXML
	ComboBox<String> pcModelBox;
	@FXML
	ComboBox<String> CPUBox;
	@FXML
	ComboBox<String> GPUBox;
	@FXML
	ComboBox<String> stateBox;
	@FXML
	TextField ipField;
	@FXML
	TextField idField;
	@FXML
	TextField passwordField;
	@FXML
	TextField ramField;
	@FXML
	Button ok;
	@FXML
	Button cancel;

	private User user;
	private String cpu;
	private String gpu;
	private String company;
	private String state;
	private String pcModel;
	private String ip;
	private String id;
	private String ram; // 여기서는 문자열 취급
	private String password;
	private String standardIp;

	private MG_ShowPCController parent;

	private OutputStream os = null;
	private InputStream is = null;
	private Protocol protocol = null;
	private byte[] buf = null;
	private SQL sql;

	private ObservableList<String> CPUList = FXCollections.observableArrayList();
	private ObservableList<String> GPUList = FXCollections.observableArrayList();
	private ObservableList<String> companyList = FXCollections.observableArrayList();
	private ObservableList<String> stateList = FXCollections.observableArrayList();
	private ObservableList<String> pcModelList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sql = new SQL();
		setCompanyBox(sql.getCompany());
		setCPUBox(sql.getCPU());
		setGPUBox(sql.getGPU());
		setState(sql.getState());

		CPUBox.setItems(CPUList);
		GPUBox.setItems(GPUList);
		companyBox.setItems(companyList);
		stateBox.setItems(stateList);
		pcModelBox.setItems(pcModelList);
		cpu = null;
		gpu = null;
		company = null;
		state = null;
		pcModel = null;
		standardIp = ipField.getText();
	}

	public void setCompanyBox(ResultSet res) {
		try {
			while (res.next()) {
				companyList.add(res.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setCPUBox(ResultSet res) {
		try {
			while (res.next()) {
				CPUList.add(res.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setGPUBox(ResultSet res) {
		try {
			while (res.next()) {
				GPUList.add(res.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setPCModel(ResultSet res) {
		try {
			pcModelList.clear();
			while (res.next()) {
				pcModelList.add(res.getString(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setState(ResultSet res) {
		try {
			while (res.next()) {
				stateList.add(res.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void chooseCPU(ActionEvent event) {
		cpu = CPUBox.getValue();
	}

	public void chooseGPU(ActionEvent event) {
		gpu = GPUBox.getValue();

	}

	public void chooseCompany(ActionEvent event) {
		company = companyBox.getValue();
		System.out.println(company);
		setPCModel(sql.getPC(company));
	}

	public void chooseState(ActionEvent event) {
		state = stateBox.getValue();
	}

	public void choosePCModel(ActionEvent event) {
		pcModel = pcModelBox.getValue();
	}

	public void confirm() throws IOException {
		ip = ipField.getText();
		id = idField.getText();
		ram = ramField.getText();
		password = passwordField.getText();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);

		if (company == null) {
			alert.setContentText("PC 제조사를 선택하십시오.");
			alert.show();
			return;
		} else if (pcModel == null) {
			alert.setContentText("PC 모델을 선택하십시오.");
			alert.show();
			return;
		} else if (cpu == null) {
			alert.setContentText("CPU를 선택하십시오.");
			alert.show();
			return;
		} else if (gpu == null) {
			alert.setContentText("GPU를 선택하십시오.");
			alert.show();
			return;
		} else if (state == null) {
			alert.setContentText("PC 상태를 선택하십시오.");
			alert.show();
			return;
		} else if (ip.equals("")) {
			alert.setContentText("IP 주소를 입력하십시오.");
			alert.show();
			return;
		} else {
			if (!ip.equals("")) {
				Pattern p = Pattern.compile("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}");
				Matcher m = p.matcher(ip);
				if (!m.find()) {
					alert.setContentText("올바른 형식으로 입력하십시오. \n ex) 192.168.0.19");
					alert.show();
					return;
				}
			}
		}

		if (id.equals("")) {
			alert.setContentText("PC 아이디를 입력하십시오.");
			alert.show();
			return;
		} else if (password.equals("")) {
			alert.setContentText("PC 비밀번호를 입력하십시오.");
			alert.show();
			return;
		} else if (ram.equals("")) {
			alert.setContentText("PC RAM(GB) 크기를 입력하십시오.");
			alert.show();
			return;
		} else if (!ram.equals("")) {
			Pattern p = Pattern.compile("^[0-9]*$");
			Matcher m = p.matcher(ram);
			if (m.find()) {
				protocol = new Protocol();
				buf = protocol.getPacket();

				String res = standardIp + "#" + ip + "#" + pcModel + "#" + cpu + "#" + gpu + "#" + ram + "#" + id + "#"
						+ password + "#" + state + "#";

				protocol = new Protocol(Protocol.PT_REQ_MODIFYPC);
				protocol.setModifyPCInfo(res);

				os.write(protocol.getPacket());

				// SQL sql = new SQL();
				// sql.modifyPC(standardIp, this.ip, pcModel, cpu, gpu, Integer.parseInt(ram),
				// id, password, user.getSchool(), state);

				while (true) {
					is.read(buf);

					int packetType = buf[0];
					protocol.setPacket(packetType, buf);
					System.out.println("타입 " + packetType);

					switch (packetType) {
					case Protocol.PT_RES_MODIFYPC:
						if (protocol.getCode().equals("1")) {
							Stage stage = (Stage) ok.getScene().getWindow();
							alert.setContentText("PC 수정이 완료되었습니다.");
							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() == ButtonType.OK) {
								///
								protocol = new Protocol(Protocol.PT_REQ_PCLIST);
								os.write(protocol.getPacket());

								while (true) {
									is.read(buf);

									packetType = buf[0];
									protocol.setPacket(packetType, buf);

									switch (packetType) {
									case Protocol.PT_RES_PCLIST:
										parent.setTable(protocol.getDataAboutPC());
										parent.showPCList();
										stage.close();
										return;
									}
								}
							}
						} else {
							alert.setContentText("다시 확인해주시기 바랍니다.");
						}
						return;
					}
				}

			} else {
				alert.setContentText("PC RAM(GB) 크기는 0 이상의 정수로 입력하십시오.");
				alert.show();
			}
		}
	}

	public void cancel() {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}

	public void setParent(MG_ShowPCController parent) {
		this.parent = parent;
	}

	public void setInform(User user, String ip, String company, String model, String cpu, String gpu, int ram,
			String id, String password, String state) {
		this.user = user;
		CPUBox.setValue(cpu);
		GPUBox.setValue(gpu);
		companyBox.setValue(company);
		stateBox.setValue(state);
		pcModelBox.setValue(model);
		standardIp = ip;
		ipField.setText(ip);
		idField.setText(id);
		passwordField.setText(password);
		ramField.setText(Integer.toString(ram));
	}

	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
