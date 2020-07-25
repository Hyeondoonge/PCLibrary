package PCManager;

import java.sql.ResultSet;
import java.util.StringTokenizer;

import Server.SQL;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MG_SAV_PCLenderTable {
	ObservableList<MG_PCLenderContents> myList;
	SQL sql;
	
	public MG_SAV_PCLenderTable() {
		sql = new SQL();
		myList = FXCollections.observableArrayList();
	}
	
	public void setMyList(String info) {
		StringTokenizer st = new StringTokenizer(info, "#");
		
		try {
			while(st.hasMoreTokens()) {
				String id = st.nextToken();
				String name = st.nextToken();
				String ip = st.nextToken();
				String pc = st.nextToken();
				String pcId = st.nextToken();
				String password = st.nextToken();
				
				myList.add(new MG_PCLenderContents(new SimpleStringProperty(id), new SimpleStringProperty(name), new SimpleStringProperty(ip),
						new SimpleStringProperty(pc), new SimpleStringProperty(pcId), new SimpleStringProperty(password)));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<MG_PCLenderContents> getMyList(){
		return myList;
	}

}
