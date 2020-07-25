package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import Server.Protocol;
import Server.SQL;
import Student.applyforPCController;
import SystemOperator.SO_showDocumentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class guser_NoteController{

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
   @FXML
   Hyperlink attach;
   
   private User user;
   private ObservableList<Post> data;
   private OutputStream os;
   private InputStream is;
   private Post selectedPost;

   public void setUser(User user) throws IOException {
      this.user = user;
      
      setNotetable();
   }
   
   public void setStream(OutputStream os, InputStream is) {
      this.os = os;
      this.is = is;
   }
   
   public void showNote(){
      // TODO Auto-generated method stub

      // SQL에서 데이터 받아와야함
       
       table.setOnMouseClicked((MouseEvent event) -> {
               if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
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
                                      
                                     FXMLLoader mypage = new FXMLLoader(getClass().getResource("guser_showDocument.fxml"));
                                     Stage stage = new Stage();
                                   Parent root = mypage.load();
                                   
                                   guser_showDocumentController controller = mypage.<guser_showDocumentController>getController();
                                   
                                   int id = Integer.parseInt(st.nextToken());
                                   String title = st.nextToken();
                                   String publisher = st.nextToken();
                                   String date = st.nextToken();
                                   int view = Integer.parseInt(st.nextToken());
                                   String content = st.nextToken();
                                   
                                   controller.setDocument(id, title, publisher, date, view, content);
                                   stage.setTitle(title);
                                   stage.setScene(new Scene(root));
                                   stage.show();
                                     
                                   setNotetable();
                                   controller.setParent(this);
                                   controller.setUser(user);
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
   
   public void setNotetable() throws IOException {

          data = FXCollections.observableArrayList();
          
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
                              String title, publisher, date, imagePath, content;
                              
                              while(st.hasMoreTokens()) {
                                 
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
   }