package PCManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import Main.User;
import Main.insertPasswordController;
import Server.Protocol;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class MG_MyPageController{
   @FXML
   Button info;
   @FXML
   Label manager;
   @FXML
   Label university;
   @FXML
   Label name;
   @FXML
   Label phoneNum;
   
   private User user;
   private OutputStream os = null;
   private InputStream is = null;
   private Protocol protocol = null;
   private byte[] buf = null;
   
   public void setUser(User user) {
      this.user = user;
      manager.setText(user.getId()+"("+user.getNickName()+")");
      university.setText(user.getSchool());
      name.setText(user.getName());
      
      if((user.getPhoneNum()).equals("null") || user.getPhoneNum() == null){
         phoneNum.setText("등록된 전화번호가 없습니다.");
      }else {
         phoneNum.setText(user.getPhoneNum());
      }
   }
   
   public void reviseInfo() throws IOException {
      protocol = new Protocol();
      buf = protocol.getPacket();
      
      protocol = new Protocol(Protocol.PT_REQ_WINDOW);
      
      protocol.setWindowNum("1");
      os.write(protocol.getPacket());
      
      while(true) {
         is.read(buf); 

          int packetType = buf[0];
          protocol.setPacket(packetType, buf);
          
          switch(packetType) {
          case Protocol.PT_RES_WINDOW:
             if(protocol.getCode().equals("1")){
                Stage primaryStage = new Stage();
                FXMLLoader mypage = new FXMLLoader(getClass().getResource("/Main/insertPassword.fxml"));
                primaryStage.setScene(new Scene(mypage.load()));
                primaryStage.setTitle("회원정보 수정");
                primaryStage.show();
                insertPasswordController controller = mypage.<insertPasswordController>getController();
                controller.setUser(user);
                controller.setStream(os, is);
                return;
             }
          }
      }
      
   }

   public void setMyPage(String myPage) {
      System.out.println("마이페이지 = " + myPage);
      StringTokenizer st = new StringTokenizer(myPage, "#");
      this.manager.setText(st.nextToken() + "(" + st.nextToken() + ")");
      this.name.setText(st.nextToken());
      this.university.setText(st.nextToken());
      this.phoneNum.setText(st.nextToken());
   }
   
   public void setStream(OutputStream os, InputStream is) {
      this.os = os;
      this.is = is;
   }
   
   
}