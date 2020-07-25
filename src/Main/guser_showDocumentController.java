package Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import Server.Protocol;
import SystemOperator.SO_NoteController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class guser_showDocumentController{
   
   @FXML
   Label title1;
   @FXML
   Label title2;
   @FXML
   Label title3;
   @FXML
   Label publisher;
   @FXML
   Label date;
   @FXML
   Label view;
   @FXML
   TextArea content;
   private int num;
   private Post post;
   private User user;
   private guser_NoteController parent;
   private OutputStream os;
   private InputStream is;

   public void setDocument(String title, String publisher, String date, int view) {
      if(title.length()>30) {
         title1.setText(title.substring(0, 30));
         title2.setText(title.substring(30, title.length()));
      }
      else {
         title3.setText(title);
      }
      this.publisher.setText(publisher);
      this.date.setText(date);
      this.view.setText(Integer.toString(view));
   }
   
   public void setDocument(int num, String title, String publisher, String date, int view, String content) {
      
      this.num = num;
      
      if(title.length()>30) {
         title1.setText(title.substring(0, 30));
         title2.setText(title.substring(30, title.length()));
      }
      else {
         title3.setText(title);
      }
      this.publisher.setText(publisher);
      this.date.setText(date);
      
      content = content.replaceAll("\\s\\s\\s\\s", "\n"); //어떻게 처리할지는 생각해보자
      this.content.setText(content);

      
      System.out.print(view);
      this.view.setText(Integer.toString(view));
   }

   public void setParent(guser_NoteController parent) {
      this.parent = parent;
      
   }

   public void setUser(User user) {
      this.user = user;
      
   }
   
   public void setStream(OutputStream os, InputStream is) {
      this.os = os;
      this.is = is;
   }
}