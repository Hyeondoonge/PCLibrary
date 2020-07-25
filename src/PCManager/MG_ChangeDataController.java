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
		alert.setTitle("안내 메시지");
		alert.setHeaderText(null);
		if (pwData.length() < 8) {
			alert.setContentText("최소 8글자의 비밀번호를 입력하십시오.");
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
				alert.setContentText("입력 형식이 잘못되었습니다. \n (영문, 숫자, !,^,@,~ 혼합 사용)");
			} else {
				alert.setContentText("사용 가능한 비밀번호입니다.");
				isCheckedPw = true;
			}
		}
		alert.show();
	}

	public void saveData() {
		Alert alert;
		if (!isCheckedPw) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("안내 메시지");
			alert.setHeaderText(null);
			alert.setContentText("비밀번호 형식을 확인해주세요.");
			alert.show();
		} else if (!(pw.getText()).equals(pwChecking.getText())) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("안내 메시지");
			alert.setHeaderText(null);
			alert.setContentText("비밀번호가 일치하지 않습니다.");
			alert.show();
		} else if ((phoneNum.getText()).equals("")) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("안내 메시지");
			alert.setHeaderText(null);
			alert.setContentText("전화번호를 입력해주세요.");
			alert.show();
		} else {
			String pn = phoneNum.getText();
			for (int i = 0; i < pn.length(); i++) {
				if (!('0' <= pn.charAt(i) && pn.charAt(i) <= '9')) {
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("안내 메시지");
					alert.setHeaderText(null);
					alert.setContentText("올바른 형식으로 입력해주세요.");
					alert.show();
					return;
				}
			}
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText(null);
			alert.setContentText("회원정보를 수정하시겠습니까?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("안내 메시지");
				alert.setHeaderText(null);
				alert.setContentText("변경이 완료되었습니다.");
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
