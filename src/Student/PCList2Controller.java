package Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import Main.User;
import Server.Protocol;

public class PCList2Controller implements Initializable {

   @FXML
   Pagination page;
   @FXML
   Label id;
   @FXML
   Label state1,state2,state3,state4;
   @FXML
   Label model1,model2,model3,model4,model1_T,model2_T,model3_T,model4_T;
   @FXML
   Label cpu1,cpu2,cpu3,cpu4,cpu1_T,cpu2_T,cpu3_T,cpu4_T;
   @FXML
   Label gpu1,gpu2,gpu3,gpu4,gpu1_T,gpu2_T,gpu3_T,gpu4_T;
   @FXML
   Label ram1,ram2,ram3,ram4,ram1_T,ram2_T,ram3_T,ram4_T;
   @FXML
   Label manufacturer1,manufacturer2,manufacturer3,manufacturer4,
         manufacturer1_T,manufacturer2_T,manufacturer3_T,manufacturer4_T;
   @FXML
   Label left1,left2,left3,left4,left1_T,left2_T,left3_T,left4_T;
   @FXML
   Button apply1,apply2,apply3,apply4;
   @FXML
   ComboBox<String> stateChoice, sortChoice;
   
   ArrayList<String>[] list = null;
   
   private String ID, pw, name, nickName, phoneNum, mail, school;
   int code;
   int pcCnt = 0;
   
   private OutputStream os;
   private InputStream is;
   
   private StringTokenizer st;
   
   private boolean isBorrower = false;
   @Override
   public void initialize(URL arg0, ResourceBundle arg1) { 
      
      stateChoice.getItems().add("���þ���");
      stateChoice.getItems().add("�뿩 ����");
      sortChoice.getItems().add("���þ���");
      sortChoice.getItems().add("�� ���� ��");
      sortChoice.getItems().add("CPU���� ��");
      sortChoice.getItems().add("GPU���� ��");// ��й�ȣ, �޴���ȭ��ȣ ����
   }// ������ ��ư Ŭ����  n�� ������ pc��� ���� 4���� ������
      // TODO Auto-generated method stub
   
   public void setUser(User user) throws IOException {
      ID = user.getId();
      pw = user.getPassword();
      name = user.getName();
      nickName = user.getNickName();
      code = user.getCode();
      phoneNum = user.getPhoneNum();
      mail = user.getMail();
      school= user.getSchool();

      id.setText(ID+"("+nickName+")");
      
      Protocol protocol = new Protocol();
      byte[] buf = protocol.getPacket();
                         
      protocol = new Protocol(Protocol.PT_REQ_CANBORROW);
      protocol.setId(ID);
      os.write(protocol.getPacket());

      program: while (true) {

              is.read(buf); 

              int packetType = buf[0];
              protocol.setPacket(packetType, buf);
                        
              if (packetType == Protocol.PT_EXIT) {
                 System.out.println("Ŭ���̾�Ʈ ����");
                 break;
              }
                        
               switch (packetType) {

                     case Protocol.PT_RES_CANBORROW: 
                           String borCode = protocol.getCode();
                           if(borCode.equals("1")) {
                              isBorrower = true;
                           }
                           break program;
                          }
         }
      
      setPage(stateChoice.getValue(),sortChoice.getValue());
   //   setPage("�뿩 ����","�� ���� ��");
   }
      
   public void setPage(String firstc, String secondc) {
      try {
         
         Protocol protocol = new Protocol();
         byte[] buf = protocol.getPacket();
                            
         protocol = new Protocol(Protocol.PT_REQ_SORTED_PCLIST);
         
         if(firstc == null) {
            firstc = "���þ���";
         }
         if(secondc == null) {
            secondc = "���þ���";
         }
         // �ϴ� �׽�Ʈ������ CPU + �뿩���� ��
         protocol.setPC_School(school);
         protocol.setFirstchoice(firstc);
         protocol.setSecondchoice(secondc);
         os.write(protocol.getPacket());
         
         program: while (true) {

              is.read(buf); 

              int packetType = buf[0];
              protocol.setPacket(packetType, buf);
                        
              if (packetType == Protocol.PT_EXIT) {
                 System.out.println("Ŭ���̾�Ʈ ����");
                 
                 break;
              }
                        
               switch (packetType) {

                     case Protocol.PT_RES_SORTED_PCLIST: // �޾��� ��

                        st = new StringTokenizer(protocol.getSortedpcList()); 
                        pcCnt = Integer.parseInt(st.nextToken("#"));// �� DB�� ����� PC���� ��
                        
                        list = new ArrayList[pcCnt];
                        
                        for(int i= 0; i < pcCnt; i++) {
                           
                           String state = st.nextToken("#"); 
                           String manufacturer = st.nextToken("#");
                           String model = st.nextToken("#");
                           String cpu = st.nextToken("#");
                           String gpu = st.nextToken("#");
                           String ram = st.nextToken("#");
                           String left = st.nextToken("#");
                           
                           list[i] = new ArrayList();
                           
                           list[i].add(state); // state
                           list[i].add(model); // model
                           list[i].add(cpu); // cpu
                           list[i].add(ram); // ram
                           list[i].add(manufacturer); // manufacturer
                           list[i].add(gpu); // gpu
                           list[i].add(left);  // left
                        }
                        if(pcCnt%4 == 0) {
                           page.setPageCount(pcCnt/4);
                        }
                        else {
                           page.setPageCount(pcCnt/4+1);
                        }
                        
                        page.setPageFactory(new Callback<Integer, Node>() {
                               public Node call(Integer pageIndex) {
                                 return createPage(pageIndex);
                               }
                           }); 
                        break program;
                           }
                  }
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } 
   }
   
   public Pane createPage(int pageIndex) { 
      //�� �������� ����
      Pane pane = new Pane();
      
      if(page.getPageCount() == pageIndex+1) { //29��, 8������ 
         int temp = pcCnt - pageIndex*4;  // 29 - 28
         // 28, 29
         apply2.setVisible(false);
         apply3.setVisible(false);
         apply4.setVisible(false);
         
         model2_T.setVisible(false); model3_T.setVisible(false); model4_T.setVisible(false);
         cpu2_T.setVisible(false); cpu3_T.setVisible(false); cpu4_T.setVisible(false);
         gpu2_T.setVisible(false); gpu3_T.setVisible(false); gpu4_T.setVisible(false);
         ram2_T.setVisible(false); ram3_T.setVisible(false); ram4_T.setVisible(false);
         manufacturer2_T.setVisible(false); manufacturer3_T.setVisible(false); manufacturer4_T.setVisible(false);
         left2_T.setVisible(false); left3_T.setVisible(false); left4_T.setVisible(false);
         
         state1.setText(""); state2.setText(""); state3.setText(""); state4.setText("");
         model1.setText(""); model2.setText(""); model3.setText(""); model4.setText("");
         cpu1.setText(""); cpu2.setText(""); cpu3.setText(""); cpu4.setText("");
         gpu1.setText(""); gpu2.setText(""); gpu3.setText(""); gpu4.setText("");
         ram1.setText(""); ram2.setText(""); ram3.setText(""); ram4.setText("");
         manufacturer1.setText(""); manufacturer2.setText(""); manufacturer3.setText(""); manufacturer4.setText("");
         left1.setText(""); left2.setText(""); left3.setText(""); left4.setText("");
         

         left1.setText(list[4*pageIndex].get(6));
         
         if(temp >= 1) {
            state1.setText(list[4*pageIndex].get(0)); // ���¿� ���� ���� ����
               if(list[4*pageIndex].get(0).equals("�뿩 ����")) {
            	   left1_T.setText("�ܿ�����: ");
                  state1.setTextFill(Color.web("#14b4f8"));
               }
               else if(list[4*pageIndex].get(0).equals("�뿩 ��")){
            	  left1_T.setText("����: ");
                  state1.setTextFill(Color.web("#ef2c2c"));
               }
               else {
            	  left1_T.setText("����: ");
                  state1.setTextFill(Color.web("#828282"));
               }
               
            model1.setText(list[4*pageIndex].get(1));
            cpu1.setText(list[4*pageIndex].get(2));
            ram1.setText(list[4*pageIndex].get(3));
            manufacturer1.setText(list[4*pageIndex].get(4));
            gpu1.setText(list[4*pageIndex].get(5));
            
               if(list[4*pageIndex].get(0).equals("�뿩 ��") ||list[4*pageIndex].get(0).equals("���� ��")) { // 1��° PC
               apply1.setDisable(true);
            }
         }
         if(temp >= 2) {
            model2_T.setVisible(true);
            cpu2_T.setVisible(true); 
            gpu2_T.setVisible(true); 
            ram2_T.setVisible(true); 
            manufacturer2_T.setVisible(true); 
            left2_T.setVisible(true); 
            
            apply2.setVisible(true);
            
            state2.setText(list[4*pageIndex+1].get(0)); // ���¿� ���� ���� ����
               if(list[4*pageIndex+1].get(0).equals("�뿩 ����")) {
            	   left2_T.setText("�ܿ�����: ");
                  state2.setTextFill(Color.web("#14b4f8"));
               }
               else if(list[4*pageIndex+1].get(0).equals("�뿩 ��")){
            	  left2_T.setText("����: ");
                  state2.setTextFill(Color.web("#ef2c2c"));
               }
               else {
            	   left2_T.setText("����: ");
                  state2.setTextFill(Color.web("#828282"));
               }
            model2.setText(list[4*pageIndex+1].get(1));
            cpu2.setText(list[4*pageIndex+1].get(2));
            ram2.setText(list[4*pageIndex+1].get(3));
            manufacturer2.setText(list[4*pageIndex+1].get(4));
            gpu2.setText(list[4*pageIndex+1].get(5));
            left2.setText(list[4*pageIndex+1].get(6));
            
            if(list[4*pageIndex+1].get(0).equals("�뿩 ��") ||list[4*pageIndex+1].get(0).equals("���� ��")) {
               apply2.setDisable(true);
            }
            
         }
         if(temp >= 3) {
            model3_T.setVisible(true);
            cpu3_T.setVisible(true); 
            gpu3_T.setVisible(true); 
            ram3_T.setVisible(true); 
            manufacturer3_T.setVisible(true);
            left3_T.setVisible(true); 
            
            apply3.setVisible(true);
            
            state3.setText(list[4*pageIndex+2].get(0)); // ���¿� ���� ���� ����
               if(list[4*pageIndex+2].get(0).equals("�뿩 ����")) {
            	   left3_T.setText("�ܿ�����: ");
                  state3.setTextFill(Color.web("#14b4f8"));
               }
               else if(list[4*pageIndex+2].get(0).equals("�뿩 ��")){
            	   left3_T.setText("����: ");
                  state3.setTextFill(Color.web("#ef2c2c"));
               }
               else {
            	   left3_T.setText("����: ");
                  state3.setTextFill(Color.web("#828282"));
               }
            model3.setText(list[4*pageIndex+2].get(1));
            cpu3.setText(list[4*pageIndex+2].get(2));
            ram3.setText(list[4*pageIndex+2].get(3));
            manufacturer3.setText(list[4*pageIndex+2].get(4));
            gpu3.setText(list[4*pageIndex+2].get(5));
            left3.setText(list[4*pageIndex+2].get(6));

            if(list[4*pageIndex+2].get(0).equals("�뿩 ��") ||list[4*pageIndex+2].get(0).equals("���� ��")) {
               apply3.setDisable(true);
            }
         }
         if(temp >= 4) {
             model4_T.setVisible(true);
            cpu4_T.setVisible(true);
            gpu4_T.setVisible(true);
            ram4_T.setVisible(true);
            manufacturer4_T.setVisible(true);
            left4_T.setVisible(true);
            
            apply4.setVisible(true);
            
            state4.setText(list[4*pageIndex+3].get(0)); // ���¿� ���� ���� ����
               if(list[4*pageIndex+3].get(0).equals("�뿩 ����")) {
            	   left4_T.setText("�ܿ�����: ");
                  state4.setTextFill(Color.web("#14b4f8"));
               }
               else if(list[4*pageIndex+3].get(0).equals("�뿩 ��")){
            	   left4_T.setText("����: ");
                  state4.setTextFill(Color.web("#ef2c2c"));
               }
               else {
            	   left4_T.setText("����: ");
                  state4.setTextFill(Color.web("#828282"));
               }
            model4.setText(list[4*pageIndex+3].get(1));
            cpu4.setText(list[4*pageIndex+3].get(2));
            ram4.setText(list[4*pageIndex+3].get(3));
            manufacturer4.setText(list[4*pageIndex+3].get(4));
            gpu4.setText(list[4*pageIndex+3].get(5));
            left4.setText(list[4*pageIndex+3].get(6));

            if(list[4*pageIndex+3].get(0).equals("�뿩 ��") ||list[4*pageIndex+3].get(0).equals("���� ��")) {
               apply4.setDisable(true);
            }
         }
      }
         else {
            model2_T.setVisible(true); model3_T.setVisible(true); model4_T.setVisible(true);
            cpu2_T.setVisible(true); cpu3_T.setVisible(true); cpu4_T.setVisible(true);
            gpu2_T.setVisible(true); gpu3_T.setVisible(true); gpu4_T.setVisible(true);
            ram2_T.setVisible(true); ram3_T.setVisible(true); ram4_T.setVisible(true);
            manufacturer2_T.setVisible(true); manufacturer3_T.setVisible(true); manufacturer4_T.setVisible(true);
            left2_T.setVisible(true); left3_T.setVisible(true); left4_T.setVisible(true);
            state1.setText(list[4*pageIndex].get(0)); // ���¿� ���� ���� ����
         
            if(list[4*pageIndex].get(0).equals("�뿩 ����")) {
               left1_T.setText("�ܿ�����: ");
               state1.setTextFill(Color.web("#14b4f8"));
            }
            else if(list[4*pageIndex].get(0).equals("�뿩 ��")){
               left1_T.setText("����: ");
               state1.setTextFill(Color.web("#ef2c2c"));
            }
            else {
            	left1_T.setText("����: ");
               state1.setTextFill(Color.web("#828282"));
            }
         model1.setText(list[4*pageIndex].get(1));
         cpu1.setText(list[4*pageIndex].get(2));
         ram1.setText(list[4*pageIndex].get(3));
         manufacturer1.setText(list[4*pageIndex].get(4));
         gpu1.setText(list[4*pageIndex].get(5));
         left1.setText(list[4*pageIndex].get(6));
         
         state2.setText(list[4*pageIndex+1].get(0)); // ���¿� ���� ���� ����
            if(list[4*pageIndex+1].get(0).equals("�뿩 ����")) {
            	left2_T.setText("�ܿ�����: ");
               state2.setTextFill(Color.web("#14b4f8"));
            }
            else if(list[4*pageIndex+1].get(0).equals("�뿩 ��")){
            	left2_T.setText("����: ");
               state2.setTextFill(Color.web("#ef2c2c"));
            }
            else {
            	left2_T.setText("����: ");
               state2.setTextFill(Color.web("#828282"));
            }
         model2.setText(list[4*pageIndex+1].get(1));
         cpu2.setText(list[4*pageIndex+1].get(2));
         ram2.setText(list[4*pageIndex+1].get(3));
         manufacturer2.setText(list[4*pageIndex+1].get(4));
         gpu2.setText(list[4*pageIndex+1].get(5));
         left2.setText(list[4*pageIndex+1].get(6));
         
         state3.setText(list[4*pageIndex+2].get(0)); // ���¿� ���� ���� ����
            if(list[4*pageIndex+2].get(0).equals("�뿩 ����")) {
            	left3_T.setText("�ܿ�����: ");
               state3.setTextFill(Color.web("#14b4f8"));
            }
            else if(list[4*pageIndex+2].get(0).equals("�뿩 ��")){
               left3_T.setText("����: ");
               state3.setTextFill(Color.web("#ef2c2c"));
            }
            else {
               left3_T.setText("����: ");
               state3.setTextFill(Color.web("#828282"));
            }
            
         model3.setText(list[4*pageIndex+2].get(1));
         cpu3.setText(list[4*pageIndex+2].get(2));
         ram3.setText(list[4*pageIndex+2].get(3));
         manufacturer3.setText(list[4*pageIndex+2].get(4));
         gpu3.setText(list[4*pageIndex+2].get(5));
         left3.setText(list[4*pageIndex+2].get(6));
         
         state4.setText(list[4*pageIndex+3].get(0)); // ���¿� ���� ���� ����
            if(list[4*pageIndex+3].get(0).equals("�뿩 ����")) {
            	left4_T.setText("�ܿ�����: ");
               state4.setTextFill(Color.web("#14b4f8"));
            }
            else if(list[4*pageIndex+3].get(0).equals("�뿩 ��")){
               left4_T.setText("����: ");
               state4.setTextFill(Color.web("#ef2c2c"));
            }
            else {
               left4_T.setText("����: ");
               state4.setTextFill(Color.web("#828282"));
            }
         model4.setText(list[4*pageIndex+3].get(1));
         cpu4.setText(list[4*pageIndex+3].get(2));
         ram4.setText(list[4*pageIndex+3].get(3));
         manufacturer4.setText(list[4*pageIndex+3].get(4));
         gpu4.setText(list[4*pageIndex+3].get(5));
         left4.setText(list[4*pageIndex+3].get(6));
         
         apply1.setVisible(true); apply2.setVisible(true); apply3.setVisible(true); apply4.setVisible(true);
         apply1.setDisable(false); apply2.setDisable(false); apply3.setDisable(false); apply4.setDisable(false);
         //��ư ����
         if(list[4*pageIndex].get(0).equals("�뿩 ��") ||list[4*pageIndex].get(0).equals("���� ��")) { // 1��° PC
            apply1.setDisable(true);
         }
         if(list[4*pageIndex+1].get(0).equals("�뿩 ��") ||list[4*pageIndex+1].get(0).equals("���� ��")) {
            apply2.setDisable(true);
         }
         if(list[4*pageIndex+2].get(0).equals("�뿩 ��") ||list[4*pageIndex+2].get(0).equals("���� ��")) {
            apply3.setDisable(true);
         }
         if(list[4*pageIndex+3].get(0).equals("�뿩 ��") ||list[4*pageIndex+3].get(0).equals("���� ��")) {
            apply4.setDisable(true);
         }
      }
      return pane;
   }
   
   public void applyBtn1Handler() throws IOException { 
      if(isBorrower) { // �뿩�� O
         Alert alert = new Alert(AlertType.ERROR);
         alert.setContentText("���� �뿩���� PC�� �ֽ��ϴ�. ");
         alert.show();
      }
      else {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("applyforPC.fxml"));
         Parent root = loader.load();
         
         Scene scene = new Scene(root);
         Stage primaryStage =(Stage)apply2.getScene().getWindow();
         primaryStage.setScene(scene);
         
         applyforPCController controller = loader.<applyforPCController>getController();
         controller.setStream(os, is);
         controller.setUser(new User(ID, pw, name, nickName, code, phoneNum, mail, school));
         controller.setPCInform(model1.getText(), cpu1.getText(), ram1.getText(), 
               manufacturer1.getText(), gpu1.getText(), left1.getText());
      }

   }
   
   public void applyBtn2Handler() throws IOException{
          
      if(isBorrower) { // �뿩�� O
           Alert alert = new Alert(AlertType.ERROR);
           alert.setContentText("���� �뿩���� PC�� �ֽ��ϴ�. ");
           alert.show();
        }
      else {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("applyforPC.fxml"));
          Parent root = loader.load();
                              
          Scene scene = new Scene(root);
          Stage primaryStage =(Stage)apply2.getScene().getWindow();
          primaryStage.setScene(scene);
                              
          applyforPCController controller = loader.<applyforPCController>getController();
          controller.setStream(os, is);
          controller.setUser(new User(ID, pw,name, nickName, code, phoneNum, mail, school));
          controller.setPCInform(model2.getText(), cpu2.getText(), ram2.getText(), 
          manufacturer2.getText(), gpu2.getText(), left2.getText());
      }
   }
   
   public void applyBtn3Handler() throws IOException{ 
      if(isBorrower) { // �뿩�� O
         Alert alert = new Alert(AlertType.ERROR);
         alert.setContentText("���� �뿩���� PC�� �ֽ��ϴ�. ");
         alert.show();
        }
      else {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("applyforPC.fxml"));
         Parent root = loader.load();
                              
         Scene scene = new Scene(root);
         Stage primaryStage =(Stage)apply2.getScene().getWindow();
         primaryStage.setScene(scene);
                              
         applyforPCController controller = loader.<applyforPCController>getController();
         controller.setStream(os, is);
         controller.setUser(new User(ID, pw, name, nickName, code, phoneNum, mail, school));
         controller.setPCInform(model3.getText(), cpu3.getText(), ram3.getText(), 
         manufacturer3.getText(), gpu3.getText(), left3.getText());
            }

         }
   
   public void applyBtn4Handler() throws IOException{ 
      if(isBorrower) { // �뿩�� O
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("���� �뿩���� PC�� �ֽ��ϴ�. ");
        alert.show();
        }
      else {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("applyforPC.fxml"));
        Parent root = loader.load();
                              
        Scene scene = new Scene(root);
        Stage primaryStage =(Stage)apply2.getScene().getWindow();
        primaryStage.setScene(scene);
                              
        applyforPCController controller = loader.<applyforPCController>getController();
        controller.setStream(os, is);
        controller.setUser(new User(ID, pw, name, nickName, code, phoneNum, mail, school));
        controller.setPCInform(model4.getText(), cpu4.getText(), ram4.getText(), 
                           manufacturer4.getText(), gpu4.getText(), left4.getText());
       }
   
   }
   
   public void sortHandler() throws IOException{ 
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("�˸� �޽���");
      
      if(stateChoice.getValue() == null) {
         alert.setContentText("�ùٸ� �׸��� �ƴմϴ�.");
         alert.show();
      }
      else if(sortChoice.getValue() == null) {
         alert.setContentText("�ùٸ� �׸��� �ƴմϴ�.");
         alert.show();
      }
      else {
         setPage(stateChoice.getValue(), sortChoice.getValue());
      }
   }
   
   public void setStream(OutputStream os, InputStream is) {
      this.os = os;
      this.is = is;
   }
}