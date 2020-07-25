package SystemOperator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.mysql.cj.protocol.Resultset;

import Main.Post;
import Main.User;
import Server.Protocol;
import Student.applyforPCController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SO_manageUserController implements Initializable {

	@FXML
	ChoiceBox group;
	@FXML
	TableView<User> table;
	@FXML
	TableColumn id, name, school, grouping;
	 
	private String op_id, op_name, op_nickName, op_phoneNum;
	private int op_code;
	private OutputStream os;
	private InputStream is;
	private String data = "";
	private ObservableList<User> dataList;
	private int cnt = 0;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		group.getItems().add("선택안함");
		group.getItems().add("학생");
		group.getItems().add("관리직원");
	}
	
	public void setTable(String choice) {
		
		dataList = FXCollections.observableArrayList();
		
		try {
			Protocol protocol = new Protocol();
			byte[] buf = protocol.getPacket();
						    		
			protocol = new Protocol(Protocol.PT_REQ_USERLIST);
			
			if(choice.equals("선택안함")) {
				protocol.setCode("0");  //  학생 + 관리자
			}
			else if(choice.equals("학생")) {
				protocol.setCode("1");
			}
			else { // 관리직원
				protocol.setCode("2");
			}
			os.write(protocol.getPacket());

			program: while (true) {

			        is.read(buf); 

			        int packetType = buf[0];
			        protocol.setPacket(packetType, buf);
			            		
			        if (packetType == Protocol.PT_EXIT) {
			        	System.out.println("클라이언트 종료");
			        	break;
			        }
			            		
			         switch (packetType) { // Last 일 때 한번에

			            	case Protocol.PT_RES_USERLIST: 
			            		
			            	 this.data += protocol.getUserlist();
			           		
			           		 String isLast = protocol.getIslast();
			           		
			           		 
			           		 if(isLast.equals("0")) { // 마지막 X
			           			 protocol = new Protocol(protocol.PT_REQ_USERLIST);
			           			 os.write(protocol.getPacket());
			           			 
			           			 break;
			           		 }
			           		 else { // 마지막 O
			              		 StringTokenizer st = new StringTokenizer(this.data, "#");
			               		 
			              		 while(st.hasMoreTokens()) {
			               			 String userId = st.nextToken();
			               			 String userpw =st.nextToken();
			               			 String userName = st.nextToken();
			               			 String userSchool = st.nextToken();
			               			 String userGrouping = st.nextToken(); // 인증상태
			               			
			               			 String userNickname = st.nextToken();
			               			 String userTel = st.nextToken();
			               			 String userMail = st.nextToken();
			               			 String userState = st.nextToken();
			               			 
			               			 int tempGroup = 0;
			               			
			               			 if(userState.equals("1")) {
			               				 tempGroup = 1;
			               			 }
			               			 else {
			               				tempGroup = 2;
			               			 }
			               			 
			               			dataList.add(new User(userId, userpw,userName,userNickname,tempGroup, 
			               				 userTel,userMail, userSchool));
			               		 	}
			              		 	this.data = "";
			              		 	
				            		break program; 
				            		}
			            		}
			                    
				}
      		 
			 id.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
      		 name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
      		 school.setCellValueFactory(new PropertyValueFactory<User, String>("school"));
      		 grouping.setCellValueFactory(new PropertyValueFactory<User, String>("strCode"));
      		 
      		 table.setItems(dataList);
      		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		 table.setOnMouseClicked((MouseEvent event) -> {
	            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
	                
	        		try { //new Stage
	        			if(table.getSelectionModel().getSelectedItem().getCode() == 1) { // 학생 창 띄우고 데이터 전달
	        				
	        				FXMLLoader loader = new FXMLLoader(getClass().getResource("SO_manageStudent.fxml"));
	        				Stage primaryStage = new Stage();
	        				primaryStage.setScene(new Scene(loader.load()));
	        				primaryStage.show();
	        				
	        				SO_manageStudentController controller = loader.<SO_manageStudentController>getController();
	        				
	        				controller.setStream(os,is);
	        				controller.setParent(this);
	        				controller.setUser(table.getSelectionModel().getSelectedItem());
	        				controller.setOperator(new Operator(op_id, op_name, op_nickName, op_code, op_phoneNum));
		        			}
	        			
	        			else {
	        				FXMLLoader loader = new FXMLLoader(getClass().getResource("SO_manageManager.fxml"));
	        				Stage primaryStage = new Stage();
	        				primaryStage.setScene(new Scene(loader.load()));
	        				primaryStage.show();
	        				
	        				SO_manageManagerController controller = loader.<SO_manageManagerController>getController();
	        				controller.setStream(os,is);
	        				controller.setParent(this);
	        				controller.setUser(table.getSelectionModel().getSelectedItem());
	        				controller.setOperator(new Operator(op_id, op_name, op_nickName, op_code, op_phoneNum));
	        			}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		
	            }
	        });
	}

	
	public void setOperator(Operator op) {
		op_id = op.getId();
		op_name = op.getName();
		op_nickName = op.getNickName();
		op_code = op.getCode();
		op_phoneNum = op.getPhoneNum();
		
		setTable("선택안함");
	}
	
	public void choiceBoxHandler() {
		 setTable(group.getValue().toString());
	}
	
	public void setStream(OutputStream os, InputStream is) {
		this.os = os;
		this.is = is;
		}
	}
