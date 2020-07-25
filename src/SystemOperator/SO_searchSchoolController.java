package SystemOperator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.mysql.cj.protocol.Resultset;

import Server.Protocol;
import Server.SQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SO_searchSchoolController {
	
	@FXML
	TextField schoolInput;
	@FXML
	TableView<School> table;
	@FXML
	TableColumn school, address, tel;
	@FXML
	Button search;
	
	private String op_id, op_name, op_nickName, op_phoneNum;
	private int op_code;
	private OutputStream os;
	private InputStream is;
	private String data = "";
	
	public void setData() throws IOException {
		
		ObservableList<School> data = FXCollections.observableArrayList();
		String schoolList ;
		
		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();
					    		
		protocol = new Protocol(Protocol.PT_REQ_SCHOOLLIST);
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

		            	case Protocol.PT_RES_SCHOOLLIST: 
		            		
		            		schoolList = protocol.getSchoollist();
		            		this.data += schoolList;
		            		
		            		if(protocol.getIslast().equals("0")) { // 가져와야할 정보 더있다면
		            			protocol = new Protocol(Protocol.PT_REQ_SCHOOLLIST);
		            			os.write(protocol.getPacket());
		            			
		            			break;
		            		}
		            		else {
			            		
		            			StringTokenizer st = new StringTokenizer(this.data, "#");
			            		
			            		while(st.hasMoreTokens()) {
			            		
			            			String name = st.nextToken();
			            			String ad = st.nextToken();
			            			String tel = st.nextToken();
			            			
			            			if(tel.equals("null")) {
			            				tel = "-";
			            			}
			            			data.add(new School(name, ad, tel));
			            		}
			            		
			            		this.data = "";
			            		
			            		break program;
		            		}
		            		
		         		}
		            }
		
		 school.setCellValueFactory(new PropertyValueFactory<School, String>("school"));
  		 address.setCellValueFactory(new PropertyValueFactory<School, String>("address"));
  		 tel.setCellValueFactory(new PropertyValueFactory<School, String>("tel"));
  		 
  		 table.setItems(data);
	}
	
	public void setOperator(Operator op) throws IOException {
		op_id = op.getId();
		op_name = op.getName();
		op_nickName = op.getNickName();
		op_code = op.getCode();
		op_phoneNum = op.getPhoneNum();
		
		setData();
	}
	
	public void searchBtnHandler() throws IOException {
		
		if(schoolInput.getText().equals("")) {
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("알림메시지");
			alert.setHeaderText(null);
			alert.setContentText("검색내용을 입력하세요");
			alert.show();
		}
		
		else {
			
			Protocol protocol = new Protocol();
			byte[] buf = protocol.getPacket();
						    		
			protocol = new Protocol(Protocol.PT_REQ_SCHOOLDETAIL);
			protocol.setData(schoolInput.getText());
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

			            	case Protocol.PT_RES_SCHOOLDETAIL: 
			            			
			            		StringTokenizer st = new StringTokenizer(protocol.getData(),"#");
			            		int cnt = Integer.parseInt(st.nextToken());
			            		
			            		if(cnt == 0) {
			    					Alert alert = new Alert(AlertType.INFORMATION);
			    					alert.setTitle("알림메시지");
			    					alert.setHeaderText(null);
			    					alert.setContentText("검색 결과가 없습니다.");
			    					alert.show();
			    				}
			            		else if(cnt > 1){
			    					Alert alert = new Alert(AlertType.INFORMATION);
			    					alert.setTitle("알림메시지");
			    					alert.setHeaderText(null);
			    					alert.setContentText("검색 결과 : " + cnt + "건");
			    					alert.show();
			    				}
			    				else {
			    					String name = st.nextToken();
			            			String ad = st.nextToken();
			            			String tel = st.nextToken();
			            			
			            			if(tel.equals("null")) {
			            				tel = "-";
			            			}
			            			FXMLLoader mypage = new FXMLLoader(getClass().getResource("SO_searchSchoolTel.fxml"));
			            			Stage stage = new Stage();
			            			Parent root = mypage.load();
			            			SO_searchSchoolTelController controller = mypage.<SO_searchSchoolTelController>getController();
			            			controller.setStream(os, is);
			            			controller.setOperator(new Operator(op_id, op_name, op_nickName, op_code, op_phoneNum));
			                		controller.setData(name, ad, tel);
			            			stage.setScene(new Scene(root));
			                		stage.show();
			    				}
			            		
			            		break program;
			            			}
			                  
				}
		}
			
	}
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
	}
}
