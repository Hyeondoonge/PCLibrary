package SystemOperator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import Student.applyforPCController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Main.Post;
import Main.guser_showDocumentController;
import Server.Protocol;
import Server.SQL;

public class SO_NoteController {

	@FXML
	TableView<Post> table;
	@FXML
	TableColumn no;
	@FXML
	TableColumn title;
	@FXML
	TableColumn publisher;
	@FXML
	TableColumn date;
	@FXML
	TableColumn view;

	private String op_id, op_name, op_nickName, op_phoneNum;
	private int op_code;
	private ObservableList<Post> data = FXCollections.observableArrayList();
	private OutputStream os;
	private InputStream is;
	private Operator op = null;

	private Post selectedPost;

	public void setOperator(Operator op) throws IOException {

		op_id = op.getId();
		op_name = op.getName();
		op_nickName = op.getNickName();
		op_code = op.getCode();
		op_phoneNum = op.getPhoneNum();

		this.op = op;

		setNotetable();
	}

	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}

	public void setNotetable() throws IOException {

		this.data.clear();

		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();

		protocol = new Protocol(Protocol.PT_REQ_NOTELIST);
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

			case Protocol.PT_RES_NOTELIST:
				StringTokenizer st = new StringTokenizer(protocol.getData(), "#");
				int no, view;
				String title, publisher, date, content;

				while (st.hasMoreTokens()) {

					no = Integer.parseInt(st.nextToken());
					title = st.nextToken();
					publisher = st.nextToken();
					date = st.nextToken();
					view = Integer.parseInt(st.nextToken());
					content = st.nextToken();

					data.add(new Post(no, title, publisher, date, view, content));
				}
				break program;
			}
		}

		no.setCellValueFactory(new PropertyValueFactory<Post, Integer>("no"));
		title.setCellValueFactory(new PropertyValueFactory<Post, String>("title"));
		publisher.setCellValueFactory(new PropertyValueFactory<Post, String>("publisher"));
		date.setCellValueFactory(new PropertyValueFactory<Post, String>("date"));
		view.setCellValueFactory(new PropertyValueFactory<Post, Integer>("view"));

		table.setItems(data);

		showNote();

	}

	public void showNote() {
		// TODO Auto-generated method stub

		table.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				System.out.println(table.getSelectionModel().getSelectedItem());

				try {

					String no = Integer.toString(table.getSelectionModel().getSelectedItem().getNo());

					Protocol protocol = new Protocol();
					byte[] buf = protocol.getPacket();

					protocol = new Protocol(Protocol.PT_REQ_NOTE);
					protocol.setCode(no);
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

						case Protocol.PT_RES_NOTE:

							String data = protocol.getData();
							StringTokenizer st = new StringTokenizer(data, "#");

							FXMLLoader mypage = new FXMLLoader(getClass().getResource("SO_showDocument.fxml"));
							Stage stage = new Stage();
							Parent root = mypage.load();

							SO_showDocumentController controller = mypage.<SO_showDocumentController>getController();

							int id = Integer.parseInt(st.nextToken());
							String title = st.nextToken();
							String publisher = st.nextToken();
							String date = st.nextToken();
							int view = Integer.parseInt(st.nextToken());
							String content = st.nextToken();

							controller.setStream(os, is);
							controller.setDocument(id, title, publisher, date, view, content);
							stage.setTitle(title);
							stage.setScene(new Scene(root));
							stage.show();

							setNotetable();
							controller.setParent(this);
							controller.setOperator(op);
							break program;
						}
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void writeBtnHandler() throws IOException { // SO_writeDocument

		FXMLLoader loader = new FXMLLoader(getClass().getResource("SO_writeDocument.fxml"));
		Parent second = loader.load();
		Scene scene = new Scene(second);
		Stage primaryStage = (Stage) table.getScene().getWindow();
		primaryStage.setScene(scene);

		SO_writeDocumentController controller = loader.<SO_writeDocumentController>getController();
		controller.setStream(os, is);
		controller.setOperator(new Operator(op_id, op_name, op_nickName, op_code, op_phoneNum));

	}

	public void findBtnHandler() throws IOException { // 이미지 업로드
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알림 메시지");
		alert.setHeaderText(null);
		alert.setContentText("게시물이 업로드 되었습니다");
		alert.show();
	}
}
