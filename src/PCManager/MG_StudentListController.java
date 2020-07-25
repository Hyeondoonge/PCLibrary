package PCManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import Main.User;
import Server.Protocol;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MG_StudentListController {
	@FXML
	TableView myTableView;
	@FXML
	private TableColumn<MG_StudentContents, String> idColumn;
	@FXML
	private TableColumn<MG_StudentContents, String> nicknameColumn;
	@FXML
	private TableColumn<MG_StudentContents, String> nameColumn;
	@FXML
	private TableColumn<MG_StudentContents, String> phoneNumColumn;
	@FXML
	private TableColumn<MG_StudentContents, String> emailColumn;

	MG_SAV_StudentTable table;
	private User user;
	private OutputStream os = null;
	private InputStream is = null;
	
	public MG_StudentListController() {
		table = new MG_SAV_StudentTable();
	}

	public void setInform(User user) {
		this.user = user;
	}

	public void getTable() {
		table.setMyList(user.getSchool());
		
	}

	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}

	public void setTable(String studentList) {
		table.setMyList(studentList);
	}

	public void showStudentList() {
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		nicknameColumn.setCellValueFactory(cellData -> cellData.getValue().getNicknameProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		phoneNumColumn.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumProperty());
		emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
		myTableView.setItems(table.getMyList());
	}
}
