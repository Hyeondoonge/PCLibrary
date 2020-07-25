package PCManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import com.mysql.cj.protocol.Resultset;

import Server.SQL;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MG_SAV_StudentTable {
	ObservableList<MG_StudentContents> myList;

	public MG_SAV_StudentTable() {
		myList = FXCollections.observableArrayList();
	}
	
	public void setMyList(String studentList) {
		StringTokenizer st = new StringTokenizer(studentList, "#");
		//SQL sql = new SQL();
		//sql.requestOurSchoolStudentList(school);
		//ResultSet res = sql.getResultSet();
		//System.out.println(studentList);
		while(st.hasMoreTokens()) {
			String id = st.nextToken();
			System.out.println(id);
			String nickName = st.nextToken();
			System.out.println(nickName);
			String name = st.nextToken();
			System.out.println(name);
			String phoneNum = st.nextToken();
			System.out.println(phoneNum);
			String email = st.nextToken();
			System.out.println(email);
			System.out.println(id + " " + nickName + " " + name + " " + phoneNum + " " + email);
			myList.add(new MG_StudentContents(new SimpleStringProperty(id), new SimpleStringProperty(nickName), new SimpleStringProperty(name), new SimpleStringProperty(phoneNum), new SimpleStringProperty(email)));
		}
	}
	
	public ObservableList<MG_StudentContents> getMyList(){
		return myList;
	}
	
	
}
