package PCManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import Main.User;
import Server.SQL;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MG_SAV_PCTable {
	ObservableList<MG_PCTableContents> myList;

	public MG_SAV_PCTable() {
		myList = FXCollections.observableArrayList();
	}

	void setMyList(String data) {
		System.out.println(data);
		myList.clear();

		StringTokenizer st = new StringTokenizer(data, "#");

		while (st.hasMoreTokens()) {
			String ip = st.nextToken();
			String company = st.nextToken();
			String pcModel = st.nextToken();
			String cpu = st.nextToken();
			String gpu = st.nextToken();
			int ram = Integer.parseInt(st.nextToken());
			String pcId = st.nextToken();
			String password = st.nextToken();
			String state = st.nextToken();
			
			//System.out.println(ip + " " + company + " " + pcModel + " " + cpu + " " + gpu + " " + ram + " " + pcId + " " + password + " " + state);

			myList.add(new MG_PCTableContents(new SimpleStringProperty(ip),
					new SimpleStringProperty(company), new SimpleStringProperty(pcModel),
					new SimpleStringProperty(cpu), new SimpleStringProperty(gpu),
					new SimpleIntegerProperty(ram),
					new SimpleStringProperty(pcId), new SimpleStringProperty(password),
					new SimpleStringProperty(state)));
		}
	}

	public ObservableList<MG_PCTableContents> getMyList() {
		return myList;
	}

}
