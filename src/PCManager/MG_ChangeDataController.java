package PCManager;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class MG_ChangeDataController {
	@FXML
	Button formChecking;
	@FXML
	PasswordField pw;
	@FXML
	PasswordField pwChecking;
	@FXML
	TextField phoneNum;
	@FXML
	Button save;
	@FXML
	Button cancel;

	boolean isCheckedPw = false;

	public void checkForm() {
		String pwData = pw.getText();
		System.out.println(pwData.length());
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("�ȳ� �޽���");
		alert.setHeaderText(null);
		if (pwData.length() < 8) {
			alert.setContentText("�ּ� 8������ ��й�ȣ�� �Է��Ͻʽÿ�.");
		} else {
			int numCnt = 0;
			int alpaCnt = 0;
			int markCnt = 0;
			for (int i = 0; i < pwData.length(); i++) {
				if ('0' <= pwData.charAt(i) && pwData.charAt(i) <= '9') {
					numCnt++;
				} else if ('A' <= pwData.charAt(i) && pwData.charAt(i) <= 'z') {
					alpaCnt++;
				} else if (pwData.charAt(i) == '!' || pwData.charAt(i) == '^' || pwData.charAt(i) == '@'
						|| pwData.charAt(i) == '~') {
					markCnt++;
				}
			}
			if ((numCnt + alpaCnt + markCnt != pwData.length()) || (numCnt == 0 || alpaCnt == 0 || markCnt == 0)) {
				alert.setContentText("�Է� ������ �߸��Ǿ����ϴ�. \n (����, ����, !,^,@,~ ȥ�� ���)");
			} else {
				alert.setContentText("��� ������ ��й�ȣ�Դϴ�.");
				isCheckedPw = true;
			}
		}
		alert.show();
	}

	public void saveData() {
		Alert alert;
		if (!isCheckedPw) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�ȳ� �޽���");
			alert.setHeaderText(null);
			alert.setContentText("��й�ȣ ������ Ȯ�����ּ���.");
			alert.show();
		} else if (!(pw.getText()).equals(pwChecking.getText())) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�ȳ� �޽���");
			alert.setHeaderText(null);
			alert.setContentText("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
			alert.show();
		} else if ((phoneNum.getText()).equals("")) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�ȳ� �޽���");
			alert.setHeaderText(null);
			alert.setContentText("��ȭ��ȣ�� �Է����ּ���.");
			alert.show();
		} else {
			String pn = phoneNum.getText();
			for (int i = 0; i < pn.length(); i++) {
				if (!('0' <= pn.charAt(i) && pn.charAt(i) <= '9')) {
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("�ȳ� �޽���");
					alert.setHeaderText(null);
					alert.setContentText("�ùٸ� �������� �Է����ּ���.");
					alert.show();
					return;
				}
			}
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText(null);
			alert.setContentText("ȸ�������� �����Ͻðڽ��ϱ�?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("�ȳ� �޽���");
				alert.setHeaderText(null);
				alert.setContentText("������ �Ϸ�Ǿ����ϴ�.");
				Optional<ButtonType> confirm = alert.showAndWait();
				if(confirm.get() == ButtonType.OK) {
					Stage stage = (Stage) cancel.getScene().getWindow();
					stage.close();
				}
				
			}
		}
	}

	public void closeWindow() {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}
}
