package SystemOperator;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class SO_searchSchoolTelController {
	@FXML
	Label school, address1, address2, tel;

	private String op_id, op_name, op_nickName, op_phoneNum;
	private int op_code;
	private OutputStream os;
	private InputStream is;
	
	public void setOperator(Operator op) {
		op_id = op.getId();
		op_name = op.getName();
		op_nickName = op.getNickName();
		op_code = op.getCode();
		op_phoneNum = op.getPhoneNum();
	}
	
	public void setData(String school, String address, String tel) {
		this.school.setText(school);
		
		if(address.length()>30) {
			address1.setText(address.substring(0, 28));
			address2.setText(address.substring(28,address.length()-1));
		}
		else {
			address1.setText(address);
		}
		
		this.tel.setText(tel);
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
